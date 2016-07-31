package org.springframework.samples.hibernate.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="parish")
public class Parish {

	private int parishId;
	
	private String parishCode;
	
	private String parishName;
	
	private String projectCode;

	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "PARISH_ID")
	public int getParishId() {
		return parishId;
	}

	public void setParishId(int parishId) {
		this.parishId = parishId;
	}
	
	@NotNull
	@Column(name = "PARISH_CODE")
	public String getParishCode() {
		return parishCode;
	}

	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}

	@Column(name = "PARISH_NAME")
	public String getParishName() {
		return parishName;
	}

	public void setParishName(String parishName) {
		this.parishName = parishName;
	}
	
	@Column(name = "PROJECT_CODE")
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	
}

