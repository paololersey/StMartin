package org.springframework.samples.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.NatureOfCasePerson;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Repository
public class NatureOfCasePersonDaoImpl implements NatureOfCasePersonDao {

	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	public NatureOfCasePersonDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// CRUD methods
	public void save(NatureOfCasePerson natureOfCasePerson) {
		this.sessionFactory.getCurrentSession().save(natureOfCasePerson);
	}

	public void update(NatureOfCasePerson natureOfCasePerson) {
		this.sessionFactory.getCurrentSession().update(natureOfCasePerson);
	}
	
	public void merge(NatureOfCasePerson natureOfCasePerson) {
		this.sessionFactory.getCurrentSession().merge(natureOfCasePerson);
	}

	public void delete(NatureOfCasePerson natureOfCasePerson) {
		this.sessionFactory.getCurrentSession().delete(natureOfCasePerson);
	}
	

	private SQLQuery instantiateQuery(String sql) {
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addEntity(NatureOfCasePerson.class);
		return query;
	}


	
	@Override
	public List<NatureOfCasePerson> findByFilter(Filter filter) {		
		
		   Integer personIdBeneficiary =filter.getPersonIdBeneficiary();
		   Date dateStart = filter.getDateStart();		  
		   Date dateEnd = filter.getDateEnd();
		   String natureOfCase = filter.getNatureOfCase();
		   String natureOfCaseStatus = filter.getNatureOfCaseStatus();
		   String projectCode = filter.getProjectCode();
		   
		   String sqlDateStartCondition = "";
		   String sqlDateEndCondition = "";
		   String sqlBeneficiaryFromCondition = "";
		   String sqlBeneficiaryWhereCondition = "";
		   String sqlNatureOfCaseCondition="";
		   String sqlNatureOfCaseStatusCondition="";
		   
		   if(personIdBeneficiary!=null){			                                    
			   sqlBeneficiaryFromCondition ="  JOIN PERSON" +
			    " ON NATURE_OF_CASE_PERSON.PERSON_ID=PERSON.PERSON_ID ";		   
			   sqlBeneficiaryWhereCondition = "AND PERSON.PERSON_ID=:personIdBeneficiary ";		   
		   }	   
		   
		   if (dateStart != null) {
				sqlDateStartCondition = "AND INSERT_DATE>=:dateStart ";
		   } 	   
			   
		   if (dateEnd != null) {
				sqlDateEndCondition = "AND INSERT_DATE<=:dateEnd ";
		   };
		   
		   if (natureOfCase != null) {
				sqlNatureOfCaseCondition = "AND NATURE_OF_CASE =:natureOfCase ";
		   };
		   
		   if (natureOfCaseStatus != null) {
			   sqlNatureOfCaseStatusCondition = "AND STATUS =:natureOfCaseStatus ";
		   };
		   

		String sql="SELECT NATURE_OF_CASE_PERSON.* FROM NATURE_OF_CASE_PERSON "
		        + sqlBeneficiaryFromCondition
		        + "WHERE 1=1 "
		        + sqlBeneficiaryWhereCondition
		        + sqlNatureOfCaseCondition
				+ sqlDateStartCondition 	
	         	+ sqlDateEndCondition
	         	+ sqlNatureOfCaseStatusCondition
	         	+ "AND NATURE_OF_CASE_PERSON.PROJECT_CODE =:projectCode ";	
		
		SQLQuery query = instantiateQuery(sql);
		
		if(personIdBeneficiary!=null)query.setParameter("personIdBeneficiary", personIdBeneficiary);
		if (dateStart != null) query.setParameter("dateStart", dateStart);
		if (dateEnd != null) query.setParameter("dateEnd", dateEnd);	
		if (natureOfCase != null) query.setParameter("natureOfCase", natureOfCase);
		if (natureOfCaseStatus != null) query.setParameter("natureOfCaseStatus", natureOfCaseStatus);
		if (projectCode != null) query.setParameter("projectCode", projectCode);
		
		return query.list();
		
	}

	@Override
	public List<String> getNatureOfCasesList(String projectCode) {
		String sql="SELECT NATURE_OF_CASE FROM NATURE_OF_CASE WHERE PROJECT_CODE=:projectCode OR PROJECT_CODE is null";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("projectCode", projectCode);
		return query.list();
	}

	@Override
	public List<NatureOfCasePerson> findById(Integer natureOfCasePersonId) {
		String sql = "SELECT * FROM NATURE_OF_CASE_PERSON WHERE NATURE_OF_CASE_PERSON_ID=:idString";
		SQLQuery query = instantiateQuery(sql);
		query.setParameter("idString", natureOfCasePersonId);
		return query.list();
	}

	@Override
	public List<String> getNatureOfCaseStatus(String projectCode) {
		String sql="SELECT NATURE_OF_CASE_STATUS FROM NATURE_OF_CASE_STATUS WHERE PROJECT_CODE=:projectCode OR PROJECT_CODE is null";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("projectCode", projectCode);
		return query.list();
	}


	
}
