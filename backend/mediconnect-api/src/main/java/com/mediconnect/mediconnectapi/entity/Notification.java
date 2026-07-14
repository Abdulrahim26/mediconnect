package com.mediconnect.mediconnectapi.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(nullable = false)
    private String message;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;


    @Column(nullable = false)
    private boolean read = false;

}