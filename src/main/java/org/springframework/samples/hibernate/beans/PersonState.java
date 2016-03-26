package org.springframework.samples.hibernate.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="person_state")
public class PersonState {

	private int personStateId;
	
	private String personState;

	
	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "PERSON_STATE_ID")
	public int getpersonStateId() {
		return personStateId;
	}

	public void setpersonStateId(int personStateId) {
		this.personStateId = personStateId;
	}
	
	@Column(name = "PERSON_STATE")
	public String getpersonState() {
		return personState;
	}
	
	public void setpersonState(String personState) {
		this.personState = personState;
	}

}

