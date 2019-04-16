package com.restcourse.spaceship.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.restcourse.spaceship.exception.AttributesAreNotValidException;
import com.restcourse.spaceship.exception.GroupNotChangeableException;
import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.service.SpaceshipService;

@RestController
public class PilotController {
	
	private SpaceshipService service;

	@Autowired
	public PilotController(SpaceshipService service) {
		this.service = service;
	}
	
	@GetMapping("pilots")
	public ResponseEntity<List<Pilot>> getAllPilots(){
		 return service.getAllPilots()
		.map(pilots -> ResponseEntity.ok(pilots))
		.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("pilots/{id}")
	public ResponseEntity<Pilot> getOnePilot(@PathVariable(name = "id", required = true) Long id){
		return service.findPilot(id)
				.map(pilot -> ResponseEntity.ok(pilot))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("pilots/{id}")
	public ResponseEntity<Pilot> deleteOnePilot(@PathVariable(name = "id", required = true) Long id){
		service.deletePilot(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("pilots/{id}")
	public ResponseEntity<Pilot> updateOnePilot(@PathVariable(name = "id", required = true) Long id, @Valid @RequestBody Pilot pilot) throws GroupNotChangeableException{
		return service.createOrUpdatePilot(id, pilot)
				.map(p -> ResponseEntity.ok(p))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PatchMapping("pilots/{id}")
	public ResponseEntity<Pilot> patchOnePilot(@PathVariable(name = "id", required = true) Long id, @RequestBody Map<String, Object> pilotAttributes) throws GroupNotChangeableException, AttributesAreNotValidException{
		return service.patchPilot(id, pilotAttributes)
				.map(p -> ResponseEntity.ok(p))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping("pilots")
	public ResponseEntity<Pilot> createOnePilot(@Valid @RequestBody Pilot pilot) throws GroupNotChangeableException{
		return ResponseEntity.created(URI.create("")).body(service.createPilot(pilot));
	}
	
}
