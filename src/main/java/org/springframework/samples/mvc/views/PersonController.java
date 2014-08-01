package org.springframework.samples.mvc.views;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.samples.hibernate.PersonDao;
import org.springframework.samples.hibernate.beans.City;
import org.springframework.samples.hibernate.beans.GlobalPerson;
import org.springframework.samples.hibernate.beans.Person;
import org.springframework.samples.hibernate.beans.ProjectPerson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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
		List<Person> beneficiari = personDao.findByTypeAndProjectCode(projectPerson.getPersonCode(), projectPerson.getProjectCode());
		return beneficiari;
	}
	
	
	
	@RequestMapping(value = "deletePerson", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String deletePerson(@RequestBody Person person) {
		PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		//List<Person> beneficiari = personDao.findAll();
		personDao.delete(person);
		return "OK";
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
				personDao.merge(globalPerson.getPerson());
			}
		}
			
		else{ //insert
			// sto aggiungendo un nominativo ma devo controllare se esiste gi� a db. In tal caso introduco solo ProjectPerson					
			projectPerson.setPersonCode(projectPerson.getPersonCode());
			projectPerson.setProjectCode(projectPerson.getProjectCode());
			Set<ProjectPerson> prSet=new HashSet<ProjectPerson>();
			prSet.add(projectPerson);
			person.setProjectPersons(prSet);
			person.setPersonId((long) 100);		   
			personDao.save(globalPerson.getPerson());	
		}

		return "redirect:/resources/index.html";

	}
	

	 @RequestMapping(value="citiesList",method=RequestMethod.GET, produces="application/json")
	    public @ResponseBody List<String>  citiesList()
	    {
		    PersonDao personDao = (PersonDao) appContext.getBean("personDao");
	    	return personDao.getCitiesList();
	    }
	
	
}