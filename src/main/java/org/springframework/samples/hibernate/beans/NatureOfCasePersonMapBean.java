package org.springframework.samples.hibernate.beans;

import java.util.Date;

public class NatureOfCasePersonMapBean {

	private Integer natureOfCasePersonId;
	
	private String beneficiary;
	
	private Date insertDate;
	
	private String status;
	
	private String natureOfCase;
	
	private String delayedMilestone;
	
	private String projectCode;
	
	
	public Integer getNatureOfCasePersonId() {
		return natureOfCasePersonId;
	}

	public void setNatureOfCasePersonId(Integer natureOfCasePersonId) {
		this.natureOfCasePersonId = natureOfCasePersonId;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public String getNatureOfCase() {
		return natureOfCase;
	}

	public void setNatureOfCase(String natureOfCase) {
		this.natureOfCase = natureOfCase;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getBeneficiary() {
		return beneficiary;
	}

	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}

	public String getDelayedMilestone() {
		return delayedMilestone;
	}

	public void setDelayedMilestone(String delayedMilestone) {
		this.delayedMilestone = delayedMilestone;
	}


	
}
