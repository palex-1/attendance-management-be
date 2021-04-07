package it.palex.attendanceManagement.data.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.core.ExpenseReport;
import it.palex.attendanceManagement.data.entities.core.ExpenseReportElement;
import it.palex.attendanceManagement.data.entities.core.QExpenseReportElement;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.ExpenseReportElementRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class ExpenseReportElementService implements BasicGenericService {

	private final QExpenseReportElement QERE = QExpenseReportElement.expenseReportElement;
	
	@Autowired
	private ExpenseReportElementRepository expenseReportElementRepository;
	
	
	public ExpenseReportElement saveOrUpdate(ExpenseReportElement elem) {
		if(elem==null) {
			throw new NullPointerException();
		}
		if(!elem.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(elem);
		}
		
		return this.expenseReportElementRepository.save(elem);
	}
	
	
	
	public ExpenseReportElement findById(Long id) {
		if(id==null) {
			return null;
		}
		return this.getFromOptional(
					this.expenseReportElementRepository.findById(id)
				);
	}
	
	
	public List<ExpenseReportElement> findAllExpensesInReport(ExpenseReport report, Sort sort){
		if(report==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QERE.expenseReport.id.eq(report.getId()));
		
		List<ExpenseReportElement> list = this.iterableToList(
					this.expenseReportElementRepository.findAll(cond, sort)
				);
		
		return list;
	}



	public void delete(ExpenseReportElement elem) {
		if(elem==null) {
			throw new NullPointerException();
		}
		this.expenseReportElementRepository.delete(elem);
	}
	
	
}
