package com.example.gromit;

import com.example.gromit.repository.UserAccountRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
public class GromitApplication {

	public static void main(String[] args) {
		SpringApplication.run(GromitApplication.class, args);

	}

}
