package org.springframework.samples.hibernate.beans;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Entity
public class Person {

	private Long personId;

	private String firstName;

	private String lastName;

	private String address;

	private String city;
	
	private String telephone;

	private String email;
	
	private Set<ProjectPerson> projectPersons=new HashSet<ProjectPerson>();


	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "PERSON_ID")
	public Long getPersonId() {
		return this.personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	@NotNull
	@Column(name = "FIRSTNAME")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotNull
	@Column(name = "LASTNAME")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
																															
	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		
		this.city = city;
	}
	

	/*@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "PROJECT_PERSON", 
	           joinColumns = @JoinColumn(name = "PERSON_ID"), 
	           inverseJoinColumns = @JoinColumn(name = "PROJECT_ID"))*/
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="PERSON_ID",nullable=false)
	public Set<ProjectPerson> getProjectPersons() {
		return projectPersons;
	}

	public void setProjectPersons(Set<ProjectPerson> projectPersons) {
		this.projectPersons = projectPersons;
	}
	
	@Column(name = "TELEPHONE")
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
