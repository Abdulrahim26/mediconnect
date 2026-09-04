package com.mediconnect.mediconnectapi.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IdentityAuditContext {

    private final String ipAddress;

    private final String userAgent;
}