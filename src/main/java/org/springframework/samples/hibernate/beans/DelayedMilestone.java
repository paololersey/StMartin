package org.springframework.samples.hibernate.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="delayed_milestone")
public class DelayedMilestone {

	private int delayedMilestoneId;
	
	private String delayedMilestone;
	
	private String projectCode;

	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "DELAYED_MILESTONE_ID")
	public int getDelayedMilestoneId() {
		return delayedMilestoneId;
	}

	public void setDelayedMilestoneId(int delayedMilestoneId) {
		this.delayedMilestoneId = delayedMilestoneId;
	}
	
	@NotNull
	@Column(name = "DELAYED_MILESTONE")
	public String getDelayedMilestone() {
		return delayedMilestone;
	}

	public void setDelayedMilestone(String delayedMilestone) {
		this.delayedMilestone = delayedMilestone;
	}

	@Column(name = "PROJECT_CODE")
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	
}

