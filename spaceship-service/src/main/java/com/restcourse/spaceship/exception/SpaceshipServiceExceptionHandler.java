package com.restcourse.spaceship.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SpaceshipServiceExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(SpaceshipServiceExceptionHandler.class);

	
	@ExceptionHandler (GroupNotChangeableException.class)
	public ResponseEntity<ApiError> handleException(GroupNotChangeableException ex) {
		log.error("CustomValidationException occured: {}", ex.getMessage());
		ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), "SPACE-001", GroupNotChangeableException.class.getName(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
