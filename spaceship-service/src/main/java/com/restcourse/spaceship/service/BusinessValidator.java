package com.restcourse.spaceship.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.restcourse.spaceship.exception.AttributesAreNotValidException;
import com.restcourse.spaceship.exception.GroupNotChangeableException;
import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;

@Component
public class BusinessValidator {
	
	private static final Logger log = LoggerFactory.getLogger(BusinessValidator.class);
	
	public void validate(Spaceship existingspaceship, Spaceship spaceship) throws GroupNotChangeableException{
		if(!existingspaceship.getGroup().equals(spaceship.getGroup())) {
			String message = "Not allowed to update the group from " + existingspaceship.getGroup() + " to " + spaceship.getGroup();
			log.error(message);
			throw new GroupNotChangeableException(message);
		}		
	}
	
	public void validate(Pilot existingPilot, Pilot pilot) throws GroupNotChangeableException {
		if(pilot.getGroup() == null) {
			String message = "Not allowed to update the group from " + existingPilot.getGroup() + " to null";
			throw new GroupNotChangeableException(message);
		}
		if(pilot.getGroup() != null && !existingPilot.getGroup().equals(pilot.getGroup())) {
			String message = "Not allowed to update the group from " + existingPilot.getGroup() + " to " + pilot.getGroup();
			log.error(message);
			throw new GroupNotChangeableException(message);
		}		
	}

	public void validate(Spaceship existingspaceship, Map<String, Object> spaceshipAttributes) throws AttributesAreNotValidException, GroupNotChangeableException {
		Spaceship spaceshipFromRequest = ServiceUtils.createObjectFromAttributes(spaceshipAttributes, Spaceship.class);
		validate(spaceshipFromRequest, existingspaceship);
	}

	public void validate(Pilot existingPilot, Map<String, Object> pilotAttributes) throws AttributesAreNotValidException, GroupNotChangeableException {
		Pilot pilotFromRequest = ServiceUtils.createObjectFromAttributes(pilotAttributes, Pilot.class);
		validate(pilotFromRequest, existingPilot);
	}

}
