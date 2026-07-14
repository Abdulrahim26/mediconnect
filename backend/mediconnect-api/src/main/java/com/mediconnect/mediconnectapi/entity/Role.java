package com.mediconnect.mediconnectapi.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {


    @Column(nullable = false, unique = true)
    private String name;

}