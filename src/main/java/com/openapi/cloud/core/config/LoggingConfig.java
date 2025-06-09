package com.openapi.cloud.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration for logging features.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableScheduling
public class LoggingConfig {
    // Configuration is handled through annotations and logback-spring.xml
}