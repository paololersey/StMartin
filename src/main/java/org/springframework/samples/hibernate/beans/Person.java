package org.springframework.samples.hibernate.beans;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Person {

	private Integer personId;

	private String firstName;

	private String lastName;
	
	private String thirdName;
	
	private String gender;
	
	private String parentGuardian;
	
	private String fileNumber;

	private String address;

	private String village;
	
	private String telephone;

	private String email;
	
	private String state;
	
	private Date dateOfBirth;
	
	private Date insertDate;
	
	private String zone;
	
	private String majorTraining;
	
	private String contactPerson;
	
	private String contactPersonRole;
	
	private String occupationRole;
	
	private String volunteerType;
	
	private String medicalStatus;
	
	private String school;
	
	private Set<ProjectPerson> projectPersons=new HashSet<ProjectPerson>();
	

	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "PERSON_ID")
	public Integer getPersonId() {
		return this.personId;
	}

	public void setPersonId(Integer personId) {
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
	
	@Column(name = "VILLAGE")
	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}
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
	
	@Column(name = "DATE_OF_BIRTH")
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
    
	@Column(name = "STATE")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
    
	@Column(name = "ZONE")
	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}
	@Column(name = "THIRDNAME")
	public String getThirdName() {
		return thirdName;
	}

	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}
	@Column(name = "PARENT_GUARDIAN")
	public String getParentGuardian() {
		return parentGuardian;
	}

	public void setParentGuardian(String parentGuardian) {
		this.parentGuardian = parentGuardian;
	}
	@Column(name = "FILE_NUMBER")
	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	@Column(name = "MAJOR_TRAINING")
	public String getmajorTraining() {
		return majorTraining;
	}

	public void setmajorTraining(String majorTraining) {
		this.majorTraining = majorTraining;
	}
	
	@Column(name = "CONTACT_PERSON")	
	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	@Column(name = "OCCUPATION_ROLE")
	public String getOccupationRole() {
		return occupationRole;
	}

	public void setOccupationRole(String occupationRole) {
		this.occupationRole = occupationRole;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "CONTACT_PERSON_ROLE")
	public String getContactPersonRole() {
		return contactPersonRole;
	}

	public void setContactPersonRole(String contactPersonRole) {
		this.contactPersonRole = contactPersonRole;
	}
	
	@Column(name = "VOLUNTEER_TYPE")
	public String getVolunteerType() {
		return volunteerType;
	}
	
	public void setVolunteerType(String volunteerType) {
		this.volunteerType = volunteerType;
	}
	@Column(name = "MEDICAL_STATUS")
	public String getMedicalStatus() {
		return medicalStatus;
	}

	public void setMedicalStatus(String medicalStatus) {
		this.medicalStatus = medicalStatus;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	@Column(name = "INSERT_DATE")
	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	
	//@Column(name = "MEDICAL_STATUS")
		
	
	
}
