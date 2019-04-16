package com.restcourse.spaceship.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.AnnotationRelProvider;
import org.springframework.hateoas.hal.Jackson2HalModule;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
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

	public static List<Spaceship> resourcesJsonToSpaceshipList(String resourcesContentAsString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		configureObjectMapperToMapId(objectMapper);
		objectMapper.registerModule(new Jackson2HalModule());
		objectMapper.setHandlerInstantiator(
		        new Jackson2HalModule.HalHandlerInstantiator(new AnnotationRelProvider(), null, null));
		Resources<Resource<Spaceship>> resource = objectMapper.readValue(resourcesContentAsString, objectMapper.getTypeFactory().constructParametricType(Resources.class,
                objectMapper.getTypeFactory().constructParametricType(Resource.class, Spaceship.class)));
		Collection<Resource<Spaceship>> content = resource.getContent();
		return content.stream().map(r -> r.getContent()).collect(Collectors.toList());
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
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		objectMapper.addMixIn(ResourceSupport.class, IdResourceSupportMixin.class);
		
	}

}
