package com.example.user_service.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheStore<K, V> {
    private final Cache<K, V> cache;

    public CacheStore(int expiryDuration, TimeUnit timeUnit) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiryDuration, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public V get(@NonNull K key) {
        log.info("Fetching value from cache for key: {} - {}", key, cache.getIfPresent(key));
        return cache.getIfPresent(key);
    }

    public void put(@NonNull K key, @NonNull V value) {
        log.info("Putting value in cache for key:value {}:{}", key, value);
        cache.put(key, value);
    }

    public void evict(@NonNull K key) {
        log.info("Removing value from cache for key: {}", key);
        cache.invalidate(key);
        log.info("Fetching value from cache for key- {}:{}", key, cache.getIfPresent(key));
    }


}
