package org.springframework.samples.hibernate.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="sibling")
public class Sibling {

	private int siblingId;

	private int personId;

	private String siblingName;

	private Date siblingDateOfBirth;
	
	private String siblingGender;


	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "SIBLING_ID")
	public int getSiblingId() {
		return this.siblingId;
	}

	public void setSiblingId(int siblingId) {
		this.siblingId = siblingId;
	}
    
	@NotNull
	@Column(name = "PERSON_ID",insertable=false,updatable=false)
	public int getPersonId() {
		return this.personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	

	@NotNull
	@Column(name = "SIBLING_NAME")
	public String getSiblingName() {
		return siblingName;
	}

	public void setSiblingName(String siblingName) {
		this.siblingName = siblingName;
	}

	@Column(name = "SIBLING_DATE_OF_BIRTH")
	public Date getSiblingDateOfBirth() {
		return siblingDateOfBirth;
	}

	public void setSiblingDateOfBirth(Date siblingDateOfBirth) {
		this.siblingDateOfBirth = siblingDateOfBirth;
	}


	@Column(name = "SIBLING_GENDER")	
	public String getSiblingGender() {
		return siblingGender;
	}

	public void setSiblingGender(String siblingGender) {
		this.siblingGender = siblingGender;
	}

	

}
