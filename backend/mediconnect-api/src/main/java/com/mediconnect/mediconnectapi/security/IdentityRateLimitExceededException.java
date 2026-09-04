package com.mediconnect.mediconnectapi.security;

public class IdentityRateLimitExceededException
        extends RuntimeException {

    public IdentityRateLimitExceededException(
            String message
    ) {

        super(message);
    }
}