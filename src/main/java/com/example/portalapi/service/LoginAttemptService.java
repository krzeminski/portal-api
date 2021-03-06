package com.example.portalapi.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private final LoadingCache<String, Integer> loginAttemptCache;

    public LoginAttemptService() {
        super();
        loginAttemptCache = CacheBuilder.newBuilder().expireAfterWrite(15, MINUTES)
                .maximumSize(100).build(new CacheLoader<>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void evictUserFromLoginAttemptCache(String userEmail) {
        loginAttemptCache.invalidate(userEmail);
    }

    public void addUserToLoginAttemptCache(String userEmail) {
        int attempts = 0;
        try {
            attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(userEmail);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        loginAttemptCache.put(userEmail, attempts);
    }

    public boolean hasExceededMaxAttempts(String userEmail) {
        try {
            return loginAttemptCache.get(userEmail) >= MAXIMUM_NUMBER_OF_ATTEMPTS;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }


    public int getNumberOfAttempts(String userEmail){
        try {
            return loginAttemptCache.get(userEmail);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 100;
    }
}
