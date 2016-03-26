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
@Table(name="activity")
public class Activity {

	private Integer activityId;
	
	private String activityType;
	
	private Date activityDate;
	
	private String referral;
	
	private String intervention;
	
	private Integer attendingPeople;
	
	private String levelChange;
	
	private String levelOfImprovement;
	
	private String projectCode;
	
	private String applianceSource;

	private Set<PersonActivity> personActivities=new HashSet<PersonActivity>();
	
	
	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "ACTIVITY_ID")
	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	@NotNull
	@Column(name = "ACTIVITY_TYPE")
	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	
	@Column(name = "REFERRAL")
	public String getReferral() {
		return referral;
	}

	public void setReferral(String referral) {
		this.referral = referral;
	}
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="ACTIVITY_ID",nullable=false)
	public Set<PersonActivity> getPersonActivities() {
		return personActivities;
	}
	
	public void setPersonActivities(Set<PersonActivity> personActivities) {
		this.personActivities = personActivities;
	}

	@NotNull
	@Column(name = "ACTIVITY_DATE")
	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}
	
	@Column(name = "INTERVENTION")
	public String getIntervention() {
		return intervention;
	}

	public void setIntervention(String intervention) {
		this.intervention = intervention;
	}
	
	@Column(name = "ATTENDING_PEOPLE")
	public Integer getAttendingPeople() {
		return attendingPeople;
	}

	public void setAttendingPeople(Integer attendingPeople) {
		this.attendingPeople = attendingPeople;
	}
	@Column(name = "LEVEL_CHANGE")
	public String getLevelChange() {
		return levelChange;
	}

	public void setLevelChange(String levelChange) {
		this.levelChange = levelChange;
	}

	
	@Column(name = "PROJECT_CODE")
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
    
	@Column(name = "APPLIANCE_SOURCE")
	public String getApplianceSource() {
		return applianceSource;
	}

	public void setApplianceSource(String applianceSource) {
		this.applianceSource = applianceSource;
	}

	@Column(name = "LEVEL_OF_IMPROVEMENT")
	public String getLevelOfImprovement() {
		return levelOfImprovement;
	}

	public void setLevelOfImprovement(String levelOfImprovement) {
		this.levelOfImprovement = levelOfImprovement;
	}
	
	/*@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "PERSON_ACTIVITY", 
	           joinColumns = {@JoinColumn(name="ACTIVITY_ID", referencedColumnName="ACTIVITY_ID")}, 
	           inverseJoinColumns={@JoinColumn(name="PERSON_ID", referencedColumnName="PERSON_ID")})	
	public List<Person> getPeople() {
		return people;
	}

	public void setPeople(List<Person> people) {
		this.people = people;
	}*/

	
}
