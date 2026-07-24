package com.mediconnect.mediconnectapi.entity;


import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord extends BaseEntity {


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "appointment_id",
            nullable = false,
            unique = true
    )
    private Appointment appointment;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "patient_id",
            nullable = false
    )
    private Patient patient;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "doctor_id",
            nullable = false
    )
    private Doctor doctor;



    @Column(nullable = false)
    private String diagnosis;



    @Column(nullable = false)
    private String treatment;



    private String prescription;



    @Column(length = 2000)
    private String notes;

}