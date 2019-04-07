package com.restcourse.spaceship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.restcourse.spaceship.repository.SpaceDataRepository;
import com.restcourse.spaceship.repository.StarWarsTestScenario;

@SpringBootApplication
public class SpaceshipServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpaceshipServiceApplication.class, args);
	}
	
	@Bean
//	@Profile("test")
	public SpaceDataRepository createRepository(StarWarsTestScenario starWarsTestSenario) {
		starWarsTestSenario.initTestData();
		return new SpaceDataRepository(starWarsTestSenario.getPilots(), starWarsTestSenario.getSpaceships());
	}

}
