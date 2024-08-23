package com.example.apikeymanager.model;

import java.time.LocalDateTime;

public class ApiKey {
    private String keyId;
    private LocalDateTime createdAt;
    private LocalDateTime blockedAt;
    private LocalDateTime lastKeepAlive;
    private boolean isBlocked;

    public ApiKey(String keyId) {
        this.keyId = keyId;
        this.createdAt = LocalDateTime.now();
        this.isBlocked = false;
    }

    // Getters and Setters

    public String getKeyId() {
        return keyId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(LocalDateTime blockedAt) {
        this.blockedAt = blockedAt;
    }

    public LocalDateTime getLastKeepAlive() {
        return lastKeepAlive;
    }

    public void setLastKeepAlive(LocalDateTime lastKeepAlive) {
        this.lastKeepAlive = lastKeepAlive;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
