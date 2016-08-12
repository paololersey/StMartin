package org.springframework.samples.mvc.views;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.samples.hibernate.PersonDao;
import org.springframework.samples.hibernate.beans.DelayedMilestone;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalPerson;
import org.springframework.samples.hibernate.beans.Login;
import org.springframework.samples.hibernate.beans.LoginBean;
import org.springframework.samples.hibernate.beans.Parish;
import org.springframework.samples.hibernate.beans.Person;
import org.springframework.samples.hibernate.beans.PersonActivity;
import org.springframework.samples.hibernate.beans.ProjectPerson;
import org.springframework.samples.hibernate.beans.Sibling;
import org.springframework.samples.hibernate.beans.SupportGroup;
import org.springframework.samples.hibernate.beans.Zone;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/views/*")
@Transactional
public class PersonController {

	@Autowired
	private ApplicationContext appContext;

	
	
	@RequestMapping(value = "listaBen", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<Person> listaBen(@RequestBody ProjectPerson projectPerson) {
		PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		List<Person> people = personDao.findByTypeAndProjectCode(projectPerson.getPersonCode(), projectPerson.getProjectCode());
		return people;
	}
	
	@RequestMapping(value = "beneficiarySeen", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<Person> beneficiarySeen(@RequestBody Filter filter) {
		PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		List<Person> people = personDao.findByFilter(filter);
		return people;
	}
	
	
	
	@RequestMapping(value = "deletePerson", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String deletePerson(@RequestBody Person person) {
		PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		person.setEndDate(new Date());
		//personDao.delete(person);
		personDao.merge(person);
		return "OK";
	}
	
	@RequestMapping(value = "personCodeByPersonId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<ProjectPerson> personCodeByPersonId(@RequestBody PersonActivity personActivity) {
		PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		List<ProjectPerson> projectPerson = personDao.findPersonCodeByPersonId(personActivity.getPersonId());
		return projectPerson;
	}
	
	@RequestMapping(value = "personByPersonId", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<Person> personByPersonId(@RequestBody ProjectPerson projectPerson) {
		PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		List<Person> person= personDao.findPersonByPersonId(projectPerson.getPersonId(),projectPerson.getPersonCode());
		return person;
	}
	
	

	
	@RequestMapping(value = "inserisciBen", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String inserisciBen(@RequestBody GlobalPerson globalPerson) {
		
		ProjectPerson projectPerson = globalPerson.getProjectPerson();	
		Person person = globalPerson.getPerson();
		PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		if(globalPerson!=null && person!=null && person.getPersonId()!=null){
			//sono in update
			List<Person> beneficiarioPresente = personDao.findById(globalPerson.getPerson().getPersonId());
			if(beneficiarioPresente.size()>0){
				addSibling(globalPerson, person);	
				personDao.merge(person);
			}
		}
			
		else{ //insert
			// sto aggiungendo un nominativo ma devo controllare se esiste gia a db. In tal caso introduco solo ProjectPerson
			if (person.getFirstName()!=null && person.getLastName()!=null && person.getThirdName()!=null){
				String isNamePresent= personDao.checkNames(globalPerson);
			    if(isNamePresent!=null && "-1".equals(isNamePresent)) return "threeNamesError";
			    if(isNamePresent!=null && Integer.valueOf(isNamePresent)>0){
			    	person.setPersonId(Integer.valueOf(isNamePresent));
			    	ProjectPerson pp = new ProjectPerson();
			    	pp.setPersonId(Integer.valueOf(isNamePresent));
			    	pp.setProjectCode(globalPerson.getProjectPerson().getProjectCode());
			    	pp.setPersonCode(globalPerson.getProjectPerson().getPersonCode());
			    	Set<ProjectPerson> prS=new HashSet<ProjectPerson>();
			    	prS.add(pp);
			    	person.setProjectPersons(prS);
			    	personDao.merge(person);
			    	return "threeNamesAnotherProgram";
			    }
				    
			}
			if (person.getFirstName()!=null && person.getLastName()!=null && person.getVillage()!=null){
				String isNamePresent= personDao.checkNames(globalPerson);
				if(isNamePresent!=null && "-3".equals(isNamePresent)) return "twoNamesVillagesError";
			}
			
			if (person.getFileNumber()!=null){
				String isFileNumberPresent= personDao.checkFileNumber(globalPerson);
				if(isFileNumberPresent!=null) return "fileNumberError";
			}
			projectPerson.setPersonCode(projectPerson.getPersonCode());
			projectPerson.setProjectCode(projectPerson.getProjectCode());
			Set<ProjectPerson> prSet=new HashSet<ProjectPerson>();
			prSet.add(projectPerson);
			person.setProjectPersons(prSet);
			person.setPersonId(100);
			person.setInsertDate(new Date());

			addSibling(globalPerson, person);			
			personDao.save(person);	
		}

		return null;

	}

	private void addSibling(GlobalPerson globalPerson, Person person) {
		Set<Sibling> siblingSet=new HashSet<Sibling>();
		if(globalPerson.getSiblingList()!=null && globalPerson.getSiblingList().size()>0 ){
			for(Sibling s:globalPerson.getSiblingList()){
				siblingSet.add(s);	
			}
		
			person.setSiblingList(siblingSet);
		}
	}
	
	 @RequestMapping(value="citiesList",method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody List<String>  citiesList(@RequestBody String projectCode)
	    {
		    PersonDao personDao = (PersonDao) appContext.getBean("personDao");
	    	return personDao.getVillagesList(projectCode);
	    }
	 

	 @RequestMapping(value="zonesList",method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody List<Zone>  zonesList(@RequestBody String projectCode)
	    {
		    PersonDao personDao = (PersonDao) appContext.getBean("personDao");
	    	return personDao.getZonesList(projectCode);
	    }
	 

	 @RequestMapping(value="parishesList",method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody List<Parish>  parishesList(@RequestBody String projectCode)
	    {
		    PersonDao personDao = (PersonDao) appContext.getBean("personDao");
	    	return personDao.getParishesList(projectCode);
	    }
	 
	 @RequestMapping(value="delayedMilestoneList",method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody List<DelayedMilestone>  delayedMilestoneList(@RequestBody String projectCode)
	    {
		    PersonDao personDao = (PersonDao) appContext.getBean("personDao");
	    	return personDao.getDelayedMilestoneList(projectCode);
	    }
	 
	 @RequestMapping(value="majorTrainingList",method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody List<String>  majorTrainingList(@RequestBody String projectCode)
	    {
		    PersonDao personDao = (PersonDao) appContext.getBean("personDao");
	    	return personDao.getMajorTrainingList(projectCode);
	    }
	 
	 @RequestMapping(value="statesList",method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody List<String>  statesList(@RequestBody String projectCode)
	    {
		    PersonDao personDao = (PersonDao) appContext.getBean("personDao");
	    	return personDao.getStatesList(projectCode);
	    }
	 
	 
	 @RequestMapping(value="volunteerTypeList",method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody List<String>  volunteerTypeList(@RequestBody String projectCode)
	    {
		    PersonDao personDao = (PersonDao) appContext.getBean("personDao");
	    	return personDao.getVolunteerTypeList(projectCode);
	    }
	 
	 @RequestMapping(value="supportGroupList",method=RequestMethod.POST, produces="application/json")
	    public @ResponseBody List<SupportGroup>  supportGroupList(@RequestBody String projectCode)
	    {
		    PersonDao personDao = (PersonDao) appContext.getBean("personDao");
	    	return personDao.getSupportGroupList(projectCode);
	    }
	 
	 @RequestMapping(value="submitCredentials",method=RequestMethod.POST, produces="application/json")
	 public @ResponseBody LoginBean submitCredentials(@RequestBody Login login){
		 PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		 List<Login> loginList= personDao.getLogin(login);
		 String department = null;
		 if(loginList.size()>0){
			department = loginList.get(0).getDepartment();
		 }
		 
		 LoginBean loginBean=new LoginBean();
		 loginBean.setDepartment(department);
		 loginBean.setUsername(login.getUsername());
		 loginBean.setAccessToken("owjehfuowm893uhiuhnkasdbcfik");
		 return loginBean;
		 
	 }
	 
	 @RequestMapping(value="changePassword",method=RequestMethod.POST, produces="application/json")
	 public @ResponseBody Login changePassword(@RequestBody Login login){
		 PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		 login.setDepartment(login.getUsername().toUpperCase());
		 login.setInsertDate(new Date());
		 personDao.saveLogin(login);
		 return login;
		 
	 }
	 
	 @RequestMapping(value="updateOldPassword",method=RequestMethod.POST, produces="application/json")
	 public @ResponseBody Login updateOldPassword(@RequestBody Login login){
		 PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		 List<Login> loginList= personDao.getLogin(login);
		 int loginId=0;
		 if(loginList.size()>0){
				loginId = loginList.get(0).getLoginId();
		 }	 
		 login.setLoginId(loginId);
		 login.setEndDate(new Date());
		 personDao.mergeLogin(login);
		 return login;
		 
	 }
}