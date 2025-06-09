package com.openapi.cloud.core.controller;

import com.openapi.cloud.core.service.CacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheControllerTest {

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private CacheController cacheController;

    @Test
    void testGetAllCacheNames() {
        // Arrange
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("cache1");
        cacheNames.add("cache2");
        when(cacheService.getAllCacheNames()).thenReturn(cacheNames);

        // Act
        ResponseEntity<Set<String>> response = cacheController.getAllCacheNames();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cacheNames, response.getBody());
        verify(cacheService, times(1)).getAllCacheNames();
    }

    @Test
    void testGetCacheEntries() {
        // Arrange
        String cacheName = "testCache";
        Map<Object, Object> entries = new HashMap<>();
        entries.put("key1", "value1");
        entries.put("key2", "value2");
        when(cacheService.getCacheEntries(cacheName)).thenReturn(entries);

        // Act
        ResponseEntity<Map<Object, Object>> response = cacheController.getCacheEntries(cacheName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(entries, response.getBody());
        verify(cacheService, times(1)).getCacheEntries(cacheName);
    }

    @Test
    void testClearCache() {
        // Arrange
        String cacheName = "testCache";
        doNothing().when(cacheService).clearCache(cacheName);

        // Act
        ResponseEntity<String> response = cacheController.clearCache(cacheName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Cache 'testCache' cleared successfully", response.getBody());
        verify(cacheService, times(1)).clearCache(cacheName);
    }

    @Test
    void testClearAllCaches() {
        // Arrange
        doNothing().when(cacheService).clearAllCaches();

        // Act
        ResponseEntity<String> response = cacheController.clearAllCaches();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("All caches cleared successfully", response.getBody());
        verify(cacheService, times(1)).clearAllCaches();
    }
}