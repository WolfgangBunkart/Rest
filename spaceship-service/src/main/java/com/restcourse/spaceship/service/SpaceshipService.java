package com.restcourse.spaceship.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restcourse.spaceship.exception.AttributesAreNotValidException;
import com.restcourse.spaceship.exception.GroupNotChangeableException;
import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;
import com.restcourse.spaceship.repository.SpaceDataRepository;

@Service
public class SpaceshipService {

	private static final Logger log = LoggerFactory.getLogger(SpaceshipService.class);

	private SpaceDataRepository repository;

	private BusinessValidator validator;

	@Autowired
	public SpaceshipService(SpaceDataRepository respository, BusinessValidator validator) {
		this.repository = respository;
		this.validator = validator;
	}

	public Spaceship createSpaceship(Spaceship spaceship) {
		Objects.requireNonNull(spaceship, "spaceship is not set");
		Spaceship createdSpaceship = repository.createSpaceship(null, spaceship);
		log.debug("Spaceship created with ID: {}", createdSpaceship.getId());
		return createdSpaceship;
	}

	public Optional<Spaceship> findSpaceship(Long id) {
		Objects.requireNonNull(id, "id is not set");
		return repository.findSpaceship(id);
	}

	public Optional<List<Spaceship>> getAllSpaceships() {
		return repository.getAllSpaceships();
	}

	public Optional<List<Spaceship>> getAllSpaceships(boolean readyToFly) {
		if (readyToFly) {
			return repository.findReadyToFlySpaceships();
		}
		return repository.findNotReadyToFlySpaceships();
	}

	public void deleteSpaceship(Long id) {
		repository.deleteSpaceship(id);
	}

	public Optional<Spaceship> patchSpaceship(Long id, Map<String, Object> spaceshipAttributes)
			throws GroupNotChangeableException, AttributesAreNotValidException {
		Objects.requireNonNull(spaceshipAttributes, "Attributes not set");
		Objects.requireNonNull(id, "id is not set");
		Optional<Spaceship> exisitingSpaceship = findSpaceship(id);
		log.debug("Patching Spaceship with ID: {}", id);
		if (exisitingSpaceship.isPresent()) {
			validator.validate(exisitingSpaceship.get(), spaceshipAttributes);
		}
		return repository.update(id, spaceshipAttributes, exisitingSpaceship.get());
	}

	public Optional<Spaceship> createOrUpdateSpaceship(Long id, Spaceship spaceship)
			throws GroupNotChangeableException {
		Objects.requireNonNull(spaceship, "spaceship is not set");
		Objects.requireNonNull(id, "id is not set");
		log.debug("Creating or Updating Spaceship with ID: {}", id);
		Optional<Spaceship> existingSpaceship = findSpaceship(id);
		if (existingSpaceship.isPresent()) {
			validator.validate(existingSpaceship.get(), spaceship);
		}
		return repository.createOrUpdate(id, spaceship);
	}

	public Pilot createPilot(Pilot pilot) {
		Objects.requireNonNull(pilot, "pilot is not set");
		return repository.createPilot(pilot, null);
	}

	public Optional<List<Pilot>> getAllPilots() {
		return repository.getAllPilots();
	}

	public Optional<Pilot> findPilot(Long id) {
		Objects.requireNonNull(id, "id is not set");
		return repository.findPilot(id);
	}

	public void deletePilot(Long id) {
		Objects.requireNonNull(id, "id is not set");
		repository.deletePilot(id);
	}

	public Optional<Pilot> createOrUpdatePilot(Long id, Pilot pilot)
			throws GroupNotChangeableException {
		Objects.requireNonNull(id, "id is not set");
		Objects.requireNonNull(pilot, "pilot is not set");
		Optional<Pilot> exisitingPilot = findPilot(id);
		if (exisitingPilot.isPresent()) {
			validator.validate(exisitingPilot.get(), pilot);
		}
		return repository.createOrUpdate(id, pilot);
	}

	public Optional<Spaceship> updatePilotSpaceshipRelation(Long spaceshipId, Long pilotId) {
		Objects.requireNonNull(pilotId, "pilotId is not set");
		Objects.requireNonNull(spaceshipId, "spaceshipId is not set");
		Optional<Spaceship> spaceship = findSpaceship(spaceshipId);
		Optional<Pilot> pilot = findPilot(pilotId);
		if (spaceship.isPresent() && pilot.isPresent()) {
			if (!spaceship.get().getPilots().contains(pilot.get())) {
				spaceship.get().addPilot(pilot.get());
			}
		}
		return spaceship;
	}

	public Optional<Spaceship> deletePilotSpaceshipRelation(Long spaceshipId, Long pilotId) {
		Objects.requireNonNull(pilotId, "pilotId is not set");
		Objects.requireNonNull(spaceshipId, "spaceshipId is not set");
		Optional<Spaceship> spaceship = findSpaceship(spaceshipId);
		if (spaceship.isPresent()) {
			Optional<Pilot> foundPilot = spaceship.get().getPilots().stream()//
					.filter(p -> p.getId().equals(pilotId)).findFirst();
			if (foundPilot.isPresent()) {
				spaceship.get().removePilot(foundPilot.get());
			}
		}
		return spaceship;
	}

	public Optional<Pilot> patchPilot(Long id, Map<String, Object> pilotAttributes)
			throws GroupNotChangeableException, AttributesAreNotValidException {
		Optional<Pilot> existingPilot = findPilot(id);
		if (existingPilot.isPresent()) {
			validator.validate(existingPilot.get(), pilotAttributes);
			ServiceUtils.fillObjectWithAttributes(pilotAttributes, existingPilot.get());
		}
		return existingPilot;
	}
}
