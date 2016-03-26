package org.springframework.samples.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.NatureOfCasePerson;
import org.springframework.samples.hibernate.beans.Person;
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
			   sqlBeneficiaryFromCondition ="  JOIN person" +
			    " ON nature_of_case_person.PERSON_ID=person.PERSON_ID ";		   
			   sqlBeneficiaryWhereCondition = "AND person.PERSON_ID=:personIdBeneficiary ";		   
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
		   String sqlProjectCode="AND nature_of_case_person.PROJECT_CODE =:projectCode ";
		   if(projectCode!=null && projectCode.equals("DIRE")){
				 sqlProjectCode="";
	       }

		String sql="SELECT nature_of_case_person.* FROM nature_of_case_person "
		        + sqlBeneficiaryFromCondition
		        + "WHERE 1=1 "
		        + sqlBeneficiaryWhereCondition
		        + sqlNatureOfCaseCondition
				+ sqlDateStartCondition 	
	         	+ sqlDateEndCondition
	         	+ sqlNatureOfCaseStatusCondition
	         	+ sqlProjectCode;	
		
		SQLQuery query = instantiateQuery(sql);
		
		if(personIdBeneficiary!=null)query.setParameter("personIdBeneficiary", personIdBeneficiary);
		if (dateStart != null) query.setParameter("dateStart", dateStart);
		if (dateEnd != null) query.setParameter("dateEnd", dateEnd);	
		if (natureOfCase != null) query.setParameter("natureOfCase", natureOfCase);
		if (natureOfCaseStatus != null) query.setParameter("natureOfCaseStatus", natureOfCaseStatus);
		if(projectCode!=null && !projectCode.equals("DIRE"))query.setParameter("projectCode", projectCode);
		
		return query.list();
		
	}

	@Override
	public List<String> getNatureOfCasesList(String projectCode) {
		 String sqlProjectCode="AND PROJECT_CODE=:projectCode OR PROJECT_CODE is null ";
		  if(projectCode!=null && projectCode.equals("DIRE")){
			 sqlProjectCode="";
        }
		String sql="SELECT NATURE_OF_CASE FROM nature_of_case WHERE 1=1 "
				+ sqlProjectCode;
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		if(projectCode!=null && !projectCode.equals("DIRE"))query.setParameter("projectCode", projectCode);
		return query.list();
	}

	@Override
	public List<NatureOfCasePerson> findById(Integer natureOfCasePersonId) {
		String sql = "SELECT * FROM nature_of_case_person WHERE NATURE_OF_CASE_PERSON_ID=:idString";
		SQLQuery query = instantiateQuery(sql);
		query.setParameter("idString", natureOfCasePersonId);
		return query.list();
	}

	@Override
	public List<String> getNatureOfCaseStatus(String projectCode) {
		String sqlProjectCode="AND PROJECT_CODE=:projectCode OR PROJECT_CODE is null ";
		if(projectCode!=null && projectCode.equals("DIRE")){
		  sqlProjectCode="";
		}
	
		String sql="SELECT nature_of_case_status FROM nature_of_case_status WHERE 1=1 "
				+ sqlProjectCode;
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		if(projectCode!=null && !projectCode.equals("DIRE"))query.setParameter("projectCode", projectCode);
		return query.list();
	}

	

	
}
