package com.mediconnect.mediconnectapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdentityRateLimitCleanup {

    private final IdentityRateLimitService rateLimitService;


    /**
     * Cleanup every 60 seconds.
     *
     * Prevents expired rate-limit entries
     * from accumulating in memory.
     */
    @Scheduled(fixedRate = 60_000)
    public void cleanup() {

        rateLimitService.cleanupExpiredEntries();
    }
}