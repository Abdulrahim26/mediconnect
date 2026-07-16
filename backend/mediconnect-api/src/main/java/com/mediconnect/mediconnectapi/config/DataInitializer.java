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


        createSuperAdmin();


    }




    private void createSuperAdmin(){


        String email =
                "admin@mediconnect.com";


        if(userRepository.findByEmail(email).isPresent()){

            return;

        }



        Role adminRole =
                roleRepository.findByName("SUPER_ADMIN")
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "SUPER_ADMIN role missing"
                                )
                        );



        User admin = new User();


        admin.setEmail(email);


        admin.setPassword(
                passwordEncoder.encode(
                        "Admin@123"
                )
        );


        admin.setRole(adminRole);



        userRepository.save(admin);



        System.out.println(
                "SUPER ADMIN CREATED"
        );

    }

}