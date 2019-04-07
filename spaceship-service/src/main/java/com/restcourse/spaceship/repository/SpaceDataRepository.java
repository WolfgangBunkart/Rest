package com.restcourse.spaceship.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;
import com.restcourse.spaceship.service.ServiceUtils;

//@Component
public class SpaceDataRepository {

	private List<Spaceship> spaceships;
	private List<Pilot> pilots;
	private ModelMapper modelMapper = new ModelMapper();

//	@Autowired
	public SpaceDataRepository(ArrayList<Pilot> pilots, ArrayList<Spaceship> spaceships) {
		this.pilots = pilots;
		this.spaceships = spaceships;
	}

	public Spaceship createSpaceship(Long id, Spaceship spaceship) {
		Objects.requireNonNull(spaceship);
		if(id == null) {
			spaceship.setId(Long.valueOf(spaceships.size() + 1));
		}else {
			spaceship.setId(id);
		}
		spaceships.add(spaceship);
		return spaceship;
	}

	public Optional<Spaceship> findSpaceship(Long id) {
		Objects.requireNonNull(id);
		return spaceships.stream().filter(p -> p.getId().equals(id)).findFirst();
	}

	public Optional<List<Spaceship>> getAllSpaceships() {
		return Optional.ofNullable(spaceships);
	}

	public Pilot createPilot(Pilot pilot, Long id) {
		Objects.requireNonNull(pilot);
		if(id == null) {			
			pilot.setId(Long.valueOf(pilots.size() + 1));
		}else {
			pilot.setId(id);
		}
		pilots.add(pilot);
		return pilot;
	}

	public Optional<List<Pilot>> getAllPilots() {
		return Optional.of(pilots);
	}

	public Optional<Pilot> findPilot(Long id) {
		Objects.requireNonNull(id);
		return pilots.stream().filter(p -> p.getId().equals(id)).findFirst();
	}

	public void deletePilot(Long id) {
		Objects.requireNonNull(id);
		Optional<Pilot> existingPilot = pilots.stream().filter(p -> p.getId().equals(id)).findFirst();
		if (existingPilot.isPresent()) {
			pilots.remove(pilots.indexOf(existingPilot.get()));
		}
	}

	public Optional<Pilot> update(Long id, Map<String, Object> pilotAttributesToUpdate, Pilot existingPilot) {
		Objects.requireNonNull(id);
		Objects.requireNonNull(existingPilot);
		Objects.requireNonNull(pilotAttributesToUpdate);
		ServiceUtils.fillObjectWithAttributes(pilotAttributesToUpdate, existingPilot);
		return Optional.ofNullable(existingPilot);
	}

	public Optional<Pilot> createOrUpdate(Long id, Pilot pilot) {
		Objects.requireNonNull(id);
		Objects.requireNonNull(pilot);
		Optional<Pilot> existingPilot = findPilot(id);
		if(existingPilot.isPresent()) {			
			modelMapper.map(pilot, existingPilot.get());
			existingPilot.get().setId(id);
			return existingPilot;
		}else {
			Pilot createdPilot = createPilot(pilot, id);
			return Optional.ofNullable(createdPilot);
		}
	}

	public void deleteSpaceship(Long id) {
		Objects.requireNonNull(id);
		Optional<Spaceship> spaceshipToDelete = spaceships.stream().filter(s -> s.getId().equals(id)).findFirst();
		if (spaceshipToDelete.isPresent()) {
			spaceships.remove(spaceshipToDelete.get());
		}
	}

	public Optional<List<Spaceship>> findReadyToFlySpaceships() {
		return Optional.ofNullable(spaceships.stream().filter(s -> s.isReadyToFly()).collect(Collectors.toList()));
	}

	public Optional<List<Spaceship>> findNotReadyToFlySpaceships() {
		return Optional.ofNullable(spaceships.stream().filter(s -> !s.isReadyToFly()).collect(Collectors.toList()));
	}

	public Optional<Spaceship> update(Long id, Map<String, Object> spaceshipAttributesToUpdate, Spaceship existingSpaceship) {
		Objects.requireNonNull(id);
		Objects.requireNonNull(existingSpaceship);
		Objects.requireNonNull(spaceshipAttributesToUpdate);
		ServiceUtils.fillObjectWithAttributes(spaceshipAttributesToUpdate, existingSpaceship);
		return Optional.ofNullable(existingSpaceship);
	}

	public Optional<Spaceship> createOrUpdate(Long id, Spaceship spaceship) {
		Objects.requireNonNull(id);
		Objects.requireNonNull(spaceship);
		Optional<Spaceship> existingSpaceship = findSpaceship(id);
		if(existingSpaceship.isPresent()) {
			modelMapper.map(spaceship, existingSpaceship.get());
			return existingSpaceship;
		}else {
			return Optional.ofNullable(createSpaceship(id, spaceship));
		}
	}

}
