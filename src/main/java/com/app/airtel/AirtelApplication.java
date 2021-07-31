package com.app.airtel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class AirtelApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirtelApplication.class, args);
	}

}
