package com.restcourse.spaceship.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import com.restcourse.spaceship.exception.AttributesAreNotValidException;
import com.restcourse.spaceship.model.Group;

public class ServiceUtils {
	
	public static <T> T createObjectFromAttributes(Map<String, Object> attributes, Class<T> clazz) throws AttributesAreNotValidException {
		T object = null;
		if(attributes != null) {
			try {
				object = clazz.getDeclaredConstructor().newInstance();
				fillObjectWithAttributes(attributes, object);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new AttributesAreNotValidException("The Attributes are not valid for Class []" + clazz);
			}
		}
		return object;
	}

	public static void fillObjectWithAttributes(Map<String, Object> attributes, Object object) {
		attributes.forEach((k, v) -> {
		    Field field = ReflectionUtils.findField(object.getClass(), k);
		    if(field != null) {		    	
		    	if(k.equals("group")) {		    	
		    		v = Group.valueOf((String)v);
		    	}
		    	field.setAccessible(true);
		    	ReflectionUtils.setField(field, object, v);
		    }else {
		    	throw new IllegalArgumentException();
		    }
		});
	}

}
