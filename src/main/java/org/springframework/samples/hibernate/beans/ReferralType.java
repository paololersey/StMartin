package org.springframework.samples.hibernate.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="referral_type")
public class ReferralType {

	private Integer referralTypeId;
	
	private String referralType;


	@NotNull
	@Id
	@GeneratedValue
	@Column(name = "REFERRAL_TYPE_ID")
	public Integer getReferralTypeId() {
		return referralTypeId;
	}

	public void setReferralTypeId(Integer referralTypeId) {
		this.referralTypeId = referralTypeId;
	}
	@NotNull
	@Column(name = "REFERRAL_TYPE")
	public String getReferralType() {
		return referralType;
	}

	public void setReferralType(String referralType) {
		this.referralType = referralType;
	}
	
	


	
}
