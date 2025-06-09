package com.openapi.cloud.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * Configuration class for security authorization rules.
 * This class centralizes all endpoint authorization rules for better
 * maintainability.
 */
@Configuration
public class SecurityAuthorizationConfig {

    /**
     * Configures the authorization rules for HTTP requests.
     *
     * @param authConfig The authorization configuration to be modified
     */
    public void configureAuthorization(

            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authConfig) {

        authConfig
                // Swagger UI v3 (OpenAPI)
                .requestMatchers("/v3/api-docs/**", "/v3/api-docs.yaml", "/swagger-ui/**", "/swagger-ui.html",
                        "/webjars/**", "/swagger-resources/**", "/configuration/**")
                .permitAll()
                // Actuator endpoints for monitoring
                .requestMatchers("/actuator/**").permitAll()
                
                // Auth endpoints
                .requestMatchers("/api/auth/**").permitAll()

                .requestMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability").permitAll()
                
                // OpenAPI endpoints
                .requestMatchers("/openApis/**").permitAll()
                
                // CUSTOMER ENDPOINTS - SPECIFIC FIRST, THEN GENERAL
                // Specific customer endpoints with role requirements
                .requestMatchers("/api/customer/edit").hasRole("CUSTOMER_EDIT")
                .requestMatchers("/api/customer/delete").hasRole("CUSTOMER_DELETE")
                .requestMatchers("/api/customer/add").hasRole("CUSTOMER_ADD")
                .requestMatchers("/api/customer/**").hasRole("CUSTOMER")
                // General customer endpoints - this comes AFTER the specific ones
                // .requestMatchers("/api/customer/**").authenticated()
                
                // ADMIN FUNCTION
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasRole("MANAGER")


                // Multiple role options (user with ANY of these roles can access)
                .requestMatchers("/api/reports/**").hasAnyRole("ADMIN", "MANAGER")
                // Your other permitted paths
                .requestMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html",
                        "/**/*.css", "/**/*.js")
                .permitAll()

                .requestMatchers(HttpMethod.GET, "/api/health/**", "/api/users/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated();
    }
}