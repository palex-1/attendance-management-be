package it.palex.attendanceManagement.data.service.core;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.ExpenseReport;
import it.palex.attendanceManagement.data.entities.core.QExpenseReport;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.ExpenseReportRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class ExpenseReportService implements BasicGenericService {

	private final QExpenseReport QER = QExpenseReport.expenseReport;
	
	@Autowired
	private ExpenseReportRepository expenseReportRepository;
	
	
	public ExpenseReport saveOrUpdate(ExpenseReport report) {
		if(report==null) {
			throw new NullPointerException();
		}
		if(!report.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(report);
		}
		
		return this.expenseReportRepository.save(report);
	}
	
	
	public ExpenseReport findById(Long id) {
		if(id==null) {
			return null;
		}
		return this.getFromOptional(
					this.expenseReportRepository.findById(id)
				);
	}
	
	
	
	public Pair<List<ExpenseReport>, Long> findAllAndCount(UserProfile madeBy, String madeByName, String madeBySurname,
			String status, String madeByEmail, Date dateOfExpenceFrom, Date dateOfExpenceTo, String location, String title, Pageable pageable){
		if(pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		
		if(madeBy!=null) {
			cond.and(QER.madeBy.id.eq(madeBy.getId()));
		}
		if(madeByName!=null) {
			cond.and(QER.madeBy.name.containsIgnoreCase(madeByName));
		}
		if(madeBySurname!=null) {
			cond.and(QER.madeBy.surname.containsIgnoreCase(madeBySurname));
		}
		if(status!=null) {
			cond.and(QER.status.containsIgnoreCase(status));
		}
		if(madeByEmail!=null) {
			cond.and(QER.madeBy.email.containsIgnoreCase(madeByEmail));
		}
		if(dateOfExpenceFrom!=null) {
			cond.and(QER.dateOfExpence.goe(dateOfExpenceFrom));
		}
		if(dateOfExpenceTo!=null) {
			cond.and(QER.dateOfExpence.loe(dateOfExpenceTo));
		}
		if(location!=null) {
			cond.and(QER.location.containsIgnoreCase(location));
		}
		if(title!=null) {
			cond.and(QER.title.containsIgnoreCase(title));
		}
		
		
		long totalCount = this.expenseReportRepository.count(cond);
		List<ExpenseReport> reports = this.iterableToList(
					this.expenseReportRepository.findAll(cond, pageable)
				);
		
		return Pair.of(reports, totalCount);
	}

	/**
	 * Use this mehtod to check that user is the owner
	 * @param profile
	 * @param reportId
	 * @return
	 */
	public ExpenseReport findByIdAndUser(UserProfile profile, Long reportId) {
		if(profile==null || reportId==null) {
			throw new NullPointerException();
		}
		
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QER.madeBy.id.eq(profile.getId()));
		cond.and(QER.id.eq(reportId));
		
		ExpenseReport rep = this.getFirstResultFromIterable(
					this.expenseReportRepository.findAll(cond)
				);
		
		return rep;
	}


	public void delete(ExpenseReport report) {
		if(report==null) {
			throw new NullPointerException();
		}
		this.expenseReportRepository.delete(report);		
	}


	public List<ExpenseReport> findRequestOfUserInDateRange(UserProfile profile, Date from, Date to) {
		if (profile == null || from == null || to == null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QER.madeBy.id.eq(profile.getId()));
		cond.and(QER.dateOfExpence.goe(from));
		cond.and(QER.dateOfExpence.loe(to));

		List<ExpenseReport> res = this.iterableToList(this.expenseReportRepository.findAll(cond));

		return res;
	}
	
	
	
	
}
