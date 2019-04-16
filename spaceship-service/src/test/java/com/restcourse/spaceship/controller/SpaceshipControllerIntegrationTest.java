package com.restcourse.spaceship.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restcourse.spaceship.SpaceshipServiceApplication;
import com.restcourse.spaceship.model.Group;
import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;
import com.restcourse.spaceship.repository.SpaceDataRepository;
import com.restcourse.spaceship.repository.StarWarsTestScenario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = SpaceshipServiceApplication.class)
@AutoConfigureMockMvc
public class SpaceshipControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SpaceDataRepository repository;

	@Autowired
	private StarWarsTestScenario testScenario;

	@Value("${local.server.port}")
	private String serverPort;

	private String baseUrl = "http://localhost:<PORT>";

	@Before
	public void setup() {
		baseUrl = baseUrl.replace("<PORT>", serverPort);
		testScenario.initTestData();
	}

	@Test
	public void testGetSpaceshipOneSuccess() throws Exception {
		MvcResult result = mockMvc.perform(get(baseUrl + "/spaceships/1"))//
				.andExpect(status().is2xxSuccessful()).andReturn();

		Spaceship spaceship = TestUtil.resourceJsonToSpaceship(result.getResponse().getContentAsString());
		Spaceship expectedSpaceship = repository.findSpaceship(1L).get();

		assertThat(spaceship).isEqualTo(expectedSpaceship);
	}
	
	@Test
	public void createSpaceship() throws Exception {
		Spaceship spaceshipOne = new Spaceship();
		spaceshipOne.setName("spaceshipOne");
		
		spaceshipOne.setGroup(Group.EMPIRE);
		spaceshipOne.setReadyToFly(true);

		String json = TestUtil.toJson(spaceshipOne);

		MvcResult result = mockMvc.perform(//
				post(baseUrl + "/spaceships")//
						.contentType(MediaType.APPLICATION_JSON)//
						.content(json))//
				.andExpect(status().is2xxSuccessful())//
				.andReturn();

		Spaceship resultSpaceship = TestUtil.resourceJsonToSpaceship(result.getResponse().getContentAsString());
		assertThat(resultSpaceship.getId()).isNotNull();
		assertThat(resultSpaceship.getName()).isEqualTo("spaceshipOne");
		assertThat(resultSpaceship.isReadyToFly()).isTrue();
		assertThat(resultSpaceship.getGroup()).isEqualTo(Group.EMPIRE);
	}
	
	@Test
	public void createSpaceshipNameNotSetValidationFailed() throws Exception {
		Spaceship spaceshipOne = new Spaceship();
		
		spaceshipOne.setGroup(Group.EMPIRE);
		spaceshipOne.setReadyToFly(true);

		String json = TestUtil.toJson(spaceshipOne);

		mockMvc.perform(//
				post(baseUrl + "/spaceships")//
						.contentType(MediaType.APPLICATION_JSON)//
						.content(json))//
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void createSpaceshipNameNotValidValidationFailed() throws Exception {
		Spaceship spaceshipOne = new Spaceship();
		
		spaceshipOne.setGroup(Group.EMPIRE);
		spaceshipOne.setReadyToFly(true);
		spaceshipOne.setName("1234");
		
		String json = TestUtil.toJson(spaceshipOne);
		
		mockMvc.perform(//
				post(baseUrl + "/spaceships")//
				.contentType(MediaType.APPLICATION_JSON)//
				.content(json))//
		.andExpect(status().is4xxClientError());
	}

	@Test
	public void testGetSpaceshipTwoHundertError() throws Exception {
		mockMvc.perform(get(baseUrl + "/spaceships/200"))//
				.andExpect(status().isNotFound());
	}

	@Test
	public void testGetAllSpaceships() throws Exception {
		MvcResult result = mockMvc.perform(get(baseUrl + "/pilots"))//
				.andExpect(status().is2xxSuccessful()).andReturn();

		List<?> spaceShipList = TestUtil.toObject(result.getResponse().getContentAsString(), List.class);

		assertThat(spaceShipList.size()).isEqualTo(testScenario.getSpaceships().size());
	}

	@Test
	public void testGetAllReadyToFlySpaceships() throws Exception {
		MvcResult result = mockMvc.perform(get(baseUrl + "/spaceships").param("readyToFly", "true"))//
				.andExpect(status().is2xxSuccessful()).andReturn();
		List<?> spaceship = TestUtil.resourcesJsonToSpaceshipList(result.getResponse().getContentAsString());
		List<Spaceship> expectedReadyToFlySpaceships = repository.findReadyToFlySpaceships().get();

		assertThat(spaceship.size()).isEqualTo(expectedReadyToFlySpaceships.size());
	}

	@Test
	public void testGetSpaceshipTwoHundretNotFound() throws Exception {
		mockMvc.perform(get(baseUrl + "/spaceships/200"))//
				.andExpect(status().isNotFound()).andReturn();
	}

	@Test
	public void deleteSpaceshipOne() throws Exception {
		mockMvc.perform(//
				delete(baseUrl + "/spaceships/1"))//
				.andExpect(status().is2xxSuccessful());

		mockMvc.perform(get(baseUrl + "spaceships/1")).andExpect(status().isNotFound());
	}

	@Test
	public void updateSpaceshipOne() throws Exception {
		Spaceship spaceshipOne = repository.findSpaceship(1L).get();
		spaceshipOne.setReadyToFly(false);

		String json = TestUtil.toJson(spaceshipOne);

		MvcResult result = mockMvc.perform(//
				put(baseUrl + "/spaceships/1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.content(json))//
				.andExpect(status().is2xxSuccessful())//
				.andReturn();

		Spaceship resultSpaceship = TestUtil.resourceJsonToSpaceship(result.getResponse().getContentAsString());
		assertThat(resultSpaceship).isEqualTo(spaceshipOne);
	}


	@Test
	public void testPatchSpaceshipOneSuccess() throws Exception {
		Map<String, Object> spaceshipAttributes = new HashMap<>();
		
		spaceshipAttributes.put("name", "spaceshipOne");
		spaceshipAttributes.put("containsDroid", true);
		spaceshipAttributes.put("group", Group.REBELLION);
		
		String json = TestUtil.toJson(spaceshipAttributes);

		MvcResult result = mockMvc.perform(//
				patch(baseUrl + "/spaceships/1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.content(json))//
				.andExpect(status().is2xxSuccessful())//
				.andReturn();

		Spaceship resultSpaceship = TestUtil.resourceJsonToSpaceship(result.getResponse().getContentAsString());
		assertThat(resultSpaceship.getName()).isEqualTo("spaceshipOne");
		assertThat(resultSpaceship.isContainsDroid()).isTrue();
	}

	
	@Test
	public void testUpdatePilotRelation() throws Exception {
		Pilot pilot1 = testScenario.getPilots().get(0);
		Pilot pilot2 = testScenario.getPilots().get(1);
		
		List<Pilot> pilotsToAdd = Arrays.asList(pilot1, pilot2);

		String json = TestUtil.toJson(pilotsToAdd);
		
		MvcResult result = mockMvc.perform(//
				patch(baseUrl + "/spaceships/1/pilots")//
						.contentType(MediaType.APPLICATION_JSON)//
						.content(json))//
				.andExpect(status().is2xxSuccessful())//
				.andReturn();
		
		Spaceship updatedSpaceship = TestUtil.resourceJsonToSpaceship(result.getResponse().getContentAsString());
		
		assertThat(updatedSpaceship.getPilots().size()).isEqualTo(2);
		assertThat(updatedSpaceship.getPilots().get(0)).isEqualTo(pilot1);
		assertThat(updatedSpaceship.getPilots().get(1)).isEqualTo(pilot2);
	}
	
	@Test
	public void testDeletePilotRelation() throws Exception {
		addTwoPilotsToSpaceshipOne();
		
		String emptyListJson = TestUtil.toJson(new ArrayList<>());
		
		MvcResult result = mockMvc.perform(//
				patch(baseUrl + "/spaceships/1/pilots")//
						.contentType(MediaType.APPLICATION_JSON)//
						.content(emptyListJson))//
				.andExpect(status().is2xxSuccessful()).andReturn();
		
		Spaceship updatedSpaceship = TestUtil.resourceJsonToSpaceship(result.getResponse().getContentAsString());
		
		assertThat(updatedSpaceship.getPilots().size()).isEqualTo(0L);
	}

	private void addTwoPilotsToSpaceshipOne() throws JsonProcessingException, Exception {
		Pilot pilot1 = testScenario.getPilots().get(0);
		Pilot pilot2 = testScenario.getPilots().get(1);
		List<Pilot> pilotsToAdd = Arrays.asList(pilot1, pilot2);
		String json = TestUtil.toJson(pilotsToAdd);
		
		mockMvc.perform(//
				patch(baseUrl + "/spaceships/1/pilots")//
						.contentType(MediaType.APPLICATION_JSON)//
						.content(json))//
				.andExpect(status().is2xxSuccessful());
	}
	
	

	@Test
	public void testUpdatePilotRelationNotFound() throws Exception {
		
		Pilot pilot = testScenario.getPilots().get(0);
		
		String json = TestUtil.toJson(pilot);
		
		mockMvc.perform(//
				patch(baseUrl + "/spaceships/999/pilots")//
				.contentType(MediaType.APPLICATION_JSON)//
				.content(json))//
				.andExpect(status().is4xxClientError());//
	}
	
	

}

