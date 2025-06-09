package com.openapi.cloud.core.model.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testParameterizedConstructor() {
        // Test data
        String name = "Test User";
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";
        
        // Create user using the constructor
        User user = new User(name, username, email, password);
        
        // Verify all fields are set correctly
        assertEquals(name, user.getName());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        
        // Verify roles is initialized but empty
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
    }
}