package com.mediconnect.mediconnectapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MediconnectApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediconnectApiApplication.class, args);
	}


	@Bean
	CommandLineRunner checkMailEnvironment() {
		return args -> {
			String username = System.getenv("MEDICONNECT_MAIL_USERNAME");
			String password = System.getenv("MEDICONNECT_MAIL_PASSWORD");

			System.out.println("MAIL USERNAME: " + username);
			System.out.println(
					"MAIL PASSWORD EXISTS: "
							+ (password != null && !password.isBlank())
			);
			System.out.println(
					"MAIL PASSWORD LENGTH: "
							+ (password == null ? 0 : password.length())
			);
		};
	}

}

