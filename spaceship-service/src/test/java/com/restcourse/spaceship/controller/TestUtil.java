package com.restcourse.spaceship.controller;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {
	
	public static <T> T toObject(String jsonString, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsonString, clazz);
	}

	public static String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}

}
