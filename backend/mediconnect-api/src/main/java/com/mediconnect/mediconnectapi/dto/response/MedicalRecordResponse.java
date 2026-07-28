package com.mediconnect.mediconnectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class MedicalRecordResponse {

    private UUID id;                    // ✅ Position 1
    private UUID appointmentId;          // ✅ Position 2
    private String patientName;          // ✅ Position 3
    private String doctorName;           // ✅ Position 4
    private String diagnosis;            // ✅ Position 5
    private String symptoms;             // ✅ Position 6 (after diagnosis)
    private String treatment;            // ✅ Position 7
    private String prescription;         // ✅ Position 8
    private String notes;                // ✅ Position 9
    private LocalDateTime createdAt;     // ✅ Position 10
}