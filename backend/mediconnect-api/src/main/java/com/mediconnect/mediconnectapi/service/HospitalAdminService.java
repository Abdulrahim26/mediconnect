package com.mediconnect.mediconnectapi.service;

import com.mediconnect.mediconnectapi.dto.request.CreateHospitalAdminRequest;
import com.mediconnect.mediconnectapi.dto.request.UpdateHospitalAdminRequest;
import com.mediconnect.mediconnectapi.dto.response.HospitalAdminDetailsResponse;
import com.mediconnect.mediconnectapi.dto.response.HospitalAdminResponse;
import com.mediconnect.mediconnectapi.dto.response.UserResponse;
import com.mediconnect.mediconnectapi.entity.Hospital;
import com.mediconnect.mediconnectapi.entity.Role;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.repository.HospitalRepository;
import com.mediconnect.mediconnectapi.repository.RoleRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HospitalAdminService {

    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createHospitalAdmin(
            CreateHospitalAdminRequest request
    ) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() ->
                        new RuntimeException("Hospital not found")
                );

        Role adminRole = roleRepository.findByName("HOSPITAL_ADMIN")
                .orElseThrow(() ->
                        new RuntimeException("HOSPITAL_ADMIN role not found")
                );

        User user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(adminRole);
        user.setHospital(hospital);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole().getName()
        );
    }

    public List<HospitalAdminResponse> getAllHospitalAdmins() {

        List<Object[]> results =
                userRepository.findHospitalAdminsWithHospital(
                        "HOSPITAL_ADMIN"
                );

        return results.stream()
                .map(row -> new HospitalAdminResponse(
                        (UUID) row[0],
                        (String) row[1],
                        (String) row[2],
                        (UUID) row[3],
                        (String) row[4],
                        (Boolean) row[5]  // active status
                ))
                .toList();
    }

    public HospitalAdminDetailsResponse getHospitalAdminDetails(
            UUID userId
    ) {

        List<Object[]> results =
                userRepository.findHospitalAdminDetails(userId);

        if (results.isEmpty()) {
            throw new RuntimeException(
                    "Hospital administrator not found"
            );
        }

        Object[] result = results.get(0);

        return new HospitalAdminDetailsResponse(
                (UUID) result[0],
                (String) result[1],
                (String) result[2],
                (java.time.LocalDateTime) result[3],
                (UUID) result[4],
                (String) result[5],
                (String) result[6],
                (String) result[7],
                (String) result[8],
                (String) result[9],
                (Boolean) result[10]  // active status
        );
    }

    public HospitalAdminDetailsResponse updateHospitalAdmin(
            UUID userId,
            UpdateHospitalAdminRequest request
    ) {

        User admin = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Hospital administrator not found"
                        )
                );

        if (!admin.getRole().getName().equals("HOSPITAL_ADMIN")) {
            throw new RuntimeException(
                    "User is not a hospital administrator"
            );
        }

        userRepository.findByEmail(request.getEmail().trim())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(userId)) {
                        throw new RuntimeException(
                                "Email already exists"
                        );
                    }
                });

        Hospital hospital = hospitalRepository.findById(
                request.getHospitalId()
        ).orElseThrow(() ->
                new RuntimeException("Hospital not found")
        );

        admin.setEmail(request.getEmail().trim());
        admin.setHospital(hospital);

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            if (request.getPassword().length() < 6) {
                throw new RuntimeException(
                        "Password must be at least 6 characters long"
                );
            }

            admin.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
        }

        userRepository.save(admin);

        return getHospitalAdminDetails(userId);
    }

    public void activateHospitalAdmin(UUID userId) {

        User admin = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Hospital administrator not found")
                );

        if (!admin.getRole().getName().equals("HOSPITAL_ADMIN")) {
            throw new RuntimeException(
                    "User is not a hospital administrator"
            );
        }

        if (admin.isActive()) {
            throw new RuntimeException(
                    "Hospital administrator is already active"
            );
        }

        admin.setActive(true);
        userRepository.save(admin);
    }

    public void deactivateHospitalAdmin(UUID userId) {

        User admin = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Hospital administrator not found")
                );

        if (!admin.getRole().getName().equals("HOSPITAL_ADMIN")) {
            throw new RuntimeException(
                    "User is not a hospital administrator"
            );
        }

        if (!admin.isActive()) {
            throw new RuntimeException(
                    "Hospital administrator is already inactive"
            );
        }

        admin.setActive(false);
        userRepository.save(admin);
    }
}