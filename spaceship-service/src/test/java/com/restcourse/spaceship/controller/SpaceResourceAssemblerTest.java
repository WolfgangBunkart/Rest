package com.restcourse.spaceship.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;
import com.restcourse.spaceship.repository.StarWarsTestScenario;

public class SpaceResourceAssemblerTest {
	
	private StarWarsTestScenario testScenario = new StarWarsTestScenario();
	
	
	@Before 
	public void setup() {
		testScenario.initTestData();
	}
	
	
	@Test
	public void testCreateRootResource() {
		SpaceResourceAssembler assembler = new SpaceResourceAssembler();
		
		Resource<String> resource = assembler.buildRootResouce("spaceship-service");
		
		assertThat(resource.getLinks()).contains(new Link("/spaceships?readyToFly=true", "spaceships"));
		assertThat(resource.getLinks()).contains(new Link("/pilots", "pilots"));
		
	}

	@Test
	public void testCreateSpaceshipResource() {
		SpaceResourceAssembler assembler = new SpaceResourceAssembler();
		
		Spaceship spaceship = testScenario.getSpaceships().get(0);
		
		Resource<Spaceship> resource = assembler.buildSpaceshipResouce(spaceship);
		
		assertThat(resource.getLinks()).contains(new Link("/spaceships?readyToFly=true", "allReadyToFlySpaceships"));
		assertThat(resource.getLinks()).contains(new Link("/spaceships?readyToFly=false", "allNotReadyToFlySpaceships"));
	}
	
	@Test
	public void testCreatePilotResource() {
		SpaceResourceAssembler assembler = new SpaceResourceAssembler();
		
		Pilot pilot = testScenario.getPilots().get(0);
		
		Resource<Pilot> resource = assembler.buildPilotResouce(pilot);
		
		assertThat(resource.getLinks()).contains(new Link("/pilots", "pilots"));
	}
	
}
