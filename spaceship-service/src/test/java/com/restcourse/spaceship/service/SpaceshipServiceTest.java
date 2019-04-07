package com.restcourse.spaceship.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

import com.restcourse.spaceship.exception.AttributesAreNotValidException;
import com.restcourse.spaceship.exception.GroupNotChangeableException;
import com.restcourse.spaceship.model.Group;
import com.restcourse.spaceship.model.Pilot;
import com.restcourse.spaceship.model.Spaceship;
import com.restcourse.spaceship.repository.SpaceDataRepository;
import com.restcourse.spaceship.repository.StarWarsTestScenario;

public class SpaceshipServiceTest {

	private SpaceshipService service;

	private StarWarsTestScenario testScenario = new StarWarsTestScenario();

	@Before
	public void setup() {
		testScenario.initTestData();
		SpaceDataRepository repo = new SpaceDataRepository(testScenario.getPilots(), testScenario.getSpaceships());
		service = new SpaceshipService(repo, new BusinessValidator());
	}

	@Test
	public void testCreatePilotSuccess() {
		Pilot pilot = new Pilot("JUnit", "Lastname", Group.EMPIRE);
		int currentSize = testScenario.getPilots().size();

		Pilot createdPilot = service.createPilot(pilot);

		assertThat(createdPilot.getId()).isNotNull();
		assertThat(testScenario.getPilots().size()).isEqualTo(currentSize + 1);
	}

	@Test
	public void testFindPilotSuccess() {
		Optional<Pilot> pilot = service.findPilot(2L);
		Optional<Pilot> notPresentPilot = service.findPilot(999L);

		assertThat(pilot).isPresent();
		assertThat(notPresentPilot).isEmpty();
	}

	@Test
	public void testGetAllPilotsSuccess() {
		Optional<List<Pilot>> allPilots = service.getAllPilots();

		assertThat(allPilots).isPresent();
		assertThat(allPilots.get().size()).isEqualTo(testScenario.getPilots().size());
	}

	@Test
	public void testDeletePilot() {
		service.deletePilot(1L);

		assertThat(service.findPilot(1L)).isEmpty();
	}

	@Test
	public void testCreateOrUpdatePilotUpdateAllAttributes() throws GroupNotChangeableException {
		Pilot existingPilot = new Pilot();
		String firstName = "JunitFirstName";

		existingPilot.setFirstName(firstName);

		existingPilot.setGroup(Group.REBELLION);

		Optional<Pilot> updatedPilot = service.createOrUpdatePilot(1L, existingPilot);

		assertThat(updatedPilot.isPresent());
		assertThat(updatedPilot.get().getLastName()).isNull();
		assertThat(updatedPilot.get().getFirstName()).isEqualTo(firstName);
		assertThat(updatedPilot.get().getGroup()).isEqualTo(Group.REBELLION);
		assertThat(updatedPilot.get().getId()).isEqualTo(1L);

	}

	@Test(expected = GroupNotChangeableException.class)
	public void testCreateOrUpdatePilotGroupNotChangeableException() throws GroupNotChangeableException {
		Pilot existingPilot = new Pilot();
		String firstName = "JunitFirstName";

		existingPilot.setFirstName(firstName);
		existingPilot.setGroup(Group.EMPIRE);

		service.createOrUpdatePilot(1L, existingPilot);
	}

	@Test
	public void testCreateOrUpdatePilotCreateNew() throws GroupNotChangeableException {
		Pilot newPilot = new Pilot();
		String firstName = "JunitFirstName";

		newPilot.setFirstName(firstName);
		newPilot.setGroup(Group.REBELLION);

		Optional<Pilot> updatedPilot = service.createOrUpdatePilot(99L, newPilot);

		assertThat(updatedPilot.isPresent());
		assertThat(updatedPilot.get().getLastName()).isNull();
		assertThat(updatedPilot.get().getFirstName()).isEqualTo(firstName);
		assertThat(updatedPilot.get().getGroup()).isEqualTo(Group.REBELLION);
		assertThat(updatedPilot.get().getId()).isEqualTo(99L);
	}

	@Test
	public void testPatchPilotSuccess() throws GroupNotChangeableException, AttributesAreNotValidException {
		String firstName = "JunitFirstName";

		Map<String, Object> pilotAttributes = new HashMap<>();
		pilotAttributes.put("firstName", firstName);
		pilotAttributes.put("group", "REBELLION");

		Optional<Pilot> updatedPilot = service.patchPilot(1L, pilotAttributes);

		assertThat(updatedPilot.isPresent());
		assertThat(updatedPilot.get().getLastName()).isNotNull();
		assertThat(updatedPilot.get().getFirstName()).isEqualTo(firstName);
		assertThat(updatedPilot.get().getGroup()).isEqualTo(Group.REBELLION);
		assertThat(updatedPilot.get().getId()).isEqualTo(1L);
	}

	@Test(expected = GroupNotChangeableException.class)
	public void testPatchPilotGroupNotChangeableException()
			throws GroupNotChangeableException, AttributesAreNotValidException {
		String firstName = "JunitFirstName";

		Map<String, Object> pilotAttributes = new HashMap<>();
		pilotAttributes.put("firstName", firstName);
		pilotAttributes.put("group", "EMPIRE");

		service.patchPilot(1L, pilotAttributes);
	}

	@Test(expected = AttributesAreNotValidException.class)
	public void testPatchPilotWrongAttributes() throws GroupNotChangeableException, AttributesAreNotValidException {
		String firstName = "JunitFirstName";

		Map<String, Object> pilotAttributes = new HashMap<>();
		pilotAttributes.put("wrongAttribute", firstName);
		pilotAttributes.put("group", "REBELLION");

		service.patchPilot(1L, pilotAttributes);
	}

	@Test
	public void testCreateSpacehip() {
		Spaceship spaceship = new Spaceship();
		spaceship.setName("spaceship1");
		spaceship.setGroup(Group.REBELLION);
		Spaceship createdSpaceship = service.createSpaceship(spaceship);

		Optional<Spaceship> newSpaceship = service.findSpaceship(createdSpaceship.getId());

		assertThat(newSpaceship).isPresent();
		assertThat(newSpaceship.get().getName()).isEqualTo("spaceship1");
	}

	@Test
	public void testFindSpacehip() {
		Optional<Spaceship> spaceship = service.findSpaceship(1L);
		assertThat(spaceship).isPresent();
		assertThat(spaceship.get().getId()).isEqualTo(1L);
	}

	@Test
	public void testGetAllSpacehips() {
		Optional<List<Spaceship>> spaceships = service.getAllSpaceships();
		assertThat(spaceships).isPresent();
		assertThat(spaceships.get().size()).isEqualTo(testScenario.getSpaceships().size());
	}

	@Test
	public void testGetAllReadyToFlySpacehips() {
		Optional<List<Spaceship>> spaceships = service.getAllSpaceships(true);
		assertThat(spaceships).isPresent();
		assertThat(spaceships.get().size()).isEqualTo(4L);
	}

	@Test
	public void testDeleteSpacehip() {
		service.deleteSpaceship(1L);
		assertThat(service.findSpaceship(1L)).isEmpty();
	}
	
	@Test
	public void testCreateOrUpdateSpaceShipCreateNew() throws GroupNotChangeableException {
		Spaceship newSpaceship = new Spaceship();
		newSpaceship.setName("New Spaceship");
		newSpaceship.setGroup(Group.EMPIRE);
		
		Optional<Spaceship> createdSpaceship = service.createOrUpdateSpaceship(99L, newSpaceship);
		
		assertThat(createdSpaceship).isPresent();
		assertThat(createdSpaceship.get().getName()).isEqualTo("New Spaceship");
		assertThat(createdSpaceship.get().getId()).isEqualTo(99L);
	}

	@Test
	public void testCreateOrUpdateSpaceShipUpdateExisting() throws GroupNotChangeableException {
		Optional<Spaceship> existingSpaceship = service.findSpaceship(1L);
		existingSpaceship.get().setName("updated Name");
		existingSpaceship.get().setGroup(Group.EMPIRE);
		
		Optional<Spaceship> createdSpaceship = service.createOrUpdateSpaceship(1L, existingSpaceship.get());
		
		assertThat(createdSpaceship).isPresent();
		assertThat(createdSpaceship.get().getName()).isEqualTo("updated Name");
		assertThat(createdSpaceship.get().getId()).isEqualTo(1L);
	}
	
	@Test (expected = GroupNotChangeableException.class)
	public void testCreateOrUpdateSpaceShipGroupNotchangable() throws GroupNotChangeableException {
		Spaceship existingSpaceship = new Spaceship();
		existingSpaceship.setId(1L);
		existingSpaceship.setGroup(Group.EMPIRE);
		
		service.createOrUpdateSpaceship(1L, existingSpaceship);
	}

	@Test
	public void testPatchSpaceShipSuccess() throws GroupNotChangeableException, AttributesAreNotValidException {
		Map<String, Object> spaceshipAttributes = new HashMap<>();
		spaceshipAttributes.put("name", "updated Name");
		spaceshipAttributes.put("group", "REBELLION");
		Optional<Spaceship> createdSpaceship = service.patchSpaceship(1L, spaceshipAttributes);
		
		assertThat(createdSpaceship).isPresent();
		assertThat(createdSpaceship.get().getName()).isEqualTo("updated Name");
		assertThat(createdSpaceship.get().getGroup()).isEqualTo(Group.REBELLION);
		assertThat(createdSpaceship.get().getId()).isEqualTo(1L);
	}

	@Test (expected = AttributesAreNotValidException.class)
	public void testPatchSpaceShipAttributesNotValid() throws GroupNotChangeableException, AttributesAreNotValidException {
		Map<String, Object> spaceshipAttributes = new HashMap<>();
		spaceshipAttributes.put("notValidAtt", "updated Att");
		spaceshipAttributes.put("group", "REBELLION");
		service.patchSpaceship(1L, spaceshipAttributes);
	}

	@Test (expected = GroupNotChangeableException.class)
	public void testPatchSpaceShipGroupNotChangable() throws GroupNotChangeableException, AttributesAreNotValidException {
		Map<String, Object> spaceshipAttributes = new HashMap<>();
		spaceshipAttributes.put("group", "EMPIRE");
		service.patchSpaceship(1L, spaceshipAttributes);
	}

	@Test 
	public void testPatchPilotSpaceshipRelation() throws GroupNotChangeableException, AttributesAreNotValidException {
		Optional<Spaceship> updatedSpaceship = service.updatePilotSpaceshipRelation(1L, 1L);
		assertThat(updatedSpaceship).isPresent();
		assertThat(updatedSpaceship.get().getPilots().get(0).getId()).isEqualTo(1L);
	}
	
	@Test 
	public void testDeletePilotSpaceshipRelation() throws GroupNotChangeableException, AttributesAreNotValidException {
		service.updatePilotSpaceshipRelation(1L, 1L);
		Optional<Spaceship> updatedSpaceship = service.deletePilotSpaceshipRelation(1L, 1L);
		assertThat(updatedSpaceship).isPresent();
		assertThat(updatedSpaceship.get().getPilots()).isEmpty();
	}
}
