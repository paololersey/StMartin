package org.springframework.samples.mvc.views;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.samples.hibernate.ActivityDao;
import org.springframework.samples.hibernate.beans.Activity;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalActivity;
import org.springframework.samples.hibernate.beans.Note;
import org.springframework.samples.hibernate.beans.PersonActivity;
import org.springframework.samples.hibernate.beans.ReferralType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/views/*")
@Transactional
public class ActivityController {

	@Autowired
	private ApplicationContext appContext;

	
	@RequestMapping(value = "activityList", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<GlobalActivity> activityList(@RequestBody Filter filterActivity) {
		ActivityDao activityDao = (ActivityDao) appContext.getBean("activityDao");
		if(filterActivity.getDateStart()!=null && filterActivity.getDateEnd()!= null && 
				filterActivity.getDateStart().compareTo(filterActivity.getDateEnd())>0){
	
		}
		List<GlobalActivity> activities = activityDao.findByFilterActivity(filterActivity);
		return activities;
	}
	
	@RequestMapping(value = "notesList", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<Note> notesList(@RequestBody int activityId) {
		ActivityDao activityDao = (ActivityDao) appContext.getBean("activityDao");
		return activityDao.getNotesList(activityId);

	}
	
	
	
	 @RequestMapping(value="referralType",method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody List<String>  referralList(@RequestBody String projectCode)
	    {
		    ActivityDao activityDao = (ActivityDao) appContext.getBean("activityDao");
	    	return activityDao.getReferralList(projectCode);
	    }
	 
	 @RequestMapping(value="activityType",method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody List<String> activityTypeList(@RequestBody String projectCode)
	    {
		    ActivityDao activityDao = (ActivityDao) appContext.getBean("activityDao");
	    	return activityDao.getActivityTypeList(projectCode);
	    }
	 
	 @RequestMapping(value="interventionType",method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody List<String> interventionList(@RequestBody String projectCode)
	    {
		    ActivityDao activityDao = (ActivityDao) appContext.getBean("activityDao");
	    	return activityDao.getInterventionList(projectCode);
	    } 
	 
	 
	@RequestMapping(value = "insertNote", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String insertNote(@RequestBody Note note) {
			ActivityDao activityDao = (ActivityDao) appContext
					.getBean("activityDao");
			activityDao.save(note);
			return null;
		}	 
	
	
	@RequestMapping(value = "insertActivity", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String insertActivity(@RequestBody GlobalActivity globalActivity) {
		
		
		Activity activity = globalActivity.getActivity();
		ActivityDao activityDao = (ActivityDao) appContext.getBean("activityDao");
		if(globalActivity!=null && activity!=null && activity.getActivityId() !=null ){
			//sono in update
			List<Activity> activityAlreadyPresent = activityDao.findById(globalActivity.getActivity().getActivityId());
			if(activityAlreadyPresent.size()>0){
				activityDao.merge(globalActivity.getActivity());
			}
		}
			
		else{ //insert
			PersonActivity personActivityBeneficiary = new PersonActivity();				
			personActivityBeneficiary.setPersonId(globalActivity.getBeneficiary().getPersonId());			
			PersonActivity personActivityInCharge=   new PersonActivity();		
			personActivityInCharge.setPersonId(globalActivity.getPersonInCharge().getPersonId());
			
			
			Set<PersonActivity> personActivitySet=new HashSet<PersonActivity>();
			personActivitySet.add(personActivityBeneficiary);
			personActivitySet.add(personActivityInCharge);

			activity.setPersonActivities(personActivitySet);
            
			activity.setActivityId(100);
			activityDao.save( activity);	
		}

		return null;

	}
	
	@RequestMapping(value = "deleteActivity", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String deleteActivity(@RequestBody Activity activity) {
		ActivityDao activityDao = (ActivityDao) appContext.getBean("activityDao");
		activityDao.delete(activity);
		return "OK";
	}
	
	@RequestMapping(value = "deleteNote", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String deleteNote(@RequestBody Note note) {
		ActivityDao activityDao = (ActivityDao) appContext.getBean("activityDao");
		List<Note> noteByActivityId=activityDao.getNotesList(note.getActivityId());
		if(noteByActivityId!=null && noteByActivityId.size()>0) activityDao.delete(noteByActivityId.get(0));
		
		return "OK";
	}
	
}