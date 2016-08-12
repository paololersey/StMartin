package org.springframework.samples.hibernate.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="nature_of_case_person")
public class NatureOfCasePerson {

	private Integer natureOfCasePersonId;
	
	private Integer personId;
	
	private Date insertDate;
	
	private String status;
	
	private String natureOfCase;
	
	private String delayedMilestone;
	
	private String projectCode;
	
	
	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "NATURE_OF_CASE_PERSON_ID")
	public Integer getNatureOfCasePersonId() {
		return natureOfCasePersonId;
	}

	public void setNatureOfCasePersonId(Integer natureOfCasePersonId) {
		this.natureOfCasePersonId = natureOfCasePersonId;
	}



	@NotNull
	@Column(name = "PERSON_ID")
	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}	
	
	@Column(name = "INSERT_DATE")
	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	@Column(name = "NATURE_OF_CASE")
	public String getNatureOfCase() {
		return natureOfCase;
	}

	public void setNatureOfCase(String natureOfCase) {
		this.natureOfCase = natureOfCase;
	}
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "PROJECT_CODE")
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	@Column(name = "DELAYED_MILESTONE")
	public String getDelayedMilestone() {
		return delayedMilestone;
	}

	public void setDelayedMilestone(String delayedMilestone) {
		this.delayedMilestone = delayedMilestone;
	}


	
}
