package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.response.AppointmentSearchResponse;
import com.mediconnect.mediconnectapi.dto.response.DepartmentSearchResponse;
import com.mediconnect.mediconnectapi.dto.response.DoctorSearchResponse;
import com.mediconnect.mediconnectapi.dto.response.HospitalSearchResponse;
import com.mediconnect.mediconnectapi.entity.Appointment;
import com.mediconnect.mediconnectapi.entity.Department;
import com.mediconnect.mediconnectapi.entity.Doctor;
import com.mediconnect.mediconnectapi.entity.Hospital;
import com.mediconnect.mediconnectapi.entity.enums.AppointmentStatus;
import com.mediconnect.mediconnectapi.repository.AppointmentRepository;
import com.mediconnect.mediconnectapi.repository.DepartmentRepository;
import com.mediconnect.mediconnectapi.repository.DoctorRepository;
import com.mediconnect.mediconnectapi.repository.HospitalRepository;
import com.mediconnect.mediconnectapi.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final DoctorRepository doctorRepository;
    private final HospitalRepository hospitalRepository;
    private final DepartmentRepository departmentRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DoctorSearchResponse> searchDoctors(
            String firstName,
            String lastName,
            String specialty,
            String department,
            String hospital,
            int page,
            int size,
            String sortBy
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Doctor> doctors;

        if (firstName != null) {
            doctors = doctorRepository.findByFirstNameContainingIgnoreCase(firstName, pageable);
        } else if (lastName != null) {
            doctors = doctorRepository.findByLastNameContainingIgnoreCase(lastName, pageable);
        } else if (specialty != null) {
            doctors = doctorRepository.findBySpecialtyContainingIgnoreCase(specialty, pageable);
        } else if (department != null) {
            doctors = doctorRepository.findByDepartmentNameContainingIgnoreCase(department, pageable);
        } else if (hospital != null) {
            doctors = doctorRepository.findByDepartmentHospitalNameContainingIgnoreCase(hospital, pageable);
        } else {
            doctors = doctorRepository.findAll(pageable);
        }

        return doctors.map(this::convertToDoctorResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HospitalSearchResponse> searchHospitals(
            String name,
            String location,
            int page,
            int size,
            String sortBy
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Hospital> hospitals;

        if (name != null) {
            hospitals = hospitalRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (location != null) {
            hospitals = hospitalRepository.findByLocationContainingIgnoreCase(location, pageable);
        } else {
            hospitals = hospitalRepository.findAll(pageable);
        }

        return hospitals.map(this::convertHospitalToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentSearchResponse> searchDepartments(
            String name,
            String hospital,
            int page,
            int size,
            String sortBy
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Department> departments;

        if (name != null) {
            departments = departmentRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (hospital != null) {
            departments = departmentRepository.findByHospitalNameContainingIgnoreCase(hospital, pageable);
        } else {
            departments = departmentRepository.findAll(pageable);
        }

        return departments.map(this::convertDepartmentToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentSearchResponse> searchAppointments(
            AppointmentStatus status,
            LocalDate date,
            String doctor,
            String patient,
            int page,
            int size,
            String sortBy
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Appointment> appointments;

        if (status != null) {
            appointments = appointmentRepository.findByStatus(status, pageable);
        } else if (date != null) {
            appointments = appointmentRepository.findByAppointmentDate(date, pageable);
        } else if (doctor != null) {
            appointments = appointmentRepository
                    .findByDoctorFirstNameContainingIgnoreCaseOrDoctorLastNameContainingIgnoreCase(
                            doctor,
                            doctor,
                            pageable
                    );
        } else if (patient != null) {
            appointments = appointmentRepository
                    .findByPatientFirstNameContainingIgnoreCaseOrPatientLastNameContainingIgnoreCase(
                            patient,
                            patient,
                            pageable
                    );
        } else {
            appointments = appointmentRepository.findAll(pageable);
        }

        return appointments.map(this::convertAppointmentToResponse);
    }

    private DoctorSearchResponse convertToDoctorResponse(Doctor doctor) {
        return new DoctorSearchResponse(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialty(),
                doctor.getQualification(),
                doctor.getConsultationFee(),
                doctor.getDepartment().getName(),
                doctor.getDepartment().getHospital().getName()
        );
    }

    private HospitalSearchResponse convertHospitalToResponse(Hospital hospital) {
        return new HospitalSearchResponse(
                hospital.getId(),
                hospital.getName(),
                hospital.getLocation(),
                hospital.getAddress(),
                hospital.getPhone(),
                hospital.getEmail()
        );
    }

    private DepartmentSearchResponse convertDepartmentToResponse(Department department) {
        return new DepartmentSearchResponse(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getHospital().getName()
        );
    }

    private AppointmentSearchResponse convertAppointmentToResponse(Appointment appointment) {
        return new AppointmentSearchResponse(
                appointment.getId(),
                appointment.getPatient().getFirstName()
                        + " "
                        + appointment.getPatient().getLastName(),
                appointment.getDoctor().getFirstName()
                        + " "
                        + appointment.getDoctor().getLastName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus().name(),
                appointment.getReason()
        );
    }
}