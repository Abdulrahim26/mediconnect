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
            name = "doctor_id",
            nullable = false
    )
    private Doctor doctor;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "patient_id",
            nullable = false
    )
    private Patient patient;



    @Column(columnDefinition = "TEXT", nullable = false)
    private String diagnosis;



    @Column(columnDefinition = "TEXT")
    private String symptoms;



    @Column(columnDefinition = "TEXT")
    private String treatment;



    @Column(columnDefinition = "TEXT")
    private String prescription;



    @Column(columnDefinition = "TEXT")
    private String notes;

}