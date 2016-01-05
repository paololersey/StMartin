package org.springframework.samples.hibernate;

import java.util.List;

import org.springframework.samples.hibernate.beans.Activity;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalActivity;
import org.springframework.samples.hibernate.beans.Note;
import org.springframework.samples.hibernate.beans.ReferralType;

public interface ActivityDao {
	
	void save(Activity activity);
	
	void update(Activity activity);
	
	void merge(Activity activity);
	
	void delete(Activity activity);
	
	void save(Note note);
	
	void delete(Note note);
	
	List<Activity> findById(Integer integer);
	
	List<String> getReferralList(String projectCode);

	List<GlobalActivity> findByFilterActivity(Filter filter);

	List<Note> getNotesList(int activityId);

	List<String> getActivityTypeList(String projectCode);

	List<String> getInterventionList(String projectCode);

	String getBeneficiaryNeededForActivityType(Filter filter);


	
	
	

	
}
