package com.mediconnect.mediconnectapi.service.impl;

import com.mediconnect.mediconnectapi.dto.request.CreateReceptionistRequest;
import com.mediconnect.mediconnectapi.dto.response.ReceptionistResponse;
import com.mediconnect.mediconnectapi.entity.Hospital;
import com.mediconnect.mediconnectapi.entity.Receptionist;
import com.mediconnect.mediconnectapi.entity.Role;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.exception.BadRequestException;
import com.mediconnect.mediconnectapi.exception.ResourceNotFoundException;
import com.mediconnect.mediconnectapi.repository.HospitalRepository;
import com.mediconnect.mediconnectapi.repository.ReceptionistRepository;
import com.mediconnect.mediconnectapi.repository.RoleRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import com.mediconnect.mediconnectapi.service.ReceptionistService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceptionistServiceImpl
        implements ReceptionistService {

    private final ReceptionistRepository receptionistRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final HospitalRepository hospitalRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ReceptionistResponse createReceptionist(
            CreateReceptionistRequest request
    ) {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User admin =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        if(admin.getHospital() == null){

            throw new BadRequestException(
                    "Hospital admin is not assigned to any hospital."
            );

        }

        if(userRepository.findByEmail(request.getEmail()).isPresent()){

            throw new BadRequestException(
                    "Email already exists."
            );

        }

        Hospital hospital =
                hospitalRepository.findById(
                        admin.getHospital().getId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Hospital not found"
                        )
                );

        Role receptionistRole =
                roleRepository.findByName("RECEPTIONIST")
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "RECEPTIONIST role not found"
                                )
                        );

        User receptionistUser = new User();

        receptionistUser.setEmail(
                request.getEmail()
        );

        receptionistUser.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        receptionistUser.setRole(
                receptionistRole
        );

        receptionistUser.setHospital(
                hospital
        );

        receptionistUser =
                userRepository.save(
                        receptionistUser
                );

        Receptionist receptionist =
                new Receptionist();

        receptionist.setFirstName(
                request.getFirstName()
        );

        receptionist.setLastName(
                request.getLastName()
        );

        receptionist.setPhone(
                request.getPhone()
        );

        receptionist.setHospital(
                hospital
        );

        receptionist.setUser(
                receptionistUser
        );

        receptionist =
                receptionistRepository.save(
                        receptionist
                );

        return mapToResponse(receptionist);
    }

    @Override
    public List<ReceptionistResponse> getMyHospitalReceptionists() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User admin =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        if(admin.getHospital() == null){

            throw new BadRequestException(
                    "Hospital admin is not assigned to any hospital."
            );

        }

        return receptionistRepository
                .findByHospitalId(
                        admin.getHospital().getId()
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ReceptionistResponse mapToResponse(
            Receptionist receptionist
    ){

        return new ReceptionistResponse(

                receptionist.getId(),

                receptionist.getFirstName(),

                receptionist.getLastName(),

                receptionist.getUser().getEmail(),

                receptionist.getPhone(),

                receptionist.getHospital().getName()

        );
    }
}