package org.springframework.samples.hibernate;

import java.util.List;

import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalPerson;
import org.springframework.samples.hibernate.beans.Login;
import org.springframework.samples.hibernate.beans.NatureOfCasePerson;
import org.springframework.samples.hibernate.beans.Person;
import org.springframework.samples.hibernate.beans.Payment;
import org.springframework.samples.hibernate.beans.PersonState;
import org.springframework.samples.hibernate.beans.ProjectPerson;
import org.springframework.samples.hibernate.beans.SupportGroup;
import org.springframework.samples.hibernate.beans.Zone;

public interface PaymentDao {
	
	
void save(Payment payment);

void update(Payment payment);

void merge(Payment payment);

void delete(Payment payment);

List<String> getPaymentsList(String projectCode);

List<Payment> findByFilter(Filter filter);

List<Payment> findById(Integer paymentId);

List<String> getPaymentStatus(String projectCode);


List<String> getNatureOfPaymentList(String projectCode);


	
	

}
