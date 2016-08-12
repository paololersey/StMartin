package org.springframework.samples.mvc.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.samples.hibernate.NatureOfCasePersonDao;
import org.springframework.samples.hibernate.PersonDao;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalPerson;
import org.springframework.samples.hibernate.beans.NatureOfCasePerson;
import org.springframework.samples.hibernate.beans.NatureOfCasePersonMapBean;
import org.springframework.samples.hibernate.beans.Person;
import org.springframework.samples.hibernate.beans.ProjectPerson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/views/*")
@Transactional
public class NatureOfCaseController {

	@Autowired
	private ApplicationContext appContext;

	
	
	@RequestMapping(value = "natureOfCasePersonList", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<NatureOfCasePersonMapBean> natureOfCasePersonList(@RequestBody Filter filter) {
		NatureOfCasePersonDao natureOfCasePersonDao = (NatureOfCasePersonDao) appContext.getBean("natureOfCasePersonDao");
		List<NatureOfCasePerson> natureOfCasePersonList = natureOfCasePersonDao.findByFilter(filter);
		PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		// NatureOfCasePersonMapBean is the bean to be sent client side;
		ArrayList<NatureOfCasePersonMapBean> natureOfCasePersonMapBeanList=new ArrayList<NatureOfCasePersonMapBean>();
		for (NatureOfCasePerson natureOfCasePerson:natureOfCasePersonList){
			List<Person> beneficiary = (List<Person>)personDao.findById(natureOfCasePerson.getPersonId());		
			// mapping: it can be done also creating a NatureOfCasePersonMapper between both
			NatureOfCasePersonMapBean natureOfCasePersonMapBean = new NatureOfCasePersonMapBean();
			natureOfCasePersonMapBean.setNatureOfCase(natureOfCasePerson.getNatureOfCase());
			natureOfCasePersonMapBean.setInsertDate(natureOfCasePerson.getInsertDate());
			natureOfCasePersonMapBean.setStatus(natureOfCasePerson.getStatus());
			natureOfCasePersonMapBean.setDelayedMilestone(natureOfCasePerson.getDelayedMilestone());
			if(beneficiary.size()>0){
				Person ben=beneficiary.get(0);
				natureOfCasePersonMapBean.setBeneficiary(ben.getFirstName() + " " + ben.getLastName() + " " + ben.getThirdName());
			}
			natureOfCasePersonMapBeanList.add(natureOfCasePersonMapBean);
		}
		return natureOfCasePersonMapBeanList;
	}

	@RequestMapping(value = "natureOfCasesList", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<String> natureOfCasesList(@RequestBody ProjectPerson projectPerson) {
		NatureOfCasePersonDao natureOfCasePersonDao = (NatureOfCasePersonDao) appContext.getBean("natureOfCasePersonDao");
		List<String> natureOfCasesList = natureOfCasePersonDao.getNatureOfCasesList(projectPerson.getProjectCode());
		return natureOfCasesList;
	}
	
	@RequestMapping(value = "natureOfCasesStatusList", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<String> natureOfCasesStatusList(@RequestBody ProjectPerson projectPerson) {
		NatureOfCasePersonDao natureOfCasePersonDao = (NatureOfCasePersonDao) appContext.getBean("natureOfCasePersonDao");
		List<String> natureOfCasesList = natureOfCasePersonDao.getNatureOfCaseStatus(projectPerson.getProjectCode());
		return natureOfCasesList;
	}
	
	
	@RequestMapping(value = "deleteNatureOfCase", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String deleteNatureOfCase(@RequestBody NatureOfCasePerson natureOfCasePerson) {
		NatureOfCasePersonDao natureOfCasePersonDao = (NatureOfCasePersonDao) appContext.getBean("natureOfCasePersonDao");
		natureOfCasePersonDao.delete(natureOfCasePerson);
		return "OK";
	}
	
	
	@RequestMapping(value = "insertNatureOfCase", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String insertNatureOfCase(@RequestBody GlobalPerson globalPerson) {
		
		NatureOfCasePerson natureOfCasePerson = globalPerson.getNatureOfCasePerson();
		NatureOfCasePersonDao natureOfCasePersonDao = (NatureOfCasePersonDao) appContext.getBean("natureOfCasePersonDao");
		if(globalPerson!=null && natureOfCasePerson!=null && natureOfCasePerson.getNatureOfCasePersonId()!=null){
			//sono in update
			List<NatureOfCasePerson> casoPresente = natureOfCasePersonDao.findById(globalPerson.getNatureOfCasePerson().getNatureOfCasePersonId());
			if(casoPresente.size()>0){
				natureOfCasePersonDao.merge(natureOfCasePerson);
			}
		}
			
		else{ //insert
			try{
			natureOfCasePerson.setPersonId(globalPerson.getPerson().getPersonId());
			if(natureOfCasePerson!=null && natureOfCasePerson.getInsertDate()==null){
				natureOfCasePerson.setInsertDate(new Date());
			}			
			natureOfCasePerson.setNatureOfCasePersonId(100);
			natureOfCasePerson.setProjectCode(globalPerson.getProjectPerson().getProjectCode());		
			natureOfCasePersonDao.save(natureOfCasePerson);	
			}
			catch (Exception e){
				//log.error(e.getMessage(),e);
			}
		}

		return "redirect:/resources/index.html";

	}
	
	
}