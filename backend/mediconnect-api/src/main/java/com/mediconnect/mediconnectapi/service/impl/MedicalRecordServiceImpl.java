package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.request.CreateMedicalRecordRequest;
import com.mediconnect.mediconnectapi.dto.response.MedicalRecordResponse;
import com.mediconnect.mediconnectapi.entity.*;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.exception.BadRequestException;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.*;
import com.mediconnect.mediconnectapi.service.MedicalRecordService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MedicalRecordResponse createRecord(
            CreateMedicalRecordRequest request
    ) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor profile not found")
                );

        Appointment appointment = appointmentRepository.findById(
                        request.getAppointmentId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found")
                );

        // Ensure doctor owns appointment
        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new BadRequestException(
                    "You can only create records for your own appointments."
            );
        }

        // Only completed appointments can have records
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new BadRequestException(
                    "Medical records can only be created after appointment completion."
            );
        }

        // Prevent duplicates
        if (medicalRecordRepository.existsByAppointmentId(appointment.getId())) {
            throw new BadRequestException(
                    "Medical record already exists for this appointment."
            );
        }

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appointment);
        record.setDoctor(doctor);
        record.setPatient(appointment.getPatient());
        record.setDiagnosis(request.getDiagnosis());
        record.setSymptoms(request.getSymptoms());
        record.setTreatment(request.getTreatment());
        record.setPrescription(request.getPrescription());
        record.setNotes(request.getNotes());

        MedicalRecord saved = medicalRecordRepository.save(record);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getPatientRecords() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient profile not found")
                );

        return medicalRecordRepository
                .findByPatientIdOrderByCreatedAtDesc(patient.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getDoctorRecords() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor profile not found"));

        return medicalRecordRepository
                .findByDoctorIdOrderByCreatedAtDesc(doctor.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordResponse getDoctorRecord(UUID recordId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor profile not found"));

        MedicalRecord record = medicalRecordRepository
                .findByIdAndDoctorId(recordId, doctor.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medical record not found"));

        return mapToResponse(record);
    }

    @Override
    @Transactional
    public MedicalRecordResponse updateRecord(
            UUID recordId,
            CreateMedicalRecordRequest request
    ) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor profile not found"));

        MedicalRecord record = medicalRecordRepository
                .findByIdAndDoctorId(recordId, doctor.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medical record not found"));

        record.setDiagnosis(request.getDiagnosis());
        record.setSymptoms(request.getSymptoms());
        record.setTreatment(request.getTreatment());
        record.setPrescription(request.getPrescription());
        record.setNotes(request.getNotes());

        MedicalRecord updated = medicalRecordRepository.save(record);

        return mapToResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalRecordResponse getPatientRecord(UUID recordId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient profile not found"));

        MedicalRecord record = medicalRecordRepository
                .findByIdAndPatientId(recordId, patient.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Medical record not found"));

        return mapToResponse(record);
    }

    private MedicalRecordResponse mapToResponse(MedicalRecord record) {

        return new MedicalRecordResponse(
                record.getId(),
                record.getAppointment().getId(),
                record.getPatient().getFirstName()
                        + " "
                        + record.getPatient().getLastName(),
                record.getDoctor().getFirstName()
                        + " "
                        + record.getDoctor().getLastName(),
                record.getDiagnosis(),
                record.getSymptoms(),
                record.getTreatment(),
                record.getPrescription(),
                record.getNotes(),
                record.getCreatedAt()
        );
    }
}