package org.springframework.samples.hibernate.beans;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnore;

public class GlobalPerson {

	@JsonIgnore
	private Person person;
	
	private ProjectPerson projectPerson;
	
	private NatureOfCasePerson natureOfCasePerson;
	
	private ArrayList<Sibling> siblingList;
	
	private Payment payment;

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

	public ArrayList<Sibling> getSiblingList() {
		return siblingList;
	}

	public void setSiblingList(ArrayList<Sibling> siblingList) {
		this.siblingList = siblingList;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	

	

	
}
