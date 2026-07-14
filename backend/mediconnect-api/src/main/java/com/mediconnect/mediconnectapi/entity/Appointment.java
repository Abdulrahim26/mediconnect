package com.mediconnect.mediconnectapi.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;


    @Column(nullable = false)
    private LocalDate appointmentDate;


    @Column(nullable = false)
    private LocalTime appointmentTime;


    @Column(nullable = false)
    private String reason;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;


}