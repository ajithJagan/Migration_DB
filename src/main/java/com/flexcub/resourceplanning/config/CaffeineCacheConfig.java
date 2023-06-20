package com.flexcub.resourceplanning.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.concurrent.TimeUnit;

public class CaffeineCacheConfig {

    public CacheManager cacheManager() {
        CaffeineCacheManager cacheConfig = new CaffeineCacheManager("test");
        cacheConfig.setCaffeine(caffeineCacheBuilder());
        return cacheManager();
    }

    Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .weakKeys()
                .recordStats();
    }
}