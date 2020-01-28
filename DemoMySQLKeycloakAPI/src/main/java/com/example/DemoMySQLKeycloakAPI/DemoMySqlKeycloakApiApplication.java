package com.example.DemoMySQLKeycloakAPI;

import com.example.DemoMySQLKeycloakAPI.model.User;
import com.example.DemoMySQLKeycloakAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootApplication
public class DemoMySqlKeycloakApiApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoMySqlKeycloakApiApplication.class, args);
	}

	@Override
	public void run(String... args) {
		this.userRepository.deleteAll();
		this.userRepository.save(new User()
				.setUsername("dw")
				.setEmail("dorian.weidler@novatec-gmbh.de")
				.setFirstName("Dorian")
				.setLastName("Weidler"));
		this.userRepository.save(new User().setUsername("afa").setEmail("andreas.falk@novatec-gmbh.de"));

		IntStream.range(1, 100).forEach(i -> this.userRepository.save(new User().setUsername(UUID.randomUUID().toString())));
	}
}
