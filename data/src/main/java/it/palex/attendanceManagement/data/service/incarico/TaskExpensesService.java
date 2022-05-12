package it.palex.attendanceManagement.data.service.incarico;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.core.QTaskExpenses;
import it.palex.attendanceManagement.data.entities.core.TaskExpenses;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.incarico.TaskExpensesRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class TaskExpensesService implements BasicGenericService {

	private final QTaskExpenses QTE = QTaskExpenses.taskExpenses;
	
	@Autowired
	private TaskExpensesRepository taskExpensesRepository;
	
	
	public TaskExpenses saveOrUpdate(TaskExpenses taskExpense) {
		if(taskExpense==null) {
			throw new NullPointerException();
		}
		if(!taskExpense.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase();
		}
		
		return this.taskExpensesRepository.save(taskExpense);
	}
	
	public TaskExpenses findById(Long id) {
		if(id==null) {
			return null;
		}
		
		return this.getFromOptional(
					this.taskExpensesRepository.findById(id)
				);
	}
	
	
	public Pair<List<TaskExpenses>, Long> findAllAndCount(String titleFilter, Date dayFrom, Date dayTo,
			String expenseType, WorkTask workTask, Pageable pageable) {
		if(pageable==null) {
			throw new NullPointerException();
		}
		
		BooleanBuilder cond = new BooleanBuilder();
		
		if(titleFilter!=null) {
			cond.and(QTE.title.containsIgnoreCase(titleFilter));
		}
		
		if(expenseType!=null) {
			cond.and(QTE.expenseType.containsIgnoreCase(expenseType));
		}
		
		if(workTask!=null) {
			cond.and(QTE.workTask.id.eq(workTask.getId()));
		}
		
		if(dayFrom!=null) {
			cond.and(QTE.day.goe(dayFrom));
		}
		
		if(dayTo!=null) {
			cond.and(QTE.day.loe(dayTo));
		}
		
		long totalCount = this.taskExpensesRepository.count(cond);
		
		List<TaskExpenses> res = this.iterableToList(
									this.taskExpensesRepository.findAll(cond, pageable)
								);
		
		return Pair.of(res, totalCount);
	}
	
	
	public void delete(TaskExpenses taskExpense) {
		if(taskExpense==null) {
			throw new NullPointerException();
		}
		
		this.taskExpensesRepository.delete(taskExpense);
	}


    public Double computeTotalExpensesCost(WorkTask task, Date startDate, Date endDate) {
		if(task==null) {
			throw new NullPointerException();
		}

		return this.taskExpensesRepository.computeTotalExpensesCost(task, startDate, endDate);
    }
}
