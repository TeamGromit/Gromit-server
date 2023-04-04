package com.example.gromit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class GromitApplication {

	public static void main(String[] args) {
		SpringApplication.run(GromitApplication.class, args);

	}

}
