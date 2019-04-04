package com.restcourse.spaceship.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SpaceshipControllerIntegrationTest {

	@Value("${local.server.port}")
	private String serverPort;

	private String baseUrl = "http://localhost:<PORT>";

	@Before
	public void setup() {
		baseUrl = baseUrl.replace("<PORT>", serverPort);
	}
}
