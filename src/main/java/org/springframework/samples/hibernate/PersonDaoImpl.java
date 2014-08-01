package org.springframework.samples.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.hibernate.PersonDao;
import org.springframework.samples.hibernate.beans.City;
import org.springframework.samples.hibernate.beans.Person;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Repository
public class PersonDaoImpl implements PersonDao {

	public static final List<String> citiesList;
	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	public PersonDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
	// CRUD methods
	public void save(Person person) {
		this.sessionFactory.getCurrentSession().save(person);
	}

	public void update(Person person) {
		this.sessionFactory.getCurrentSession().update(person);
	}
	
	public void merge(Person person) {
		this.sessionFactory.getCurrentSession().merge(person);
	}

	public void delete(Person person) {
		this.sessionFactory.getCurrentSession().delete(person);
	}

	@Override
	public List<Person> findByLastName(String firstname) {
		//TODO
		return null;
	}
	
	@Override
	public List<Person> findAll() {
		String sql = "SELECT * FROM PERSON";
		SQLQuery query = instantiateQuery(sql);
		return query.list();
	}



	private SQLQuery instantiateQuery(String sql) {
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addEntity(Person.class);
		return query;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> findById(Long id) {
		String idString = id.toString();
		String sql = "SELECT * FROM PERSON WHERE PERSON_ID=:idString";
		SQLQuery query = instantiateQuery(sql);
		query.setParameter("idString", id);
		return query.list();

	} 
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Person> findByFirstName(String firstname) {
		
		String sql = "SELECT * FROM PERSON WHERE FIRSTNAME=:firstname";
		SQLQuery query = instantiateQuery(sql);
		query.setParameter("firstname", firstname);
		return query.list();

	}



	@Override
	public List<Person> findByTypeAndProjectCode(String personType,
			String projectCode) {
		String sql="SELECT * FROM PERSON LEFT JOIN PROJECT_PERSON ON PERSON.PERSON_ID=PROJECT_PERSON.PERSON_ID WHERE PROJECT_CODE =:projectCode AND PERSON_CODE=:personType";
		SQLQuery query = instantiateQuery(sql);
		query.setParameter("personType", personType);
		query.setParameter("projectCode", projectCode);
		return query.list();
	
	}

	
	
	@Override
	public List<String> getCitiesList() {	
		return citiesList;		
	}

	static {
	/*	citiesList= new ArrayList<City>() {{
			add( new City() 
			{{ 
				id=0; codice="NY"; denominazione="Nyahururu"; cap="3111";				
			}});
			add( new City() 
			{{ 
				id=1; codice="NA"; denominazione="Nakuru"; cap="3111";				
			}});
		}};	*/
		citiesList=new ArrayList<String>();
		citiesList.add("Nyahururu");
		citiesList.add("Nakuru");
		
	}

}
