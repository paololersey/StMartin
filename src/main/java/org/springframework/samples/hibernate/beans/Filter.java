package org.springframework.samples.hibernate.beans;

import java.util.Date;

public class Filter {


	private Date dateStart;
	
	private Date dateEnd;
	
	private Integer personIdBeneficiary;
	
	private Integer personIdPersonInCharge;
	
	private String  status;
	
	private String  personType;
	
	private String  projectCode;
	
	private String  activityType;
	
	private String  zone;
	
	private String  natureOfCase;
	
	private String  referral;
	
	private String  intervention;
	
	private String  natureOfCaseStatus;
	
	private String  majorTraining;

    private String volunteerType;
    
    private String contactPerson;
  
	private String natureOfPayment;
    
	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Integer getPersonIdPersonInCharge() {
		return personIdPersonInCharge;
	}

	public void setPersonIdPersonInCharge(Integer personIdPersonInCharge) {
		this.personIdPersonInCharge = personIdPersonInCharge;
	}

	public Integer getPersonIdBeneficiary() {
		return personIdBeneficiary;
	}

	public void setPersonIdBeneficiary(Integer personIdBeneficiary) {
		this.personIdBeneficiary = personIdBeneficiary;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getNatureOfCase() {
		return natureOfCase;
	}

	public void setNatureOfCase(String natureOfCase) {
		this.natureOfCase = natureOfCase;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getReferral() {
		return referral;
	}

	public void setReferral(String referral) {
		this.referral = referral;
	}

	public String getNatureOfCaseStatus() {
		return natureOfCaseStatus;
	}

	public void setNatureOfCaseStatus(String natureOfCaseStatus) {
		this.natureOfCaseStatus = natureOfCaseStatus;
	}

	public String getIntervention() {
		return intervention;
	}

	public void setIntervention(String intervention) {
		this.intervention = intervention;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMajorTraining() {
		return majorTraining;
	}

	public void setMajorTraining(String majorTraining) {
		this.majorTraining = majorTraining;
	}

	public String getVolunteerType() {
		return volunteerType;
	}

	public void setVolunteerType(String volunteerType) {
		this.volunteerType = volunteerType;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	  
    public String getNatureOfPayment() {
		return natureOfPayment;
	}

	public void setNatureOfPayment(String natureOfPayment) {
		this.natureOfPayment = natureOfPayment;
	}

}
