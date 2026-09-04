package com.mediconnect.mediconnectapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "identity_lookup_audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdentityLookupAuditLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "performed_by_user_id",
            nullable = false
    )
    private User performedByUser;

    @Column(
            name = "action_type",
            nullable = false,
            length = 50
    )
    private String actionType;

    @Column(
            name = "identifier_type",
            nullable = false,
            length = 30
    )
    private String identifierType;

    @Column(
            name = "masked_identifier",
            nullable = false,
            length = 30
    )
    private String maskedIdentifier;

    @Column(
            name = "was_successful",
            nullable = false
    )
    private boolean successful;

    @Column(
            name = "failure_reason",
            length = 255
    )
    private String failureReason;

    @Column(
            name = "ip_address",
            nullable = false,
            length = 45
    )
    private String ipAddress;

    @Column(
            name = "user_agent",
            length = 500
    )
    private String userAgent;

    @Column(
            name = "provider",
            nullable = false,
            length = 50
    )
    private String provider;

    @Column(
            name = "queried_at",
            nullable = false
    )
    private LocalDateTime queriedAt;
}