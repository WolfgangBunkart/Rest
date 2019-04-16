package com.restcourse.spaceship.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;

public class TestUtil {

	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(String jsonString, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsonString, List.class);
	}

	public static <T> T toObject(String jsonString, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsonString, clazz);
	}

	public static String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}

	public static Spaceship resourceJsonToSpaceship(String resourceContentAsString) throws JsonParseException, JsonMappingException, IOException {
		return resourceJsonToSpaceshipResource(resourceContentAsString).getContent();
	}
	
	public static Pilot resourceJsonToPilot(String resourceContentAsString) throws JsonParseException, JsonMappingException, IOException {		
		return resourceJsonToPilotResource(resourceContentAsString).getContent();
	}

	public static Resource<Pilot> resourceJsonToPilotResource(String resourceContentAsString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		configureObjectMapperToMapId(objectMapper);
		Resource<Pilot> resource = objectMapper.readValue(resourceContentAsString, new TypeReference<Resource<Spaceship>>() {});
		return resource;
	}

	public static Resource<Spaceship> resourceJsonToSpaceshipResource(String resourceContentAsString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		configureObjectMapperToMapId(objectMapper);
		Resource<Spaceship> resource = objectMapper.readValue(resourceContentAsString, new TypeReference<Resource<Spaceship>>() {});
		return resource;
	}

	private static void configureObjectMapperToMapId(ObjectMapper objectMapper) {
		objectMapper.addMixIn(ResourceSupport.class, IdResourceSupportMixin.class);
	}

}
