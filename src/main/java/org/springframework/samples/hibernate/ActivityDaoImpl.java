package org.springframework.samples.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.hibernate.beans.Activity;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalActivity;
import org.springframework.samples.hibernate.beans.Note;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Repository
public class ActivityDaoImpl implements ActivityDao {

	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	public ActivityDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}



	
	// CRUD methods
	public void save(Activity activity) {
		this.sessionFactory.getCurrentSession().save(activity);
	}

	public void update(Activity activity) {
		this.sessionFactory.getCurrentSession().update(activity);
	}
	
	public void merge(Activity activity) {
		this.sessionFactory.getCurrentSession().merge(activity);
	}

	public void delete(Activity activity) {
		this.sessionFactory.getCurrentSession().delete(activity);
	}
	
	public void save(Note note) {
		this.sessionFactory.getCurrentSession().save(note);
	}
	
	public void delete(Note note) {
		this.sessionFactory.getCurrentSession().delete(note);
	}

	private SQLQuery instantiateQuery(String sql) {
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.addEntity(Activity.class);
		return query;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> findById(Integer id) {
		String idString = id.toString();
		String sql = "SELECT * FROM ACTIVITY WHERE ACTIVITY_ID=:idString";
		SQLQuery query = instantiateQuery(sql);
		query.setParameter("idString", id);
		return query.list();

	}

	
	
	@Override
	public List<GlobalActivity> findByFilterActivity(Filter filterActivity) {		
		
		   Integer personIdPersonIncharge =filterActivity.getPersonIdPersonInCharge();
		   Integer personIdBeneficiary =filterActivity.getPersonIdBeneficiary();
		   Date activityDateStart = filterActivity.getDateStart();		  
		   Date activityDateEnd = filterActivity.getDateEnd();
		   String activityType = filterActivity.getActivityType();
		   String referral = filterActivity.getReferral();
		   String intervention = filterActivity.getIntervention();
		   
		   String sqlDateStartCondition = "";
		   String sqlDateEndCondition = "";
		   String sqlPersonInChargeFromCondition = "";
		   String sqlPersonInChargeWhereCondition = "";
		   String sqlBeneficiaryFromCondition = "";
		   String sqlBeneficiaryWhereCondition = "";
		   String sqlActivityTypeCondition="";
		   String sqlReferralCondition = "";
           String sqlInterventionCondition ="";
		   
		   if(personIdBeneficiary!=null){			                                    
			   sqlBeneficiaryFromCondition =" JOIN PERSON_ACTIVITY PA1 " +
			    " ON PA1.ACTIVITY_ID=ACTIVITY.ACTIVITY_ID JOIN PERSON P1 " +
			    " ON	PA1.PERSON_ID=P1.PERSON_ID ";
			   
			   sqlBeneficiaryWhereCondition = "AND P1.PERSON_ID=:personIdBeneficiary ";
			   
		   }
		   if(personIdPersonIncharge!=null){			 			   
			   sqlPersonInChargeFromCondition =" JOIN PERSON_ACTIVITY PA2 " +
					    " ON PA2.ACTIVITY_ID=ACTIVITY.ACTIVITY_ID JOIN PERSON P2 " +
					    " ON	PA2.PERSON_ID=P2.PERSON_ID ";
					   
			   sqlPersonInChargeWhereCondition = "AND P2.PERSON_ID=:personIdPersonIncharge ";
		   }
		   
		   if (activityDateStart != null) {
				sqlDateStartCondition = "AND ACTIVITY_DATE>=:activityDateStart ";
		   } 			   
		   if (activityDateEnd != null) {
				sqlDateEndCondition = "AND ACTIVITY_DATE<:activityDateEnd ";
		   };		   
		   if (activityType != null) {
				sqlActivityTypeCondition = "AND ACTIVITY_TYPE =:activityType ";
		   };
		   if (referral != null) {
			   sqlReferralCondition = "AND REFERRAL =:referral ";
		   }
		   if (intervention != null) {
			   sqlInterventionCondition = "AND INTERVENTION =:intervention ";
		   }

		String sql="SELECT ACTIVITY.*,NOTE.* FROM ACTIVITY LEFT JOIN NOTE " 
	            + "ON ACTIVITY.ACTIVITY_ID =NOTE.ACTIVITY_ID " 
		        + sqlPersonInChargeFromCondition 
		        + sqlBeneficiaryFromCondition
		        + "WHERE 1=1 "
		        + sqlPersonInChargeWhereCondition
		        + sqlBeneficiaryWhereCondition
		        + sqlActivityTypeCondition
		        + sqlReferralCondition
				+ sqlDateStartCondition 	
	         	+ sqlDateEndCondition
	         	+ sqlInterventionCondition;	
		
		SQLQuery query = instantiateQuery(sql);
		query.addEntity(Note.class);
		if(personIdPersonIncharge!=null) query.setParameter("personIdPersonIncharge", personIdPersonIncharge);
		if(personIdBeneficiary!=null)query.setParameter("personIdBeneficiary", personIdBeneficiary);
		if (activityDateStart != null) query.setParameter("activityDateStart", activityDateStart);
		if (activityDateEnd != null) query.setParameter("activityDateEnd", activityDateEnd);	
		if (activityType != null) query.setParameter("activityType", activityType);
		if (referral != null) query.setParameter("referral", referral);
		if (intervention != null) query.setParameter("intervention", intervention);
		
		return query.list();
		
	}


	@Override
	public List<String> getReferralList(String projectCode) {
		String sql="SELECT REFERRAL_TYPE FROM REFERRAL_TYPE WHERE PROJECT_CODE=:projectCode OR PROJECT_CODE is null";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("projectCode", projectCode);
		return query.list();
	
	}

	

	@Override
	public List<Note> getNotesList(int activityId) {
		String sql="SELECT * FROM NOTE WHERE ACTIVITY_ID=:activityId";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("activityId", activityId);
		query.addEntity(Note.class);
		return query.list();
	}




	@Override
	public List<String> getActivityTypeList(String projectCode) {
		String sql="SELECT ACTIVITY_TYPE FROM ACTIVITY_TYPE WHERE PROJECT_CODE=:projectCode OR PROJECT_CODE is null";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("projectCode", projectCode);
		return query.list();
	}
	
	@Override
	public List<String> getInterventionList(String projectCode) {
		String sql="SELECT INTERVENTION FROM INTERVENTION WHERE PROJECT_CODE=:projectCode OR PROJECT_CODE is null";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("projectCode", projectCode);
		return query.list();
	}



	
	


	
}
