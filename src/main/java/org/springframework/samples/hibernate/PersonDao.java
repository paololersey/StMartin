package org.springframework.samples.hibernate;

import java.util.List;

import org.springframework.samples.hibernate.beans.DelayedMilestone;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalPerson;
import org.springframework.samples.hibernate.beans.Login;
import org.springframework.samples.hibernate.beans.Parish;
import org.springframework.samples.hibernate.beans.Person;
import org.springframework.samples.hibernate.beans.ProjectPerson;
import org.springframework.samples.hibernate.beans.SupportGroup;
import org.springframework.samples.hibernate.beans.Zone;

public interface PersonDao {
	
	void save(Person person);
	
	void update(Person person);
	
	void merge(Person person);
	
	void delete(Person person);
	
	List<Person> findAll();

	List<Person> findByFirstName(String firstname);

	List<Person> findById(Integer id);
	
	List<Person> findByTypeAndProjectCode(String personType, String projectCode);

	List<ProjectPerson> findPersonCodeByPersonId(int personId);
	
	List<Person> findPersonByPersonId(int personId, String personCode);

	List<Person> findByFilter(Filter filter);

	List<String> getStatesList(String projectCode);

	List<Zone> getZonesList(String projectCode);

	List<String> getMajorTrainingList(String projectCode);
	
	String checkFileNumber(GlobalPerson globalPerson);

	String checkNames(GlobalPerson globalPerson);

	List<String> getVillagesList(String projectCode);

	List<String> getVolunteerTypeList(String projectCode);

	List<String> getNatureOfCaseByPersonId(Person person);

	List<SupportGroup> getSupportGroupList(String projectCode);

	void saveLogin(Login login);

	void mergeLogin(Login login);

	List<Login> getLogin(Login login);

	List<Parish> getParishesList(String projectCode);

	List<DelayedMilestone> getDelayedMilestoneList(String projectCode);

}
