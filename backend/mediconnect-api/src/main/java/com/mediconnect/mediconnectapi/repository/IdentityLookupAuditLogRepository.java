package com.mediconnect.mediconnectapi.repository;

import com.mediconnect.mediconnectapi.entity.IdentityLookupAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IdentityLookupAuditLogRepository
        extends JpaRepository<IdentityLookupAuditLog, UUID> {
}