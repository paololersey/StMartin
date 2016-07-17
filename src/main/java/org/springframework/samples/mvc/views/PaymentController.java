package org.springframework.samples.mvc.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.samples.hibernate.PaymentDao;
import org.springframework.samples.hibernate.PersonDao;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.GlobalPerson;
import org.springframework.samples.hibernate.beans.PaymentMapBean;
import org.springframework.samples.hibernate.beans.Payment;
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
public class PaymentController {

	@Autowired
	private ApplicationContext appContext;

	
	
	@RequestMapping(value = "paymentList", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<PaymentMapBean> paymentList(@RequestBody Filter filter) {
		PaymentDao paymentDao = (PaymentDao) appContext.getBean("paymentDao");
		PersonDao personDao = (PersonDao) appContext.getBean("personDao");
		List<Payment> paymentList = paymentDao.findByFilter(filter);
		// PaymentMapBean is the bean to be sent client side;
		ArrayList<PaymentMapBean> paymentMapBeanList=new ArrayList<PaymentMapBean>();
		for (Payment payment:paymentList){
			List<Payment> beneficiary = (List<Payment>)paymentDao.findById(payment.getPaymentId());		
			// mapping: it can be done also creating a NatureOfPaymentMapper between both
			PaymentMapBean paymentMapBean = new PaymentMapBean();
			paymentMapBean.setNatureOfPayment(payment.getNatureOfPayment());
			paymentMapBean.setPaymentDate(payment.getPaymentDate());
			paymentMapBean.setAmount(payment.getAmount());
			paymentMapBean.setPaymentId(payment.getPaymentId());
			List<Person> peopleList=personDao.findById(payment.getPersonId());
			if(peopleList.size()>0){
				Person ben=peopleList.get(0);
				paymentMapBean.setBeneficiary(ben.getFirstName() + " " + ben.getLastName() + " " + ben.getThirdName());
			}
				
			//paymentMapBean.setStatus(payment.getStatus());
			if(beneficiary.size()>0){
				Payment ben=beneficiary.get(0);
				//paymentMapBean.setBeneficiary(ben.getFirstName() + " " + ben.getLastName() + " " + ben.getThirdName());
			}
			paymentMapBeanList.add(paymentMapBean);
		}
		return paymentMapBeanList;
	}

	@RequestMapping(value = "natureOfPaymentList", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<String> natureOfPaymentList(@RequestBody ProjectPerson projectPerson) {
		PaymentDao paymentDao = (PaymentDao) appContext.getBean("paymentDao");
		List<String> natureOfPaymentList = paymentDao.getNatureOfPaymentList(projectPerson.getProjectCode());
		return natureOfPaymentList;
	}
	
	@RequestMapping(value = "paymentStatusList", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	List<String> PaymentStatusList(@RequestBody ProjectPerson projectPerson) {
		PaymentDao paymentDao = (PaymentDao) appContext.getBean("paymentDao");
		List<String> paymentList = paymentDao.getPaymentStatus(projectPerson.getProjectCode());
		return paymentList;
	}
	
	
	@RequestMapping(value = "deletePayment", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String deletePayment(@RequestBody Payment payment) {
		PaymentDao paymentDao = (PaymentDao) appContext.getBean("paymentDao");
		paymentDao.delete(payment);
		return "OK";
	}
	
	
	@RequestMapping(value = "insertPayment", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody
	String insertNatureOfCase(@RequestBody GlobalPerson globalPerson) {
		
		Payment payment = globalPerson.getPayment();
		PaymentDao paymentDao = (PaymentDao) appContext.getBean("paymentDao");
		if(globalPerson!=null && payment!=null && payment.getPaymentId()!=null){
			//sono in update
			List<Payment> casoPresente = paymentDao.findById(globalPerson.getPayment().getPaymentId());
			if(casoPresente.size()>0){
				paymentDao.merge(payment);
			}
		}
			
		else{ //insert
			try{
			payment.setPersonId(globalPerson.getPerson().getPersonId());
			if(payment!=null && payment.getPaymentDate()==null){
				payment.setPaymentDate(new Date());
			}			
			payment.setPaymentId(100);
			payment.setProjectCode(globalPerson.getProjectPerson().getProjectCode());		
			paymentDao.save(payment);	
			}
			catch (Exception e){
				//log.error(e.getMessage(),e);
			}
		}

		return "redirect:/resources/index.html";

	}
	

	
	
}