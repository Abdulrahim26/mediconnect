package com.mediconnect.mediconnectapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receptionists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Receptionist extends BaseEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "hospital_id",
            nullable = false
    )
    private Hospital hospital;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;
}