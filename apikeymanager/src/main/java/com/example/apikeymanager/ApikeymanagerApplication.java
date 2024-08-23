package com.example.apikeymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApikeymanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApikeymanagerApplication.class, args);
	}

}
