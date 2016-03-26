package org.springframework.samples.hibernate;

import java.util.List;

import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalActivity;
import org.springframework.samples.hibernate.beans.NatureOfCasePerson;
import org.springframework.samples.hibernate.beans.NatureOfCasePersonMapBean;
import org.springframework.samples.hibernate.beans.Person;

public interface NatureOfCasePersonDao {
	
	void save(NatureOfCasePerson NatureOfCase);
	
	void update(NatureOfCasePerson NatureOfCase);
	
	void merge(NatureOfCasePerson NatureOfCase);
	
	void delete(NatureOfCasePerson NatureOfCase);

	List<String> getNatureOfCasesList(String projectCode);

	List<NatureOfCasePerson> findByFilter(Filter filter);

	List<NatureOfCasePerson> findById(Integer natureOfCasePersonId);

	List<String> getNatureOfCaseStatus(String projectCode);

	
}
