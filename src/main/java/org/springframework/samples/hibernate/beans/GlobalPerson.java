package org.springframework.samples.hibernate.beans;

public class GlobalPerson {

	private Person person;
	
	private ProjectPerson projectPerson;

	public ProjectPerson getProjectPerson() {
		return projectPerson;
	}

	public void setProjectPerson(ProjectPerson projectPerson) {
		this.projectPerson = projectPerson;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	

	
}
