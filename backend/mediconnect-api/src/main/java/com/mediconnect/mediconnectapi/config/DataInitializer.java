package com.mediconnect.mediconnectapi.config;

import com.mediconnect.mediconnectapi.entity.Role;
import com.mediconnect.mediconnectapi.entity.User;
import com.mediconnect.mediconnectapi.repository.RoleRepository;
import com.mediconnect.mediconnectapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        createRoles();
        createSuperAdmin();

    }

    private void createRoles() {

        createRoleIfMissing("PATIENT");
        createRoleIfMissing("DOCTOR");
        createRoleIfMissing("RECEPTIONIST");
        createRoleIfMissing("HOSPITAL_ADMIN");
        createRoleIfMissing("SUPER_ADMIN");

    }

    private void createRoleIfMissing(String roleName) {

        if (roleRepository.findByName(roleName).isEmpty()) {

            Role role = new Role();
            role.setName(roleName);

            roleRepository.save(role);

            System.out.println("ROLE CREATED: " + roleName);
        }
    }

    private void createSuperAdmin() {

        String email = "admin@mediconnect.com";

        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }

        Role adminRole = roleRepository.findByName("SUPER_ADMIN")
                .orElseThrow(() ->
                        new RuntimeException("SUPER_ADMIN role could not be created")
                );

        User admin = new User();

        admin.setEmail(email);

        admin.setPassword(
                passwordEncoder.encode("Admin@123")
        );

        admin.setRole(adminRole);

        userRepository.save(admin);

        System.out.println("SUPER ADMIN CREATED");
    }
}

