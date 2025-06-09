package com.openapi.cloud.core.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;
    private final HazelcastInstance hazelcastInstance;

    /**
     * Clear a specific cache
     * 
     * @param cacheName the name of the cache to clear
     */
    public void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            log.info("Cache '{}' cleared successfully", cacheName);
        } else {
            log.warn("Cache '{}' not found", cacheName);
        }
    }

    /**
     * Clear all caches
     */
    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        });
        log.info("All caches cleared successfully");
    }
    
    /**
     * Get all cache names
     * 
     * @return set of cache names
     */
    public Set<String> getAllCacheNames() {
        return cacheManager.getCacheNames().stream().collect(Collectors.toSet());
    }
    
    /**
     * Get all entries from a specific cache
     * 
     * @param cacheName the name of the cache
     * @return map of cache entries (key-value pairs)
     */
    public Map<Object, Object> getCacheEntries(String cacheName) {
        Map<Object, Object> result = new HashMap<>();
        try {
            IMap<Object, Object> map = hazelcastInstance.getMap(cacheName);
            if (map != null) {
                result.putAll(map);
            }
        } catch (Exception e) {
            log.error("Error getting cache entries for {}: {}", cacheName, e.getMessage());
        }
        return result;
    }
}