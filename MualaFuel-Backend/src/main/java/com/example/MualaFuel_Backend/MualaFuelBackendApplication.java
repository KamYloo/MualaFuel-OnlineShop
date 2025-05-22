package com.example.MualaFuel_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class MualaFuelBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MualaFuelBackendApplication.class, args);
	}

}
