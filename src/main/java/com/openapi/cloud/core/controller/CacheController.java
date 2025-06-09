package com.openapi.cloud.core.controller;

import java.util.Map;
import java.util.Set;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openapi.cloud.core.service.CacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/admin/cache")
@Slf4j
@RequiredArgsConstructor
public class CacheController {

    private final CacheService cacheService;

    

    /**
     * Get all cache names
     * 
     * @return set of cache names
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<String>> getAllCacheNames() {
        log.info("Request to get all cache names");
        return ResponseEntity.ok(cacheService.getAllCacheNames());
    }
    
    /**
     * Get all entries from a specific cache
     * 
     * @param cacheName the name of the cache
     * @return map of cache entries
     */
    @GetMapping("/{cacheName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<Object, Object>> getCacheEntries(@PathVariable String cacheName) {
        log.info("Request to get entries from cache: {}", cacheName);
        return ResponseEntity.ok(cacheService.getCacheEntries(cacheName));
    }

    /**
     * Clear a specific cache
     * 
     * @param cacheName the name of the cache to clear
     * @return response entity
     */
    @DeleteMapping("/{cacheName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> clearCache(@PathVariable String cacheName) {
        log.info("Request to clear cache: {}", cacheName);
        cacheService.clearCache(cacheName);
        return ResponseEntity.ok("Cache '" + cacheName + "' cleared successfully");
    }

    /**
     * Clear all caches
     * 
     * @return response entity
     */
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> clearAllCaches() {
        log.info("Request to clear all caches");
        cacheService.clearAllCaches();
        return ResponseEntity.ok("All caches cleared successfully");
    }
}