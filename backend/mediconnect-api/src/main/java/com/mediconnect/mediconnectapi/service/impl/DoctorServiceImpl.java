package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.request.CreateDoctorRequest;
import com.mediconnect.mediconnectapi.dto.request.UpdateDoctorProfileRequest;
import com.mediconnect.mediconnectapi.dto.request.UpdateDoctorRequest;
import com.mediconnect.mediconnectapi.dto.response.DoctorResponse;
import com.mediconnect.mediconnectapi.entity.*;
import com.mediconnect.mediconnectapi.exception.BadRequestException;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.*;
import com.mediconnect.mediconnectapi.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ======================================================
    // 1. CREATE DOCTOR (HOSPITAL ADMIN)
    // ======================================================
    @Transactional
    @Override
    public DoctorResponse createDoctor(CreateDoctorRequest request) {

        // Get logged-in hospital admin
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User hospitalAdmin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (hospitalAdmin.getHospital() == null) {
            throw new BadRequestException("Hospital admin is not assigned to a hospital");
        }

        // Check doctor email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // Get DOCTOR role
        Role doctorRole = roleRepository.findByName("DOCTOR")
                .orElseThrow(() -> new ResourceNotFoundException("DOCTOR role not found"));

        // Create login user
        User doctorUser = new User();
        doctorUser.setEmail(request.getEmail());
        doctorUser.setPassword(passwordEncoder.encode(request.getPassword()));
        doctorUser.setRole(doctorRole);
        doctorUser.setHospital(hospitalAdmin.getHospital());

        User savedUser = userRepository.save(doctorUser);

        // Find department - SECURITY: Verify department belongs to admin's hospital
        Department department = departmentRepository
                .findByIdAndHospitalId(
                        request.getDepartmentId(),
                        hospitalAdmin.getHospital().getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found in your hospital"
                        )
                );

        // Create doctor profile
        Doctor doctor = new Doctor();
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setSpecialty(request.getSpecialty());
        doctor.setQualification(request.getQualification());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());
        doctor.setConsultationFee(request.getConsultationFee());
        doctor.setDepartment(department);
        doctor.setUser(savedUser);

        Doctor savedDoctor = doctorRepository.save(doctor);

        System.out.println(savedDoctor.getId());
        System.out.println(savedDoctor.getFirstName());
        System.out.println(savedDoctor.getLastName());
        System.out.println(savedDoctor.getSpecialty());
        System.out.println(savedDoctor.getEmail());
        System.out.println(savedDoctor.getDepartment().getName());

        return mapToResponse(savedDoctor);
    }

    // ======================================================
    // 2. GET ALL DOCTORS IN HOSPITAL
    // ======================================================
    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getHospitalDoctors() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User hospitalAdmin = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        if (hospitalAdmin.getHospital() == null) {
            throw new BadRequestException(
                    "Hospital admin is not assigned to a hospital"
            );
        }

        return doctorRepository
                .findByDepartmentHospitalId(
                        hospitalAdmin.getHospital().getId()
                )
                .stream()
                .filter(Doctor::isActive)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ======================================================
    // 3. GET SINGLE DOCTOR
    // ======================================================
    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctor(UUID doctorId) {

        User hospitalAdmin = getCurrentHospitalAdmin();

        Doctor doctor = doctorRepository
                .findByIdAndDepartmentHospitalId(
                        doctorId,
                        hospitalAdmin.getHospital().getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found")
                );

        return mapToResponse(doctor);
    }

    // ======================================================
    // 4. UPDATE DOCTOR (HOSPITAL ADMIN)
    // ======================================================
    @Transactional
    @Override
    public DoctorResponse updateDoctor(UUID doctorId, UpdateDoctorRequest request) {

        User hospitalAdmin = getCurrentHospitalAdmin();

        Doctor doctor = doctorRepository
                .findByIdAndDepartmentHospitalId(
                        doctorId,
                        hospitalAdmin.getHospital().getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found")
                );

        // Update doctor fields (email is NOT updated here - it stays the same)
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setSpecialty(request.getSpecialty());
        doctor.setQualification(request.getQualification());
        doctor.setPhone(request.getPhone());
        // doctor.setEmail(request.getEmail()); // REMOVED: Email cannot be changed by admin
        doctor.setConsultationFee(request.getConsultationFee());

        // Update department if changed - SECURITY: Verify department belongs to admin's hospital
        if (!doctor.getDepartment().getId().equals(request.getDepartmentId())) {

            Department department = departmentRepository
                    .findByIdAndHospitalId(
                            request.getDepartmentId(),
                            hospitalAdmin.getHospital().getId()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Department not found in your hospital"
                            )
                    );

            doctor.setDepartment(department);
        }

        Doctor updatedDoctor = doctorRepository.save(doctor);

        return mapToResponse(updatedDoctor);
    }

    // ======================================================
    // 5. DEACTIVATE DOCTOR
    // ======================================================
    @Transactional
    @Override
    public String deactivateDoctor(UUID doctorId) {

        User hospitalAdmin = getCurrentHospitalAdmin();

        Doctor doctor = doctorRepository
                .findByIdAndDepartmentHospitalId(
                        doctorId,
                        hospitalAdmin.getHospital().getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found")
                );

        doctor.setActive(false);
        doctorRepository.save(doctor);

        return "Doctor deactivated successfully";
    }

    // ======================================================
    // 6. DOCTOR VIEW OWN PROFILE
    // ======================================================
    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getMyProfile() {

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

        return mapToResponse(doctor);
    }

    // ======================================================
    // 7. DOCTOR UPDATE OWN PROFILE
    // ======================================================
    @Override
    @Transactional
    public DoctorResponse updateMyProfile(UpdateDoctorProfileRequest request) {

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

        // Update only allowed fields
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setPhone(request.getPhone());
        doctor.setQualification(request.getQualification());
        doctor.setConsultationFee(request.getConsultationFee());

        // Update specialty if provided
        if (request.getSpecialty() != null) {
            doctor.setSpecialty(request.getSpecialty());
        }

        Doctor updatedDoctor = doctorRepository.save(doctor);

        return mapToResponse(updatedDoctor);
    }

    // ======================================================
    // 8. HELPER: GET CURRENT HOSPITAL ADMIN
    // ======================================================
    private User getCurrentHospitalAdmin() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }

    // ======================================================
    // 9. HELPER: MAP DOCTOR TO RESPONSE
    // ======================================================
    private DoctorResponse mapToResponse(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialty(),
                doctor.getQualification(),
                doctor.getPhone(),
                doctor.getEmail(),
                doctor.getConsultationFee(),
                doctor.getDepartment().getId(),
                doctor.getDepartment().getName()
        );
    }
}