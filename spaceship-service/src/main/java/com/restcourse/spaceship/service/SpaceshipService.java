package com.restcourse.spaceship.service;

import java.util.List;
import java.util.Optional;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public Optional<Spaceship> findSpaceship(Long id) {
		return repository.findSpaceship(id);
	}

	public Optional<List<Spaceship>> getAllSpaceships(Boolean readyToFly) {
		Optional<List<Spaceship>>  result = Optional.empty();
		if(readyToFly == null) {
			result =  getAllSpaceships();
		}else if(readyToFly) {
			result = repository.findReadyToFlySpaceships();			
		}else {
			result = repository.findNotReadyToFlySpaceships();
		}
		return result;
	}
	

	public Optional<List<Spaceship>> getAllSpaceships() {
		return repository.getAllSpaceships();
	}

	public Spaceship createSpaceship(Spaceship spaceship) {
		Spaceship createdSpaceship = repository.createSpaceship(spaceship);
		log.debug("Spaceship created with ID: {}", createdSpaceship.getId());
		return createdSpaceship;
	}

	public void deleteSpaceship(Long id) {
		repository.deleteSpaceship(id);
	}

	public Optional<Spaceship> patchSpaceship(Long id, Spaceship spaceship) throws GroupNotChangeableException{
		return createOrUpdateSpaceship(id, spaceship, false);
	}
	
	public Optional<Spaceship> createOrUpdateSpaceship(Long id, Spaceship spaceship) throws GroupNotChangeableException{
		return createOrUpdateSpaceship(id, spaceship, true);
	}
	
	private Optional<Spaceship> createOrUpdateSpaceship(Long id, Spaceship spaceship, boolean overrideAttribtuesWithNull)
			throws GroupNotChangeableException {
 		Optional<Spaceship> existingSpaceship = findSpaceship(id);
		if (existingSpaceship.isPresent()) {
			validator.validate(existingSpaceship.get(), spaceship);
			mapObject(id, spaceship, existingSpaceship.get(), overrideAttribtuesWithNull);
			return Optional.of(existingSpaceship.get());
		} else {
			spaceship.setId(id);
			repository.add(spaceship);
		}
		return Optional.empty();
	}

	public Pilot createPilot(Pilot pilot) {
		return repository.createPilot(pilot);
	}

	public Optional<List<Pilot>> getAllPilots() {
		return repository.getAllPilots();
	}

	public Optional<Pilot> findPilot(Long id) {
		return repository.findPilot(id);
	}

	public void deletePilot(Long id) {
		repository.deletePilot(id);
	}

	public Optional<Pilot> createOrUpdatePilot(Long id, Pilot pilot, boolean overrideAttribtuesWithNull)
			throws GroupNotChangeableException {
		Optional<Pilot> exisitingPilot = findPilot(id);
		if (exisitingPilot.isPresent()) {
			validator.validate(exisitingPilot.get(), pilot);
			mapObject(id, pilot, exisitingPilot.get(), overrideAttribtuesWithNull);
			exisitingPilot.get().setId(pilot.getId());
		}
		return Optional.of(exisitingPilot.get());
	}

	public Optional<Spaceship> updatePilotSpaceshipRelation(Long spaceshipId, Long pilotId) {
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

	public Optional<Pilot> patchPilot(Long id, Pilot pilot) throws GroupNotChangeableException {
		Pilot result = null;
		Optional<Pilot> existingPilot = findPilot(id);
		if (existingPilot.isPresent()) {
			mapObject(id, pilot, existingPilot.get(), false);
			validator.validate(existingPilot.get(), pilot);
			result = existingPilot.get();
		}
		return Optional.ofNullable(result);
	}

	private void mapObject(Long id, Object sourceObject, Object targetObject, boolean overrideAttribtuesWithNull) {
		ModelMapper modelMapper = new ModelMapper();
		if (!overrideAttribtuesWithNull) {
			modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
		}
		modelMapper.map(sourceObject, targetObject);
	}

}
