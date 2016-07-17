package org.springframework.samples.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.hibernate.beans.Filter;
import org.springframework.samples.hibernate.beans.NatureOfPayment;
import org.springframework.samples.hibernate.beans.Payment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class PaymentDaoImpl implements PaymentDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	public PaymentDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	// CRUD methods
	@Override
	public void save(Payment payment) {
		this.sessionFactory.getCurrentSession().save(payment);
	}

	public void update(Payment payment) {
		this.sessionFactory.getCurrentSession().update(payment);
	}

	public void merge(Payment payment) {
		this.sessionFactory.getCurrentSession().merge(payment);
	}

	public void delete(Payment payment) {
		this.sessionFactory.getCurrentSession().delete(payment);
	}

	private SQLQuery instantiateQuery(String sql) {
		SQLQuery query = this.sessionFactory.getCurrentSession()
				.createSQLQuery(sql);
		query.addEntity(Payment.class);
		return query;
	}

	@Override
	public List<Payment> findByFilter(Filter filter) {

		Integer personIdBeneficiary = null;
		if (filter.getPersonIdBeneficiary() != null)
			personIdBeneficiary = filter.getPersonIdBeneficiary();

		Date dateStart = null;
		if (filter.getDateStart() != null)
			dateStart = filter.getDateStart();

		Date dateEnd = null;
		if (filter.getDateEnd() != null)
			dateEnd = filter.getDateEnd();

		String natureOfPayment = null;
		if (filter.getNatureOfPayment() != null)
			natureOfPayment = filter.getNatureOfPayment();

		String projectCode = null;
		if (filter.getProjectCode() != null)
			projectCode = filter.getProjectCode();

		String sqlDateStartCondition = "";
		String sqlDateEndCondition = "";
		String sqlBeneficiaryFromCondition = "";
		String sqlBeneficiaryWhereCondition = "";
		String sqlNatureOfPaymentCondition = "";
		
		if (personIdBeneficiary != null) {
			sqlBeneficiaryFromCondition = "  JOIN person"
					+ " ON payment.PERSON_ID=person.PERSON_ID ";
			sqlBeneficiaryWhereCondition = "AND person.PERSON_ID=:personIdBeneficiary ";
		}

		if (dateStart != null) {
			sqlDateStartCondition = "AND INSERT_DATE>=:dateStart ";
		}

		if (dateEnd != null) {
			sqlDateEndCondition = "AND INSERT_DATE<=:dateEnd ";
		}
		;

		if (natureOfPayment != null) {
			sqlNatureOfPaymentCondition = "AND NATURE_OF_PAYMENT =:natureOfPayment ";
		}
		;

		String sqlProjectCode = "AND payment.PROJECT_CODE =:projectCode ";
		if (projectCode != null && projectCode.equals("DIRE")) {
			sqlProjectCode = "";
		}

		String sql = "SELECT payment.* FROM payment "
				+ sqlBeneficiaryFromCondition
				+ "WHERE 1=1 "
				+ sqlBeneficiaryWhereCondition
				+ sqlNatureOfPaymentCondition
				+ sqlDateStartCondition
				+ sqlDateEndCondition
			    + sqlProjectCode;

		SQLQuery query = instantiateQuery(sql);

		if (personIdBeneficiary != null)
			query.setParameter("personIdBeneficiary", personIdBeneficiary);
		if (dateStart != null)
			query.setParameter("dateStart", dateStart);
		if (dateEnd != null)
			query.setParameter("dateEnd", dateEnd);
		if (natureOfPayment != null)
			query.setParameter("natureOfPayment", natureOfPayment);

		if (projectCode != null && !projectCode.equals("DIRE"))
			query.setParameter("projectCode", projectCode);

		return query.list();

	}

	@Override
	public List<String> getNatureOfPaymentList(String projectCode) {

		String sql = "SELECT NATURE_OF_PAYMENT FROM nature_of_payment WHERE 1=1 ";
		SQLQuery query = this.sessionFactory.getCurrentSession()
				.createSQLQuery(sql);
		
		return query.list();
	}

	@Override
	public List<Payment> findById(Integer paymentId) {
		String sql = "SELECT * FROM payment WHERE PAYMENT_ID=:idString";
		SQLQuery query = instantiateQuery(sql);
		query.setParameter("idString", paymentId);
		return query.list();
	}

	@Override
	public List<String> getPaymentsList(String projectCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getPaymentStatus(String projectCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
