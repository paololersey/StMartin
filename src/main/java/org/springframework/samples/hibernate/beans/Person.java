package org.springframework.samples.hibernate.beans;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "person")
public class Person {

	private Integer personId;

	private String firstName;

	private String lastName;

	private String thirdName;

	private String gender;

	private String parentGuardian;

	private String parentGuardianGender;

	private String parentGuardianTel;

	private String fileNumber;

	private String address;

	private String village;

	private String telephone;

	private String email;

	private String state;

	private Date dateOfBirth;

	private Date insertDate;

	private String zone;
	
	private String parish;

	private String majorTraining;

	private String contactPerson;

	private String contactPersonRole;

	private String occupationRole;

	private String volunteerType;

	private String medicalStatus;

	private String father;

	private String deadAliveFather;

	private String mother;

	private String deadAliveMother;

	private String school;

	private Set<ProjectPerson> projectPersons = new HashSet<ProjectPerson>();

	private Set<Sibling> siblingList = new HashSet<Sibling>();

	private Date endDate;

	private Date joiningDate;

	private Date recommitmentDate;

	private String nhifMembershipNumber;

	private String supportGroup;
	
	private String significantOthers;

	private String commobio;
	
	private String drugOfChoice;

	private String relationship;
	
	private String delayedMilestone;
	
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
	@JoinColumn(name = "PERSON_ID", nullable = false)
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

	@Column(name = "PARENT_GUARDIAN_GENDER")
	public String getParentGuardianGender() {
		return parentGuardianGender;
	}

	public void setParentGuardianGender(String parentGuardianGender) {
		this.parentGuardianGender = parentGuardianGender;
	}

	@Column(name = "PARENT_GUARDIAN_TEL")
	public String getParentGuardianTel() {
		return parentGuardianTel;
	}

	public void setParentGuardianTel(String parentGuardianTel) {
		this.parentGuardianTel = parentGuardianTel;
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

	@Column(name = "SCHOOL")
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

	@Column(name = "FATHER")
	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	@Column(name = "DEAD_ALIVE_FATHER")
	public String getDeadAliveFather() {
		return deadAliveFather;
	}

	public void setDeadAliveFather(String deadAliveFather) {
		this.deadAliveFather = deadAliveFather;
	}

	@Column(name = "MOTHER")
	public String getMother() {
		return mother;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	@Column(name = "DEAD_ALIVE_MOTHER")
	public String getDeadAliveMother() {
		return deadAliveMother;
	}

	public void setDeadAliveMother(String deadAliveMother) {
		this.deadAliveMother = deadAliveMother;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "PERSON_ID", nullable = false)
	public Set<Sibling> getSiblingList() {
		return siblingList;
	}

	public void setSiblingList(Set<Sibling> siblingList) {
		this.siblingList = siblingList;
	}

	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "JOINING_DATE")
	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	@Column(name = "RECOMMITMENT_DATE")
	public Date getRecommitmentDate() {
		return recommitmentDate;
	}

	public void setRecommitmentDate(Date recommitmentDate) {
		this.recommitmentDate = recommitmentDate;
	}

	@Column(name = "NHIF_MEMBERSHIP_NUMBER")
	public String getNhifMembershipNumber() {
		return nhifMembershipNumber;
	}

	public void setNhifMembershipNumber(String nhifMembershipNumber) {
		this.nhifMembershipNumber = nhifMembershipNumber;
	}

	@Column(name = "SUPPORT_GROUP")
	public String getSupportGroup() {
		return supportGroup;
	}

	public void setSupportGroup(String supportGroup) {
		this.supportGroup = supportGroup;
	}

	

	@Column(name = "SIGNIFICANT_OTHERS")
	public String getSignificantOthers() {
		return significantOthers;
	}

	public void setSignificantOthers(String significantOthers) {
		this.significantOthers = significantOthers;
	}


	
	@Column(name = "COMMOBIO")
	public String getCommobio() {
		return commobio;
	}

	public void setCommobio(String commobio) {
		this.commobio = commobio;
	}

	@Column(name = "DRUG_OF_CHOICE")
	public String getDrugOfChoice() {
		return drugOfChoice;
	}

	public void setDrugOfChoice(String drugOfChoice) {
		this.drugOfChoice = drugOfChoice;
	}
	
	@Column(name = "RELATIONSHIP")
	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	@Column(name = "PARISH")
	public String getParish() {
		return parish;
	}

	public void setParish(String parish) {
		this.parish = parish;
	}
	
	@Column(name = "DELAYED_MILESTONE")
	public String getDelayedMilestone() {
		return delayedMilestone;
	}

	public void setDelayedMilestone(String delayedMilestone) {
		this.delayedMilestone = delayedMilestone;
	}

}
