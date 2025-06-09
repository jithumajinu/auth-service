package com.openapi.cloud.core.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter for logging HTTP requests and responses.
 */

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Generate unique ID for the request
        String requestId = UUID.randomUUID().toString();
        String sessionId = request.getSession(true).getId();
        String remoteIP = request.getRemoteAddr();
        
        // Wrap request and response to cache the content
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Add context to logs
            MDC.put("requestId", requestId);
            MDC.put("sessionId", sessionId);
            MDC.put("remoteIP", remoteIP);
            MDC.put("method", request.getMethod());
            MDC.put("uri", request.getRequestURI());
            
            // Log the request
            log.info("Request: {} {} from {}", request.getMethod(), request.getRequestURI(), remoteIP);
            
            // Continue with the filter chain
            filterChain.doFilter(requestWrapper, responseWrapper);
            
            // Log the response
            long duration = System.currentTimeMillis() - startTime;
            log.info("Response: {} {} - {} ({} ms)", request.getMethod(), request.getRequestURI(), 
                    responseWrapper.getStatus(), duration);
            
        } finally {
            // Copy content back to the original response
            responseWrapper.copyBodyToResponse();
            
            // Clear MDC context
            MDC.remove("requestId");
            MDC.remove("sessionId");
            MDC.remove("remoteIP");
            MDC.remove("method");
            MDC.remove("uri");
        }
    }
}