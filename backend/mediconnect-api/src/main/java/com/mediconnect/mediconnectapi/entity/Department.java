package com.mediconnect.mediconnectapi.entity;


import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(
        name = "departments",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name", "hospital_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Department extends BaseEntity {


    @Column(nullable = false)
    private String name;


    private String description;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "hospital_id",
            nullable = false
    )
    private Hospital hospital;


}