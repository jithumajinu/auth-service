package com.openapi.cloud.core.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActuatorConfigTest {

    @Mock
    private WebEndpointProperties webEndpointProperties;
    
    @Mock
    private WebEndpointProperties.Discovery discovery;
    
    @Mock
    private Environment environment;
    
    @InjectMocks
    private ActuatorConfig actuatorConfig;

    @Test
    void shouldRegisterLinksMapping_WhenDiscoveryEnabledAndBasePathNotEmpty() throws Exception {
        // Given
        when(webEndpointProperties.getDiscovery()).thenReturn(discovery);
        when(discovery.isEnabled()).thenReturn(true);
        String basePath = "/actuator";
        
        // When
        Method method = ActuatorConfig.class.getDeclaredMethod("shouldRegisterLinksMapping", 
                WebEndpointProperties.class, Environment.class, String.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(actuatorConfig, webEndpointProperties, environment, basePath);
        
        // Then
        assertTrue(result);
    }
    
//    @Test
//    void shouldRegisterLinksMapping_WhenDiscoveryEnabledAndSameManagementPort() throws Exception {
//        // Given
//        when(webEndpointProperties.getDiscovery()).thenReturn(discovery);
//        when(discovery.isEnabled()).thenReturn(true);
//        when(environment.getProperty("management.server.port")).thenReturn(null);
//        String basePath = "";
//
//        // When
//        Method method = ActuatorConfig.class.getDeclaredMethod("shouldRegisterLinksMapping",
//                WebEndpointProperties.class, Environment.class, String.class);
//        method.setAccessible(true);
//        boolean result = (boolean) method.invoke(actuatorConfig, webEndpointProperties, environment, basePath);
//
//        // Then
//        assertTrue(result);
//    }
    
    @Test
    void shouldRegisterLinksMapping_WhenDiscoveryDisabled() throws Exception {
        // Given
        when(webEndpointProperties.getDiscovery()).thenReturn(discovery);
        when(discovery.isEnabled()).thenReturn(false);
        String basePath = "/actuator";
        
        // When
        Method method = ActuatorConfig.class.getDeclaredMethod("shouldRegisterLinksMapping", 
                WebEndpointProperties.class, Environment.class, String.class);
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(actuatorConfig, webEndpointProperties, environment, basePath);
        
        // Then
        assertFalse(result);
    }
    
//    @Test
//    void shouldRegisterLinksMapping_WhenDiscoveryEnabledButDifferentManagementPortAndEmptyBasePath() throws Exception {
//        // Given
//        when(webEndpointProperties.getDiscovery()).thenReturn(discovery);
//        when(discovery.isEnabled()).thenReturn(true);
//        when(environment.getProperty("management.server.port")).thenReturn("8081");
//        String basePath = "";
//
//        // When
//        Method method = ActuatorConfig.class.getDeclaredMethod("shouldRegisterLinksMapping",
//                WebEndpointProperties.class, Environment.class, String.class);
//        method.setAccessible(true);
//        boolean result = (boolean) method.invoke(actuatorConfig, webEndpointProperties, environment, basePath);
//
//        // Then
//        assertFalse(result);
//    }
}