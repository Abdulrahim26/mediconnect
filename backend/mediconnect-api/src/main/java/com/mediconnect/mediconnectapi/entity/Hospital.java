package com.mediconnect.mediconnectapi.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospitals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hospital extends BaseEntity {


    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private String location;


    private String address;


    private String phone;


    @Column(unique = true)
    private String email;

}