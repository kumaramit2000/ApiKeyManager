package com.example.apikeymanager.service;

import com.example.apikeymanager.model.ApiKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

@Service
@Slf4j
public class ApiKeyService {

    private final Map<String, ApiKey> keyMap = new ConcurrentHashMap<>();
    private final PriorityBlockingQueue<ApiKey> expirationQueue = new PriorityBlockingQueue<>(100, Comparator.comparing(ApiKey::getLastKeepAlive));
    private final Random random = new Random();

    public ApiKey createKey() {
        String keyId = UUID.randomUUID().toString();
        ApiKey apiKey = new ApiKey(keyId);
        apiKey.setLastKeepAlive(LocalDateTime.now().plusMinutes(5));
        keyMap.put(keyId, apiKey);
        expirationQueue.add(apiKey);
        return apiKey;
    }

    public ApiKey retrieveKey() {
        log.info("here i am 2..............");
        List<ApiKey> unblockedKeys = new ArrayList<>();
        for (ApiKey key : keyMap.values()) {
            if (!key.isBlocked()) {
                unblockedKeys.add(key);
            }
        }

        if (unblockedKeys.isEmpty()) {
            return null;
        }

        ApiKey apiKey = unblockedKeys.get(random.nextInt(unblockedKeys.size()));
        apiKey.setBlocked(true);
        apiKey.setBlockedAt(LocalDateTime.now());
        return apiKey;
    }

    public ApiKey getKeyInfo(String keyId) {
        return keyMap.get(keyId);
    }

    public boolean deleteKey(String keyId) {
        ApiKey removedKey = keyMap.remove(keyId);
        return removedKey != null && expirationQueue.remove(removedKey);
    }

    public boolean unblockKey(String keyId) {
        ApiKey apiKey = keyMap.get(keyId);
        if (apiKey == null) {
            return false;
        }
        apiKey.setBlocked(false);
        apiKey.setBlockedAt(null);
        return true;
    }

    public boolean keepAlive(String keyId) {
        ApiKey apiKey = keyMap.get(keyId);
        if (apiKey == null) {
            return false;
        }
        apiKey.setLastKeepAlive(LocalDateTime.now().plusMinutes(5));
        expirationQueue.remove(apiKey);
        expirationQueue.add(apiKey);
        return true;
    }

    public void expireKeys() {
        LocalDateTime now = LocalDateTime.now();
        while (!expirationQueue.isEmpty() && expirationQueue.peek().getLastKeepAlive().isBefore(now)) {
            ApiKey expiredKey = expirationQueue.poll();
            keyMap.remove(expiredKey.getKeyId());
        }
    }

    public void releaseBlockedKeys() {
        LocalDateTime now = LocalDateTime.now();
        for (ApiKey key : keyMap.values()) {
            if (key.isBlocked() && key.getBlockedAt().plusSeconds(60).isBefore(now)) {
                key.setBlocked(false);
                key.setBlockedAt(null);
            }
        }
    }
}
