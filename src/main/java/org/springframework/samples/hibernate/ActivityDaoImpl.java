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
		String sql = "SELECT * FROM activity WHERE ACTIVITY_ID=:idString";
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
		   String projectCode = filterActivity.getProjectCode();
		   
		   String sqlDateStartCondition = "";
		   String sqlDateEndCondition = "";
		   String sqlPersonInChargeFromCondition = "";
		   String sqlPersonInChargeWhereCondition = "";
		   String sqlBeneficiaryFromCondition = "";
		   String sqlBeneficiaryWhereCondition = "";
		   String sqlActivityTypeCondition="";
		   String sqlReferralCondition = "";
           String sqlInterventionCondition ="";
		   String sqlBeneficiarySelect = "";
		   String sqlProjectCode="";
		   
		   if(personIdBeneficiary!=null){
			   sqlBeneficiarySelect = ",p1.* ";
			   sqlBeneficiaryFromCondition =" JOIN person_activity pa1 " +
			    " ON pa1.ACTIVITY_ID=activity.ACTIVITY_ID JOIN person p1 " +
			    " ON	pa1.PERSON_ID=p1.PERSON_ID ";
			   
			   sqlBeneficiaryWhereCondition = "AND p1.PERSON_ID=:personIdBeneficiary ";
			   
		   }
		   if(personIdPersonIncharge!=null){			 			   
			   sqlPersonInChargeFromCondition =" JOIN person_activity pa2 " +
					    " ON pa2.ACTIVITY_ID=activity.ACTIVITY_ID JOIN person p2 " +
					    " ON	pa2.PERSON_ID=p2.PERSON_ID ";
					   
			   sqlPersonInChargeWhereCondition = "AND p2.PERSON_ID=:personIdPersonIncharge ";
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
		   if (projectCode!=null && !projectCode.equals("DIRE")){
			   sqlProjectCode="AND activity.PROJECT_CODE=:projectCode ";
		   }

		String sql="SELECT activity.*,note.* "
			 	+ sqlBeneficiarySelect 
				+ "FROM activity LEFT JOIN note " 
	            + "ON activity.ACTIVITY_ID =note.ACTIVITY_ID " 
		        + sqlPersonInChargeFromCondition 
		        + sqlBeneficiaryFromCondition
		        + "WHERE 1=1 "
		        + sqlPersonInChargeWhereCondition
		        + sqlBeneficiaryWhereCondition
		        + sqlActivityTypeCondition
		        + sqlReferralCondition
				+ sqlDateStartCondition 	
	         	+ sqlDateEndCondition
	         	+ sqlInterventionCondition
	         	+ sqlProjectCode
	         	+ "ORDER BY ACTIVITY_DATE";	
		
		SQLQuery query = instantiateQuery(sql);
		query.addEntity(Note.class);
		if(personIdPersonIncharge!=null) query.setParameter("personIdPersonIncharge", personIdPersonIncharge);
		if(personIdBeneficiary!=null)query.setParameter("personIdBeneficiary", personIdBeneficiary);
		if (activityDateStart != null) query.setParameter("activityDateStart", activityDateStart);
		if (activityDateEnd != null) query.setParameter("activityDateEnd", activityDateEnd);	
		if (activityType != null) query.setParameter("activityType", activityType);
		if (referral != null) query.setParameter("referral", referral);
		if (intervention != null) query.setParameter("intervention", intervention);
		if (projectCode!=null && !projectCode.equals("DIRE")) query.setParameter("projectCode", projectCode);
		
		return query.list();
		
	}


	@Override
	public List<String> getReferralList(String projectCode) {
		String sqlProjectCode="AND (PROJECT_CODE=:projectCode OR PROJECT_CODE IS NULL) ";
		if(projectCode!=null && projectCode.equals("DIRE")){
			 sqlProjectCode="";
		 }
		String sql="SELECT REFERRAL_TYPE FROM referral_type WHERE 1=1 "
				+ sqlProjectCode
				+ "ORDER BY referral_type.REFERRAL_TYPE asc";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		if(projectCode!=null && !projectCode.equals("DIRE"))query.setParameter("projectCode", projectCode);
		return query.list();
	
	}

	

	@Override
	public List<Note> getNotesList(int activityId) {
		String sql="SELECT * FROM note WHERE ACTIVITY_ID=:activityId";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("activityId", activityId);
		query.addEntity(Note.class);
		return query.list();
	}




	@Override
	public List<String> getActivityTypeList(String projectCode) {
		String sqlProjectCode="AND (PROJECT_CODE=:projectCode OR PROJECT_CODE IS NULL) ";
		if(projectCode!=null && projectCode.equals("DIRE")){
			 sqlProjectCode="";
		 }
		String sql="SELECT ACTIVITY_TYPE FROM activity_type WHERE 1=1 "
				+ sqlProjectCode
				+ "ORDER BY activity_type.ACTIVITY_TYPE asc";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		if(projectCode!=null && !projectCode.equals("DIRE"))query.setParameter("projectCode", projectCode);
		return query.list();
	}
	
	@Override
	public List<String> getInterventionList(String projectCode) {
		String sqlProjectCode="AND (PROJECT_CODE=:projectCode OR PROJECT_CODE IS NULL) ";
		 if(projectCode!=null && projectCode.equals("DIRE")){
			 sqlProjectCode="";
		 }
		String sql="SELECT intervention FROM intervention "
				+ "WHERE 1=1 "
				+ sqlProjectCode
				+ "ORDER BY intervention.INTERVENTION asc";
		SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);
		if(projectCode!=null && !projectCode.equals("DIRE"))query.setParameter("projectCode", projectCode);
		return query.list();
	}




	@Override
	public String getBeneficiaryNeededForActivityType(Filter filter) {
		 String activityType = filter.getActivityType();
		 String projectCode = filter.getProjectCode();
		 String sqlProjectCode="AND (PROJECT_CODE=:projectCode OR PROJECT_CODE IS NULL) ";
		 if(projectCode!=null && projectCode.equals("DIRE")){
			 sqlProjectCode="";
		 }
		 String sql = " SELECT NEEDS_BENEFICIARY FROM activity_type "
		 	      	+ " WHERE activity_type=:activityType "
		 	      	+ sqlProjectCode;
		 SQLQuery query =  this.sessionFactory.getCurrentSession().createSQLQuery(sql);			
		 query.setParameter("activityType", activityType);
		 query.setParameter("projectCode", projectCode);		 
		return (String) query.uniqueResult();
	}



	
	


	
}
