package ru.orekhov.SpringSecurity.security_app;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SecurityAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityAppApplication.class, args);
	}

	@Bean
	ModelMapper modelMapper(){// создали модел мапер что бы внедрять зависмость через бин
		return new ModelMapper();
	}

}
