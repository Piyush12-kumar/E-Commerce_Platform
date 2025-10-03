package com.example.ecommerce_project.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;


@Service
public class TokenBlacklistService {

    // Using ConcurrentHashMap for thread safety
    private final Map<String, Date> blacklistedTokens = new ConcurrentHashMap<>();


    public void blacklistToken(String token, Date expiryDate) {
        blacklistedTokens.put(token, expiryDate);

        // Clean up expired tokens (optional - can be scheduled to run periodically)
        cleanupExpiredTokens();
    }


    public boolean isBlacklisted(String token) {
        if (!blacklistedTokens.containsKey(token)) {
            return false;
        }

        // If token has expired, we can remove it from the blacklist
        Date expiryDate = blacklistedTokens.get(token);
        if (expiryDate.before(new Date())) {
            blacklistedTokens.remove(token);
            return false;
        }

        return true;
    }


    private void cleanupExpiredTokens() {
        Date now = new Date();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue().before(now));
    }
}
