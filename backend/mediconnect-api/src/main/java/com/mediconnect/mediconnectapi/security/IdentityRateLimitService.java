package com.mediconnect.mediconnectapi.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdentityRateLimitService {

    private static final int MAX_REQUESTS = 5;

    private static final long WINDOW_SECONDS = 60;

    private final Map<String, RateLimitEntry> requests =
            new ConcurrentHashMap<>();


    /**
     * Checks whether an identity verification request is allowed.
     *
     * Limit:
     * 5 requests per user/IP/verification-type combination
     * within 60 seconds.
     *
     * This means Ghana Card and NHIS have independent
     * rate-limit buckets.
     */
    public boolean isAllowed(
            String userId,
            String ipAddress,
            IdentityVerificationType verificationType
    ) {

        String key =
                buildKey(
                        userId,
                        ipAddress,
                        verificationType
                );

        Instant now = Instant.now();

        RateLimitEntry entry = requests.compute(
                key,
                (ignored, existing) ->
                        updateEntry(existing, now)
        );

        return entry.requestCount <= MAX_REQUESTS;
    }


    /**
     * Returns the number of seconds remaining
     * before the current rate-limit window resets.
     */
    public long getRetryAfterSeconds(
            String userId,
            String ipAddress,
            IdentityVerificationType verificationType
    ) {

        String key =
                buildKey(
                        userId,
                        ipAddress,
                        verificationType
                );

        RateLimitEntry entry =
                requests.get(key);

        if (entry == null) {
            return 0;
        }

        long elapsed =
                Instant.now()
                        .getEpochSecond()
                        - entry.windowStart
                        .getEpochSecond();

        long remaining =
                WINDOW_SECONDS - elapsed;

        return Math.max(0, remaining);
    }


    /**
     * Removes expired entries.
     *
     * This prevents the in-memory map from growing indefinitely.
     */
    public void cleanupExpiredEntries() {

        Instant now = Instant.now();

        requests.entrySet().removeIf(entry -> {

            long elapsed =
                    now.getEpochSecond()
                            - entry.getValue()
                            .windowStart
                            .getEpochSecond();

            return elapsed >= WINDOW_SECONDS;
        });
    }


    private RateLimitEntry updateEntry(
            RateLimitEntry existing,
            Instant now
    ) {

        if (existing == null) {

            return new RateLimitEntry(
                    now,
                    1
            );
        }


        long elapsed =
                now.getEpochSecond()
                        - existing.windowStart
                        .getEpochSecond();


        /*
         * Start a new window after 60 seconds.
         */
        if (elapsed >= WINDOW_SECONDS) {

            return new RateLimitEntry(
                    now,
                    1
            );
        }


        /*
         * Existing window.
         */
        return new RateLimitEntry(
                existing.windowStart,
                existing.requestCount + 1
        );
    }


    private String buildKey(
            String userId,
            String ipAddress,
            IdentityVerificationType verificationType
    ) {

        return userId
                + ":"
                + ipAddress
                + ":"
                + verificationType.name();
    }


    private static class RateLimitEntry {

        private final Instant windowStart;

        private final int requestCount;


        private RateLimitEntry(
                Instant windowStart,
                int requestCount
        ) {

            this.windowStart = windowStart;

            this.requestCount = requestCount;
        }
    }
}