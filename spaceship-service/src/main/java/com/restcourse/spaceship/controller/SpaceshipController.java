package com.restcourse.spaceship.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restcourse.spaceship.exception.AttributesAreNotValidException;
import com.restcourse.spaceship.exception.GroupNotChangeableException;
import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;
import com.restcourse.spaceship.service.SpaceshipService;

@RestController
public class SpaceshipController {

	private SpaceshipService spaceshipService;

	@Autowired
	public SpaceshipController(SpaceshipService spaceshipService) {
		this.spaceshipService = spaceshipService;
	}

	@GetMapping("spaceships")
	public ResponseEntity<List<Spaceship>> getAllSpaceships(
			@RequestParam(name = "readyToFly", required = false) Boolean readyToFly) {

		return spaceshipService.getAllSpaceships(readyToFly)//
				.map(spaceships -> ResponseEntity.ok(spaceships))//
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("spaceships/{id}")
	public ResponseEntity<Spaceship> getSpaceship(@PathVariable(name = "id", required = true) Long id) {
		return spaceshipService.findSpaceship(id)//
				.map(spaceship -> ResponseEntity.ok(spaceship))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping(path = "spaceships", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Spaceship> createSpaceship(@Valid @RequestBody Spaceship spaceship) {
		Spaceship createdSpaceship = spaceshipService.createSpaceship(spaceship);
		return ResponseEntity.created(URI.create("")).body(createdSpaceship);
	}

	@DeleteMapping("spaceships/{id}")
	public ResponseEntity<?> deleteSpaceship(@PathVariable(name = "id", required = true) Long id) {
		spaceshipService.deleteSpaceship(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("spaceships/{id}")
	public ResponseEntity<Spaceship> updateSpaceship(@PathVariable(name = "id", required = true) Long id, @Valid @RequestBody Spaceship spaceship) throws GroupNotChangeableException{
		return spaceshipService.createOrUpdateSpaceship(id, spaceship)
		.map(s -> ResponseEntity.ok(s))//
		.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PatchMapping("spaceships/{id}")
	public ResponseEntity<Spaceship> patchSpaceship(@PathVariable(name = "id", required = true) Long id, @RequestBody Map<String, Object> spaceshipAttributes) throws GroupNotChangeableException, AttributesAreNotValidException{
		return spaceshipService.patchSpaceship(id, spaceshipAttributes)
				.map(s -> ResponseEntity.ok(s))//
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PatchMapping("spaceships/{id}/pilots")
	public ResponseEntity<Spaceship> patchSpaceshipRelation(@PathVariable(name = "id", required = true) Long spaceshipId, @RequestBody List<Pilot> pilots) throws GroupNotChangeableException, AttributesAreNotValidException{
		return spaceshipService.updatePilotSpaceshipRelations(spaceshipId, pilots)
				.map(s -> ResponseEntity.ok(s))//
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	
}
