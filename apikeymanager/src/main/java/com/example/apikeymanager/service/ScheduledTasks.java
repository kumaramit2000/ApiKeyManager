package com.example.apikeymanager.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private final ApiKeyService apiKeyService;

    public ScheduledTasks(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @Scheduled(fixedRate = 60000)
    public void manageKeys() {
        apiKeyService.expireKeys();
        apiKeyService.releaseBlockedKeys();
    }
}
