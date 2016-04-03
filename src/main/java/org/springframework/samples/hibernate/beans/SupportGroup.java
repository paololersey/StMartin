package org.springframework.samples.hibernate.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="support_group")
public class SupportGroup {

	private int supportGroupId;
	
	private String supportGroupCode;
	
	private String supportGroupName;
	
	private String projectCode;

	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "SUPPORT_GROUP_ID")
	public int getSupportGroupId() {
		return supportGroupId;
	}

	public void setSupportGroupId(int supportGroupId) {
		this.supportGroupId = supportGroupId;
	}
	
	@NotNull
	@Column(name = "SUPPORT_GROUP_CODE")
	public String getSupportGroupCode() {
		return supportGroupCode;
	}

	public void setSupportGroupCode(String supportGroupCode) {
		this.supportGroupCode = supportGroupCode;
	}

	@Column(name = "SUPPORT_GROUP_NAME")
	public String getSupportGroupName() {
		return supportGroupName;
	}

	public void setSupportGroupName(String supportGroupName) {
		this.supportGroupName = supportGroupName;
	}
	
	@Column(name = "PROJECT_CODE")
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	
}

