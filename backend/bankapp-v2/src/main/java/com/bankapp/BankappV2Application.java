package com.bankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BankappV2Application {

	public static void main(String[] args) {
		SpringApplication.run(BankappV2Application.class, args);
		
		String rawPassword = "pass123";
		String hashed = new BCryptPasswordEncoder().encode(rawPassword);
		System.out.println(hashed);

	}
}
