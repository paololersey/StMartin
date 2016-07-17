package org.springframework.samples.hibernate.beans;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="payment")
public class Payment {
     private Integer paymentId;
     private Integer personId;
     private BigDecimal amount;
     private String chequeNumber ;
     private String receiptNumber;
     private Date paymentDate;
     private String natureOfPayment;
     private String projectCode;
     private String status;
     
	
    @NotNull
 	@Id
 	@GeneratedValue
 	@Column(name = "PAYMENT_ID")
 	public Integer getPaymentId() {
 		return paymentId;
 	}
 	public void setPaymentId(Integer paymentId) {
 		this.paymentId = paymentId;
 	}
 	@NotNull
	@Column(name = "PERSON_ID")
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	
	@Column(name = "AMOUNT")
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Column(name = "CHEQUE_NUMBER")
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	@Column(name = "RECEIPT_NUMBER")
	public String getReceiptNumber() {
		return receiptNumber;
	}
	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}
	@Column(name = "PAYMENT_DATE")
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	@Column(name = "NATURE_OF_PAYMENT")
	public String getNatureOfPayment() {
		return natureOfPayment;
	}
	public void setNatureOfPayment(String natureOfPayment) {
		this.natureOfPayment = natureOfPayment;
	}
	@Column(name = "PROJECT_CODE")
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
     
}
