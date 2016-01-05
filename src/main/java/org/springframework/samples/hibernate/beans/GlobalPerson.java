package org.springframework.samples.hibernate.beans;

public class GlobalPerson {

	private Person person;
	
	private ProjectPerson projectPerson;
	
	private NatureOfCasePerson natureOfCasePerson;

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

	public NatureOfCasePerson getNatureOfCasePerson() {
		return natureOfCasePerson;
	}

	public void setNatureOfCasePerson(NatureOfCasePerson natureOfCasePerson) {
		this.natureOfCasePerson = natureOfCasePerson;
	}

	

	
}
