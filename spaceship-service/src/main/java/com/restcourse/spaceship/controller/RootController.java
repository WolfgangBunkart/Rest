package com.restcourse.spaceship.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
	
	private SpaceResourceAssembler assembler;

	@Autowired
	public RootController(SpaceResourceAssembler assembler) {
		this.assembler = assembler;
	}
	
	
	@GetMapping("/")
	public Resource<String> getResources() {
		return assembler.buildRootResouce("spaceship-service");
	}
	

}
