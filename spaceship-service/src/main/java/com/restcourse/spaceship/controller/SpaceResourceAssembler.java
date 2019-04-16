package com.restcourse.spaceship.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;

@Component
public class SpaceResourceAssembler {

	public Resource<String> buildRootResouce(String serviceName) {
		
		Link allPilotsLink = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PilotController.class)//
				.getAllPilots()).withRel("pilots");
		Link allSpaceshipsLink = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(SpaceshipController.class)//
				.getAllSpaceships(true)).withRel("spaceships");
		return new Resource<String>(serviceName, allPilotsLink, allSpaceshipsLink);
	}

	public Resource<Spaceship> buildSpaceshipResouce(Spaceship spaceship) {
		Link selfLink = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(SpaceshipController.class)//
				.getSpaceship(spaceship.getId())).withSelfRel();
		
		Link allReadyToFlySpaceships = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(SpaceshipController.class)//
				.getAllSpaceships(true)).withRel("allReadyToFlySpaceships");

		Link allNotReadyToFlySpaceships = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(SpaceshipController.class)//
				.getAllSpaceships(false)).withRel("allNotReadyToFlySpaceships");
		
		return new Resource<Spaceship>(spaceship, selfLink, allReadyToFlySpaceships, allNotReadyToFlySpaceships);
	}

	public Resources<Resource<Spaceship>> buildSpaceshipResouces(List<Spaceship> spaceshipList) {
		Link selfLink = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(SpaceshipController.class)//
				.getAllSpaceships(true)).withSelfRel();
		List<Resource<Spaceship>> res = new ArrayList<>();
		spaceshipList.forEach(s -> res.add(buildSpaceshipResouce(s)));
		return new Resources<Resource<Spaceship>>(res, selfLink);
	}

	public Resource<Pilot> buildPilotResouce(Pilot pilot) {
		Link selfLink = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PilotController.class)//
				.getOnePilot(pilot.getId())).withSelfRel();

		Link allPilotsLink = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PilotController.class)//
				.getAllPilots()).withRel("pilots");
		
		return new Resource<Pilot>(pilot, selfLink, allPilotsLink);
	}

	

}
