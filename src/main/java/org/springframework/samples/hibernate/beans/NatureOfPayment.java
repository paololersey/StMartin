package org.springframework.samples.hibernate.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "nature_of_payment")
public class NatureOfPayment {

	private Integer natureOfPaymentId;

	private String natureOfPayment;

	private String projectId;

	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "NATURE_OF_PAYMENT_ID")
	public Integer getNatureOfPaymentId() {
		return natureOfPaymentId;
	}

	public void setNatureOfPaymentId(Integer natureOfPaymentId) {
		this.natureOfPaymentId = natureOfPaymentId;
	}

	@Column(name = "NATURE_OF_PAYMENT")
	public String getNatureOfPayment() {
		return natureOfPayment;
	}

	public void setNatureOfPayment(String natureOfPayment) {
		this.natureOfPayment = natureOfPayment;
	}

	@Column(name = "PROJECT_ID")
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

}
