package com.restcourse.spaceship.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.restcourse.spaceship.model.Group;
import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.repository.StarWarsTestScenario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PilotControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
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
	public void testGetAllPilots() throws Exception {
		MvcResult result = mockMvc.perform(get(baseUrl + "/pilots"))//
		.andExpect(status().is2xxSuccessful()).andReturn();

		List<Pilot> pilots = TestUtil.toList(result.getResponse().getContentAsString(), Pilot.class);
		
		assertThat(pilots.size()).isEqualTo(testScenario.getPilots().size());
		
	}
	
	@Test
	public void testGetOnePilot() throws Exception {
		MvcResult result = mockMvc.perform(get(baseUrl + "/pilots/1"))//
				.andExpect(status().is2xxSuccessful()).andReturn();
		
		Pilot pilot = TestUtil.toObject(result.getResponse().getContentAsString(), Pilot.class);
		
		assertThat(pilot.getFirstName()).isEqualTo(testScenario.getPilots().get(0).getFirstName());
		assertThat(pilot.getLastName()).isEqualTo(testScenario.getPilots().get(0).getLastName());
		assertThat(pilot.getGroup()).isEqualTo(testScenario.getPilots().get(0).getGroup());
		assertThat(pilot.getId()).isEqualTo(1L);
	}

	@Test
	public void testGetOnePilotNotfound() throws Exception {
		mockMvc.perform(get(baseUrl + "/pilots/99"))//
				.andExpect(status().is4xxClientError());
		
	}

	@Test
	public void testGetOnePilotInvalidChar() throws Exception {
		mockMvc.perform(get(baseUrl + "/pilots/asdffds"))//
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void testDeleteOnePilot() throws Exception {
		mockMvc.perform(delete(baseUrl + "/pilots/1"))//
				.andExpect(status().is2xxSuccessful());
		
		mockMvc.perform(get(baseUrl + "/pilots/1"))//
		.andExpect(status().is4xxClientError());
	}
	
	
	@Test
	public void testUpdateOnePilot() throws Exception {
		
		Pilot pilot = testScenario.getPilots().get(0);
		pilot.setFirstName("Luuuke");
		
		String json = TestUtil.toJson(pilot);
		MvcResult result = mockMvc.perform(put(baseUrl + "/pilots/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))//
				
				.andExpect(status().is2xxSuccessful()).andReturn();
		
		Pilot updatedPilot = TestUtil.toObject(result.getResponse().getContentAsString(), Pilot.class);
		assertThat(updatedPilot.getFirstName()).isEqualTo("Luuuke");
		assertThat(updatedPilot.getLastName()).isEqualTo(testScenario.getPilots().get(0).getLastName());
		assertThat(updatedPilot.getGroup()).isEqualTo(testScenario.getPilots().get(0).getGroup());
		assertThat(updatedPilot.getId()).isEqualTo(1L);
	}

	@Test
	public void testCreateOnePilotSucess() throws Exception {
		
		Pilot pilot = new Pilot();
		pilot.setGroup(Group.EMPIRE);
		pilot.setFirstName("firstName");
		pilot.setLastName("lastName");
		
		String json = TestUtil.toJson(pilot);
		MvcResult result = mockMvc.perform(put(baseUrl + "/pilots/10")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))//
				.andExpect(status().is2xxSuccessful()).andReturn();
		
		Pilot updatedPilot = TestUtil.toObject(result.getResponse().getContentAsString(), Pilot.class);
		assertThat(updatedPilot.getFirstName()).isEqualTo("firstName");
		assertThat(updatedPilot.getLastName()).isEqualTo("lastName");
		assertThat(updatedPilot.getGroup()).isEqualTo(Group.EMPIRE);
		assertThat(updatedPilot.getId()).isEqualTo(10L);
	}
	
	@Test
	public void testCreateOnePilotLastNameNotSetValidationFailed() throws Exception {
		
		Pilot pilot = new Pilot();
		pilot.setGroup(Group.EMPIRE);
		pilot.setFirstName("CreatedByPUT");
		
		String json = TestUtil.toJson(pilot);
		mockMvc.perform(put(baseUrl + "/pilots/10")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))//
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void testPatchOnePilot() throws Exception {
		
		Map<String, Object> pilotAttributes = new HashMap<>();
		
		pilotAttributes.put("firstName", "Luuuukkeeee");
		pilotAttributes.put("group", "REBELLION");
		
		String json = TestUtil.toJson(pilotAttributes);
		
		MvcResult result = mockMvc.perform(patch(baseUrl + "/pilots/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))//
				.andExpect(status().is2xxSuccessful()).andReturn();
		
		Pilot updatedPilot = TestUtil.toObject(result.getResponse().getContentAsString(), Pilot.class);
		assertThat(updatedPilot.getFirstName()).isEqualTo("Luuuukkeeee");
		assertThat(updatedPilot.getLastName()).isEqualTo("Skywalker");
		assertThat(updatedPilot.getGroup()).isEqualTo(Group.REBELLION);
		assertThat(updatedPilot.getId()).isEqualTo(1L);
	}

	@Test
	public void testPatchNotExistingPilot() throws Exception {
		
		Map<String, Object> pilotAttributes = new HashMap<>();
		
		pilotAttributes.put("firstName", "Luuuukkeeee");
		pilotAttributes.put("group", "REBELLION");
		
		String json = TestUtil.toJson(pilotAttributes);
		
		mockMvc.perform(patch(baseUrl + "/pilots/100")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))//
				.andExpect(status().is4xxClientError());
	}

	
	@Test
	public void testCreatePostOnePilot() throws Exception {
		
		Pilot pilot = new Pilot();
		pilot.setGroup(Group.EMPIRE);
		pilot.setFirstName("FirstNameCreatedByPOST");
		pilot.setLastName("LastNameCreatedByPOST");
		
		String json = TestUtil.toJson(pilot);
		MvcResult result = mockMvc.perform(post(baseUrl + "/pilots")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))//
				.andExpect(status().is2xxSuccessful()).andReturn();
		
		Pilot updatedPilot = TestUtil.toObject(result.getResponse().getContentAsString(), Pilot.class);
		assertThat(updatedPilot.getFirstName()).isEqualTo("FirstNameCreatedByPOST");
		assertThat(updatedPilot.getLastName()).isEqualTo("LastNameCreatedByPOST");
		assertThat(updatedPilot.getGroup()).isEqualTo(Group.EMPIRE);
		assertThat(updatedPilot.getId()).isNotNull();
	}
	
	

}
