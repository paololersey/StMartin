package org.springframework.samples.hibernate.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;

@Entity
@Table(name="project_person")
public class ProjectPerson {

	private int projectPersonId;

	private int personId;
	
	private int projectId;

	private String personCode;

	private String projectCode;


	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "PROJECT_PERSON_ID")
	public int getProjectPersonId() {
		return this.projectPersonId;
	}

	public void setProjectPersonId(int projectPersonId) {
		this.projectPersonId = projectPersonId;
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
	@Column(name = "PERSON_CODE")
	public String getPersonCode() {
		return personCode;
	}

	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}

	@NotNull
	@Column(name = "PROJECT_ID")
	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	@NotNull
	@Column(name = "PROJECT_CODE")
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

}
