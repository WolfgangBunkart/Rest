package com.restcourse.spaceship.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class IdResourceSupportMixin extends ResourceSupport {

    @Override
    @JsonIgnore(false)
    public abstract Link getId();
}