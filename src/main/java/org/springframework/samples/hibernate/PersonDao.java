package org.springframework.samples.hibernate;

import java.util.List;

import org.springframework.samples.hibernate.beans.City;
import org.springframework.samples.hibernate.beans.Person;

public interface PersonDao {
	
	void save(Person person);
	
	void update(Person person);
	
	void merge(Person person);
	
	void delete(Person person);
	
	List<Person> findAll();

	List<Person> findByFirstName(String firstname);

	List<Person> findByLastName(String lastname);

	List<Person> findById(Long id);
	
	List<Person> findByTypeAndProjectCode(String personType, String projectCode);

	List<String> getCitiesList();

	
}
