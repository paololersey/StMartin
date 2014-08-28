package org.springframework.samples.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalPerson;
import org.springframework.samples.hibernate.beans.Login;
import org.springframework.samples.hibernate.beans.Person;
import org.springframework.samples.hibernate.beans.PersonState;
import org.springframework.samples.hibernate.beans.ProjectPerson;
import org.springframework.samples.hibernate.beans.Zone;
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
	public List<Person> findById(Integer id) {
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
	public List<String> getVillagesList(String projectCode) {
		String sql="SELECT VILLAGE FROM VILLAGE WHERE PROJECT_CODE=:projectCode OR PROJECT_CODE is null ORDER BY VILLAGE.VILLAGE asc";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("projectCode", projectCode);
		return query.list();
	
	}
	
	@Override
	public List<Zone> getZonesList(String projectCode) {	
		String sql="SELECT * FROM ZONE WHERE PROJECT_CODE=:projectCode OR PROJECT_CODE IS NULL";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("projectCode", projectCode);
		query.addEntity(Zone.class);
		return query.list();
	}
	

	


	static {
		citiesList=new ArrayList<String>();
		citiesList.add("Nyahururu");
		citiesList.add("Nakuru");		
	}

	@Override
	public List<Person> findPersonByPersonId(int personId, String personCode) {	
		String sql="SELECT PERSON.* FROM PERSON, PROJECT_PERSON "
                   +"WHERE PERSON.PERSON_ID=:personId "
                   +"AND PROJECT_PERSON.PERSON_ID = PERSON.PERSON_ID "
                   + "AND PERSON_CODE=:personCode ";			      
		SQLQuery query = instantiateQuery(sql);
		query.setParameter("personId", personId);
		query.setParameter("personCode", personCode);
		return (List<Person>) query.list();
	}
	
	@Override
	public List<ProjectPerson> findPersonCodeByPersonId(int personId) {	
		String sql="SELECT PROJECT_PERSON.* FROM PROJECT_PERSON "
                   +"WHERE PROJECT_PERSON.PERSON_ID=:personId ";			      
		SQLQuery query = instantiateQuery(sql);
		query.setParameter("personId", personId);
		return query.list();
	}



	@Override
	public List<Person> findByFilter(Filter filter) {
		   Integer personIdPersonIncharge =filter.getPersonIdPersonInCharge();
		   Date dateStart = filter.getDateStart();		  
		   Date dateEnd = filter.getDateEnd();
		   String status = filter.getStatus();
		   String personType = filter.getPersonType();
		   String projectCode = filter.getProjectCode();
		   String zone = filter.getZone();
		   String majorTraining = filter.getMajorTraining();
		   String volunteerType = filter.getVolunteerType();
		   String contactPerson = filter.getContactPerson();
		   
		   String sqlActivityFromCondition = "";
		   String sqlActivityWhereCondition = "";
		   String sqlDateStartCondition = "";
		   String sqlDateEndCondition = "";
		   String sqlPersonInChargeFromCondition = "";
		   String sqlPersonInChargeWhereCondition = "";
		   String sqlActivePersonCondition = "";
		   String sqlZoneEndCondition= "";
		   String sqlStatusCondition = "";
		   String sqlMajorTrainingCondition = "";
		   String sqlvolunteerTypeCondition = "";
		   String sqlContactPersonCondition ="";
		   
		   if (dateStart != null || dateEnd!= null || personIdPersonIncharge!=null) {
			    sqlActivityFromCondition = ", PERSON_ACTIVITY PBEN, ACTIVITY A ";
			    sqlActivityWhereCondition = "AND   PBEN.ACTIVITY_ID = A.ACTIVITY_ID "
			    		                  +  "AND   BEN.PERSON_ID=PBEN.PERSON_ID ";
		   }
		   
		   if(personIdPersonIncharge!=null){
			   sqlPersonInChargeFromCondition = ", PERSON_ACTIVITY PCH ";
			   sqlPersonInChargeWhereCondition = "AND   A.ACTIVITY_ID = PCH.ACTIVITY_ID  "	  
					                           + "AND   PCH.PERSON_ID =:personIdPersonIncharge ";
		   }
		   
		   if (dateStart != null) {
				sqlDateStartCondition = "AND ACTIVITY_DATE>=:dateStart ";				
		   } 	   			   
		   if (dateEnd != null) {
				sqlDateEndCondition = "AND ACTIVITY_DATE<=:dateEnd ";
		   };
		   if (zone != null) {
				sqlZoneEndCondition = "AND ZONE=:zone ";
		   };
		   if (status != null) {
			   sqlStatusCondition = "AND STATE=:status ";
		   };
		   if(majorTraining!=null){
			   sqlMajorTrainingCondition = "AND MAJOR_TRAINING=:majorTraining ";
		   }
		   if(volunteerType!=null){
			   sqlvolunteerTypeCondition= "AND VOLUNTEER_TYPE=:volunteerType ";
		   }
		   if(contactPerson!=null){
			   sqlContactPersonCondition  = "AND CONTACT_PERSON =: contactPerson ";
		   }
		   
		   String sql= null;
           if("BE".equals(personType)){
	 	          sql="SELECT DISTINCT BEN.* FROM PERSON BEN, PROJECT_PERSON PP "
	 	          + sqlActivityFromCondition
	 	  		  + sqlPersonInChargeFromCondition
	 	  		  + "WHERE BEN.PERSON_ID = PP.PERSON_ID "
	 	  		  + "AND   PP.PERSON_CODE = 'BE' "
	  		  
	 	  		  + "AND   PROJECT_CODE=:projectCode "
	 	  		  + sqlActivityWhereCondition
	 	  		  + sqlActivePersonCondition
	 	  		  + sqlPersonInChargeWhereCondition
	 	  	      + sqlDateStartCondition
	              + sqlDateEndCondition 
	              + sqlZoneEndCondition
	              + sqlStatusCondition
	              + sqlContactPersonCondition;	
           }
           else{
        	   sql="SELECT DISTINCT PERSON.* FROM PERSON, PROJECT_PERSON PP "
        	   		+ "WHERE PERSON.PERSON_ID = PP.PERSON_ID "
        			+ "AND PERSON_CODE=:personType "
        			+ "AND PROJECT_CODE=:projectCode "
        			+ sqlStatusCondition 
        			+ sqlMajorTrainingCondition
        			+ sqlvolunteerTypeCondition;
        			
           }
        
		SQLQuery query = instantiateQuery(sql);
		if (personType!=null && !"BE".equals(personType)) query.setParameter("personType", personType);
		if (personIdPersonIncharge!=null) query.setParameter("personIdPersonIncharge", personIdPersonIncharge);
		if (dateStart != null) query.setParameter("dateStart", dateStart);
		if (dateEnd != null) query.setParameter("dateEnd", dateEnd);			
		if (zone != null) query.setParameter("zone", zone);
		if (projectCode != null) query.setParameter("projectCode", projectCode);
		if (status!=null) query.setParameter("status", status);
		if (majorTraining!=null) query.setParameter("majorTraining", majorTraining);
		if (volunteerType!=null) query.setParameter("volunteerType",volunteerType);
		if (contactPerson!=null) query.setParameter("contactPerson",contactPerson);
		
		return query.list();
	}



	@Override
	public String credentials(Login login) {
		String username= login.getUsername();
		String password=login.getPassword();		
		String sql="SELECT DEPARTMENT FROM LOGIN "
                +"WHERE USERNAME=:username "
                +"AND PASSWORD=:password ";			      
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("username", username);
		query.setParameter("password", password);
		return (String)query.uniqueResult();
	}



	



	@Override
	public List<String> getStatesList(String projectCode) {
		String sql="SELECT PERSON_STATE FROM PERSON_STATE WHERE PROJECT_CODE=:projectCode OR PROJECT_CODE IS NULL";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("projectCode", projectCode);
		return query.list();
	}

	

	@Override
	public List<String> getMajorTrainingList(String projectCode) {
		String sql="SELECT MAJOR_TRAINING FROM MAJOR_TRAINING WHERE PROJECT_CODE=:projectCode OR PROJECT_CODE IS NULL";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("projectCode", projectCode);
		return query.list();
	}
	
	@Override
	public List<String> getVolunteerTypeList(String projectCode) {
		String sql="SELECT VOLUNTEER_TYPE FROM VOLUNTEER_TYPE WHERE PROJECT_CODE=:projectCode OR PROJECT_CODE IS NULL";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("projectCode", projectCode);
		return query.list();
	}


	@Override
	public String checkFileNumber(GlobalPerson globalPerson) {
		String projectCode = globalPerson.getProjectPerson().getProjectCode();
		String fileNumber = globalPerson.getPerson().getFileNumber();
		String sql="SELECT FILE_NUMBER FROM PERSON, PROJECT_PERSON "
                +"WHERE FILE_NUMBER=:fileNumber "
                +"AND PERSON.PERSON_ID=PROJECT_PERSON.PERSON_ID "
                +"AND PROJECT_CODE=:projectCode ";		
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("fileNumber", fileNumber);
		query.setParameter("projectCode", projectCode);
		
		return (String) query.uniqueResult();
	}
	
	@Override
	public String checkNames(GlobalPerson globalPerson) {
		String firstName = globalPerson.getPerson().getFirstName();
		String lastName = globalPerson.getPerson().getLastName();
		String thirdName = globalPerson.getPerson().getThirdName();
		String village = globalPerson.getPerson().getVillage();
		String projectCode = globalPerson.getProjectPerson().getProjectCode();
		
		String sqlThirdNameCondition = "";
		
		// check 3 names
		if(thirdName!=null){
			sqlThirdNameCondition = "AND THIRDNAME =:thirdName ";
		
		
		String sql= "SELECT PERSON_ID FROM PERSON "
                + "WHERE FIRSTNAME=:firstName "
                + "AND LASTNAME =:lastName "
                + sqlThirdNameCondition;
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
	    query.setParameter("thirdName", thirdName);
	    Integer peId = (Integer)query.uniqueResult();
		
		if(peId!=null && peId!=0) {
			    sql= "SELECT * FROM PROJECT_PERSON "
	                + "WHERE PERSON_ID =:peId "
			    	+ "AND PROJECT_CODE =:projectCode";   
			query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		    query.setParameter("peId", peId);
		    query.setParameter("projectCode", projectCode);
		    String secondResult = (String)query.uniqueResult();
		    if(secondResult!=null && !"".equals(secondResult)) {	
		    	// The person is already registered within this program: error
		    	return "-1";
		    }
		    else{
		    	// The person is already registered but within another program
		    	return peId.toString();
		    }
		}
		
		}
		
		// if I haven't got the thirdName, I check 2 names and village
		String sql= "SELECT FIRSTNAME FROM PERSON "
                + "WHERE FIRSTNAME=:firstName "
                + "AND LASTNAME =:lastName "
                + "AND VILLAGE =:village ";
		SQLQuery query2 =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query2.setParameter("firstName", firstName);
		query2.setParameter("lastName", lastName);
		query2.setParameter("village", village);		
		String result = (String)query2.uniqueResult();
		if(result!=null && !"".equals(result)) return "-2";
		return null;
	}






	
}
