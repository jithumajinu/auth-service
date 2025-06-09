package com.openapi.cloud.core.config;

import com.hazelcast.config.*;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance");

        // Configure network settings
        NetworkConfig network = config.getNetworkConfig();
        network.setPort(5701);
        network.setPortAutoIncrement(true);

        // Join configuration - use TCP/IP for simplicity in development
        JoinConfig join = network.getJoin();
        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().setEnabled(true);
        join.getTcpIpConfig().addMember("127.0.0.1");

        // Define cache maps
        MapConfig productCache = new MapConfig("productCache");
        productCache.setTimeToLiveSeconds(300); // 5 minutes TTL
        productCache.setMaxIdleSeconds(200);    // 3.3 minutes max idle time
        productCache.setEvictionConfig(
                new EvictionConfig()
                        .setEvictionPolicy(EvictionPolicy.LRU)
                        .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_PERCENTAGE)
                        .setSize(20) // Use up to 20% of free heap
        );

        // User cache for authentication queries
        MapConfig userCache = new MapConfig("userCache");
        userCache.setTimeToLiveSeconds(300); // 5 minutes TTL
        userCache.setMaxIdleSeconds(200);    // 3.3 minutes max idle time
        userCache.setEvictionConfig(
                new EvictionConfig()
                        .setEvictionPolicy(EvictionPolicy.LRU)
                        .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_PERCENTAGE)
                        .setSize(20)
        );

        // User roles cache for authorization queries
        MapConfig userRolesCache = new MapConfig("userRolesCache");
        userRolesCache.setTimeToLiveSeconds(300); // 5 minutes TTL
        userRolesCache.setMaxIdleSeconds(200);    // 3.3 minutes max idle time
        userRolesCache.setEvictionConfig(
                new EvictionConfig()
                        .setEvictionPolicy(EvictionPolicy.LRU)
                        .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_PERCENTAGE)
                        .setSize(20)
        );


        config.addMapConfig(productCache);
        config.addMapConfig(userCache);
        config.addMapConfig(userRolesCache);

        return config;
    }
}