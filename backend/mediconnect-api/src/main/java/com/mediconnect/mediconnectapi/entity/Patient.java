package com.mediconnect.mediconnectapi.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends BaseEntity {


    @Column(nullable = false)
    private String firstName;


    @Column(nullable = false)
    private String lastName;


    private String phone;


    private LocalDate dateOfBirth;


    private String gender;


    private String address;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}