package com.biteful.mealplanner.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
        //SpringApplication.run(UserServiceApplication.class, args);
        SpringApplication app = new SpringApplication(UserServiceApplication.class);
        app.setAdditionalProfiles("test");
        app.run(args);
	}

}
