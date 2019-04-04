package com.restcourse.spaceship.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Spaceship {
	
	private Long id;
	
	@NotNull
	@Size(min = 5, message = "The name must be min 5 characters")
	private String name;
	
	private String serialNumber;
	
	private Integer numberOfWeapons;
	
	private boolean containsDroid;
	
	private List<Pilot> pilots = new ArrayList<>();
	
	@NotNull
	private Group group;
	
	private boolean armed;
	
	private boolean readyToFly;
	
	public Spaceship() {
	}
	
	public Spaceship(Long id, String name, Group group, boolean armed, boolean readytoFly, Integer numberOfWeapons) {
		this.id = id;
		this.name = name;
		this.group = group;
		this.armed = armed;
		this.readyToFly = readytoFly;
		this.numberOfWeapons = numberOfWeapons;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public List<Pilot> getPilots() {
		return pilots;
	}

	public void addPilot(Pilot pilot) {
		pilots.add(pilot);
	}
	
	public void setPilots(List<Pilot> pilots) {
		this.pilots = pilots;
	}

	public void removePilot(Pilot pilot) {
		pilots.remove(pilot);
	}

	public boolean isArmed() {
		return armed;
	}

	public void setArmed(boolean armed) {
		this.armed = armed;
	}

	public boolean isReadyToFly() {
		return readyToFly;
	}

	public void setReadyToFly(boolean readyToFly) {
		this.readyToFly = readyToFly;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getNumberOfWeapons() {
		return numberOfWeapons;
	}

	public void setNumberOfWeapons(Integer numberOfWeapons) {
		this.numberOfWeapons = numberOfWeapons;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pilots == null) ? 0 : pilots.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Spaceship other = (Spaceship) obj;
		if (group != other.group)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pilots == null) {
			if (other.pilots != null)
				return false;
		} else if (!pilots.equals(other.pilots))
			return false;
		return true;
	}

	public boolean isContainsDroid() {
		return containsDroid;
	}

	public void setContainsDroid(boolean containsDroid) {
		this.containsDroid = containsDroid;
	}

}
