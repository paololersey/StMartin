package org.springframework.samples.hibernate.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="person_activity")
public class PersonActivity {

	private int personActivityId;
	
	private int personId;
	
	private int activityId;

	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "PERSON_ACTIVITY_ID")
	public int getPersonActivityId() {
		return personActivityId;
	}

	public void setPersonActivityId(int personActivityId) {
		this.personActivityId = personActivityId;
	}
	
	@NotNull
	@Column(name = "PERSON_ID")
	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	@NotNull
	@Column(name = "ACTIVITY_ID",insertable=false,updatable=false)
	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	
}
