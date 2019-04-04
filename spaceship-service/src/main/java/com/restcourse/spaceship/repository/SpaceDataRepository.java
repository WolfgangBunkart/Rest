package com.restcourse.spaceship.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;

//@Component
public class SpaceDataRepository {
	
	private List<Spaceship> spaceships;
	private List<Pilot> pilots;
	
//	@Autowired
	public SpaceDataRepository(ArrayList<Pilot> pilots, ArrayList<Spaceship> spaceships) {
		this.pilots = pilots;
		this.spaceships = spaceships;
	}
	
	public Spaceship createSpaceship(Spaceship spaceship) {
		spaceship.setId(Long.valueOf(spaceships.size() + 1));
		spaceships.add(spaceship);
		return spaceship;
	}

	public Optional<Spaceship> findSpaceship(Long id) {
		return spaceships.stream().filter(p -> p.getId().equals(id)).findFirst();
	}
	
	public Optional<List<Spaceship>> getAllSpaceships() {
		return Optional.ofNullable(spaceships);
	}
	
	public Pilot createPilot(Pilot pilot){
		pilot.setId(Long.valueOf(pilots.size() + 1 ));
		pilots.add(pilot);
		return pilot;
	}
	
	public Optional<List<Pilot>> getAllPilots() {
		return Optional.of(pilots);
	}

	public Optional<Pilot> findPilot(Long id) {
		return pilots.stream().filter(p -> p.getId().equals(id)).findFirst();
	}

	public void deletePilot(Long id) {
		Optional<Pilot> existingPilot = pilots.stream().filter(p -> p.getId().equals(id)).findFirst();
		if(existingPilot.isPresent()) {
			pilots.remove(pilots.indexOf(existingPilot.get()));
		}
	}
	
	public void deleteSpaceship(Long id) {
		Optional<Spaceship> spaceshipToDelete = spaceships.stream().filter(s -> s.getId().equals(id)).findFirst();
		if(spaceshipToDelete.isPresent()) {
			spaceships.remove(spaceshipToDelete.get());
		}
	}

	public void add(Spaceship spaceship) {
		spaceships.add(spaceship);
	}

	public Optional<List<Spaceship>> findReadyToFlySpaceships() {
		return Optional.of(spaceships.stream().filter(s -> s.isReadyToFly()).collect(Collectors.toList()));
	}

	public Optional<List<Spaceship>> findNotReadyToFlySpaceships() {
		return Optional.of(spaceships.stream().filter(s -> !s.isReadyToFly()).collect(Collectors.toList()));
	}

}
