package com.restcourse.spaceship.repository;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.restcourse.spaceship.model.Group;
import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;

@Component
//@Profile("test")
public class StarWarsTestScenario {
	
	private ArrayList<Pilot> pilots = new ArrayList<Pilot>();
	private ArrayList<Spaceship> spaceships = new ArrayList<Spaceship>();

	public void initTestData() {
		Spaceship xwing1 = new Spaceship(1L, "X-WING 1", Group.REBELLION, true, true, 4);
		Spaceship xwing2 = new Spaceship(2L, "X-WING 2", Group.REBELLION, true, true, 4);
		Spaceship xwing3 = new Spaceship(3L, "X-WING 3", Group.REBELLION, true, false, 4);
		
		Spaceship tieFighter1 = new Spaceship(4L, "TIE FIGHTER 1", Group.REBELLION, true, true, 2);
		Spaceship tieFighter2 = new Spaceship(5L, "TIE FIGHTER 2", Group.REBELLION, true, false, 2);
		Spaceship tieFighter3 = new Spaceship(6L, "TIE FIGHTER 3", Group.REBELLION, true, true, 2);
		

		Pilot rebelPilot1 = new Pilot(1L, "Luke", "Skywalker", Group.EMPIRE);
		Pilot rebelPilot2 = new Pilot(2L, "Wedge", "Antilles", Group.EMPIRE);
		Pilot rebelPilot3 = new Pilot(3L, "Poe", "Dameron", Group.EMPIRE);

		Pilot empirePilot1 = new Pilot(4L, "Darth", "Vader", Group.EMPIRE);
		Pilot empirePilot2 = new Pilot(5L, "FN", "2198", Group.EMPIRE);
		Pilot empirePilot3 = new Pilot(6L, "FN", "2199", Group.EMPIRE);

		pilots.clear();
		spaceships.clear();
		
		pilots.addAll(Arrays.asList(rebelPilot1, rebelPilot2, rebelPilot3, empirePilot1, empirePilot2, empirePilot3));
		spaceships.addAll(Arrays.asList(xwing1, xwing2, xwing3, tieFighter1, tieFighter2, tieFighter3));
	}

	public ArrayList<Pilot> getPilots() {
		return pilots;
	}

	public ArrayList<Spaceship> getSpaceships() {
		return spaceships;
	}

}
