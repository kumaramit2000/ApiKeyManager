package com.example.apikeymanager.controller;

import com.example.apikeymanager.model.ApiKey;
import com.example.apikeymanager.service.ApiKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@RequestMapping(path="/keys")
@Slf4j
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping
    public ResponseEntity<ApiKey> createKey() {
        ApiKey newKey = apiKeyService.createKey();
        return new ResponseEntity<>(newKey, HttpStatus.CREATED);
    }

    @GetMapping("/keys")
    public ResponseEntity<?> retrieveKey() {
        log.info("here i am..............");
        ApiKey apiKey = apiKeyService.retrieveKey();
        if (apiKey == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Map.of("keyId", apiKey.getKeyId()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKeyInfo(@PathVariable String id) {
        ApiKey apiKey = apiKeyService.getKeyInfo(id);
        if (apiKey == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Map.of(
                "isBlocked", apiKey.isBlocked(),
                "blockedAt", apiKey.getBlockedAt(),
                "createdAt", apiKey.getCreatedAt()
        ), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKey(@PathVariable String id) {
        boolean isDeleted = apiKeyService.deleteKey(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> unblockKey(@PathVariable String id) {
        boolean isUnblocked = apiKeyService.unblockKey(id);
        return isUnblocked ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/keepalive/{id}")
    public ResponseEntity<?> keepAliveKey(@PathVariable String id) {
        boolean isAlive = apiKeyService.keepAlive(id);
        return isAlive ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
