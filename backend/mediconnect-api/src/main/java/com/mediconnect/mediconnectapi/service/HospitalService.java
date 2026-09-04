package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.CreateHospitalRequest;
import com.mediconnect.mediconnectapi.dto.request.UpdateHospitalRequest;
import com.mediconnect.mediconnectapi.dto.response.HospitalResponse;
import com.mediconnect.mediconnectapi.entity.Hospital;
import com.mediconnect.mediconnectapi.repository.HospitalRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;


    public HospitalResponse createHospital(
            CreateHospitalRequest request
    ) {

        if (
                request.getEmail() != null
                        && !request.getEmail().isBlank()
                        && hospitalRepository.existsByEmail(request.getEmail())
        ) {
            throw new RuntimeException(
                    "A hospital with this email already exists"
            );
        }

        Hospital hospital = new Hospital();

        hospital.setName(request.getName());
        hospital.setLocation(request.getLocation());
        hospital.setAddress(request.getAddress());
        hospital.setPhone(request.getPhone());
        hospital.setEmail(request.getEmail());

        Hospital savedHospital =
                hospitalRepository.save(hospital);

        return mapToResponse(savedHospital);
    }


    public List<HospitalResponse> getAllHospitals() {

        return hospitalRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public HospitalResponse getHospitalById(
            UUID id
    ) {

        Hospital hospital =
                hospitalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hospital not found"
                                )
                        );

        return mapToResponse(hospital);
    }


    public HospitalResponse updateHospital(
            UUID id,
            UpdateHospitalRequest request
    ) {

        Hospital hospital =
                hospitalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hospital not found"
                                )
                        );


        /*
         * Only check the email when it has changed.
         * This prevents the hospital from conflicting
         * with its own existing email.
         */
        if (
                request.getEmail() != null
                        && !request.getEmail().isBlank()
                        && !request.getEmail().equalsIgnoreCase(
                        hospital.getEmail()
                )
                        && hospitalRepository.existsByEmail(
                        request.getEmail()
                )
        ) {
            throw new RuntimeException(
                    "A hospital with this email already exists"
            );
        }


        hospital.setName(request.getName());
        hospital.setLocation(request.getLocation());
        hospital.setAddress(request.getAddress());
        hospital.setPhone(request.getPhone());
        hospital.setEmail(request.getEmail());


        Hospital updatedHospital =
                hospitalRepository.save(hospital);

        return mapToResponse(updatedHospital);
    }


    public void deleteHospital(
            UUID id
    ) {

        Hospital hospital =
                hospitalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hospital not found"
                                )
                        );


        /*
         * Do not allow deletion when users are still
         * assigned to the hospital.
         *
         * This protects doctors, receptionists,
         * hospital administrators and other users
         * associated with this hospital.
         */
        long assignedUsers =
                userRepository.countByHospitalId(id);


        if (assignedUsers > 0) {
            throw new RuntimeException(
                    "This hospital cannot be deleted because "
                            + assignedUsers
                            + " user(s) are still assigned to it"
            );
        }


        hospitalRepository.delete(hospital);
    }


    private HospitalResponse mapToResponse(
            Hospital hospital
    ) {

        return new HospitalResponse(

                hospital.getId(),

                hospital.getName(),

                hospital.getLocation(),

                hospital.getAddress(),

                hospital.getPhone(),

                hospital.getEmail()

        );
    }
}