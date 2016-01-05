package org.springframework.samples.mvc.views;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.samples.hibernate.ActivityDao;
import org.springframework.samples.hibernate.PersonDao;
import org.springframework.samples.hibernate.beans.Activity;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalActivity;
import org.springframework.samples.hibernate.beans.Note;
import org.springframework.samples.hibernate.beans.Person;
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
	
	@RequestMapping(value="beneficiaryNeededForActivityType",method=RequestMethod.POST, produces="application/json")
    public @ResponseBody String beneficiaryNeededForActivityType(@RequestBody Filter filter)
    {
	    ActivityDao activityDao = (ActivityDao) appContext.getBean("activityDao");
    	return activityDao.getBeneficiaryNeededForActivityType(filter);
    }
	
	
	@RequestMapping(value = "insertActivity", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String insertActivity(@RequestBody GlobalActivity globalActivity) {
		
		
		Activity activity = globalActivity.getActivity();
		ActivityDao activityDao = (ActivityDao) appContext.getBean("activityDao");
		if(globalActivity!=null && activity!=null && activity.getActivityId() !=null ){
			//update of activity fields
			List<Activity> activityAlreadyPresent = activityDao.findById(globalActivity.getActivity().getActivityId());
			if(activityAlreadyPresent.size()>0){
				activityDao.merge(globalActivity.getActivity());
			}
		}
			
		else{ //insert of activity 
			Set<PersonActivity> personActivitySet=new HashSet<PersonActivity>();
			
			if(globalActivity.getBeneficiary()!=null && globalActivity.getBeneficiary().getPersonId()!=null){
				PersonActivity personActivityBeneficiary = new PersonActivity();
				personActivityBeneficiary.setPersonId(globalActivity.getBeneficiary().getPersonId());	
				// update of level
				if(globalActivity.getLevelChange()!=null){
					PersonDao personDao = (PersonDao) appContext.getBean("personDao");
					List<Person> personList = personDao.findPersonByPersonId(globalActivity.getBeneficiary().getPersonId(), "BE");
					if(personList!=null && personList.size()>0){
						Person person = personList.get(0);
						person.setState(globalActivity.getLevelChange());
						personDao.merge(person);
					}					
				}
				// update of the insertDate (it is the 'admission date' in the program)
				if(globalActivity.getActivity()!=null && globalActivity.getActivity().getActivityType()!=null 
						  && ("ADMISSION".equalsIgnoreCase(globalActivity.getActivity().getActivityType())
								  ||"ASSESSMENT".equalsIgnoreCase(globalActivity.getActivity().getActivityType()))){
					PersonDao personDao = (PersonDao) appContext.getBean("personDao");
					List<Person> personList = personDao.findPersonByPersonId(globalActivity.getBeneficiary().getPersonId(), "BE");
					if(personList!=null && personList.size()>0){
						Person person = personList.get(0);
						person.setInsertDate(new Date());
						personDao.merge(person);
					}	
				}
				personActivitySet.add(personActivityBeneficiary);
			}
						
			if(globalActivity.getPersonInCharge()!=null && globalActivity.getPersonInCharge().getPersonId()!=null){
				PersonActivity personActivityInCharge=   new PersonActivity();		
				personActivityInCharge.setPersonId(globalActivity.getPersonInCharge().getPersonId());
				personActivitySet.add(personActivityInCharge);
			}
			if(!personActivitySet.isEmpty()){
			activity.setPersonActivities(personActivitySet);
			}
			activity.setActivityId(100);
			activity.setProjectCode(globalActivity.getActivity().getProjectCode());
			activityDao.save( activity);	
		}

		return null;

	}
	
	@RequestMapping(value = "deleteActivity", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String deleteActivity(@RequestBody GlobalActivity globalActivity) {
		ActivityDao activityDao = (ActivityDao) appContext.getBean("activityDao");
		activityDao.delete(globalActivity.getActivity());
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