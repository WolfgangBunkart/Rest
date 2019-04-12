package com.restcourse.spaceship.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.restcourse.spaceship.exception.AttributesAreNotValidException;
import com.restcourse.spaceship.exception.GroupNotChangeableException;
import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;

public interface SpaceshipService {

	Spaceship createSpaceship(Spaceship spaceship);

	Optional<Spaceship> findSpaceship(Long id);

	Optional<List<Spaceship>> getAllSpaceships();

	Optional<List<Spaceship>> getAllSpaceships(Boolean readyToFly);

	void deleteSpaceship(Long id);

	Optional<Spaceship> patchSpaceship(Long id, Map<String, Object> spaceshipAttributes)
			throws GroupNotChangeableException, AttributesAreNotValidException;

	Optional<Spaceship> createOrUpdateSpaceship(Long id, Spaceship spaceship) throws GroupNotChangeableException;

	Pilot createPilot(Pilot pilot);

	Optional<List<Pilot>> getAllPilots();

	Optional<Pilot> findPilot(Long id);

	void deletePilot(Long id);

	Optional<Pilot> createOrUpdatePilot(Long id, Pilot pilot) throws GroupNotChangeableException;

	Optional<Spaceship> updatePilotSpaceshipRelation(Long spaceshipId, Long pilotId);

	Optional<Spaceship> deletePilotSpaceshipRelation(Long spaceshipId, Long pilotId);

	Optional<Pilot> patchPilot(Long id, Map<String, Object> pilotAttributes)
			throws GroupNotChangeableException, AttributesAreNotValidException;

	Optional<Spaceship> updatePilotSpaceshipRelations(Long spaceshipId, List<Pilot> pilots);

	Optional<Spaceship> patchSpaceship(Long id, Spaceship spaceship) throws GroupNotChangeableException;

}