package it.palex.attendanceManagement.data.service.incarico;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.CompletedTask;
import it.palex.attendanceManagement.data.entities.QCompletedTask;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.incarico.CompletedTaskRepository;
import it.palex.attendanceManagement.data.service.core.TasksUtilsService;
import it.palex.attendanceManagement.library.service.BasicGenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;

@Service
public class CompletedTaskService implements BasicGenericService {
	
	private final QCompletedTask QCT = QCompletedTask.completedTask;

	@Autowired
	private CompletedTaskRepository completedTaskRepo;
	
	@Autowired
	private TasksUtilsService tasksUtilsService;
	
	
	public CompletedTask saveOrUpdate(CompletedTask task) {
		if(task==null) {
			throw new NullPointerException();
		}
		if(!task.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(task);
		}
		
		return this.completedTaskRepo.save(task);
	}
	
	
	public CompletedTask findById(Long id) {
		if(id==null) {
			return null;
		}
		Optional<CompletedTask> res = this.completedTaskRepo.findById(id);
		
		return this.getFromOptional(res);
	}
	
	/**
	 * Use this function to check that user is the owner of CompletedTask
	 * @param id
	 * @param profile
	 * @return
	 */
	public CompletedTask findByIdAndUser(Long id, UserProfile profile) {
		if(id==null || profile==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QCT.id.eq(id));
		cond.and(QCT.userProfile.id.eq(profile.getId()));
		
		CompletedTask res = this.getFirstResultFromIterable(
				this.completedTaskRepo.findAll(cond)
			);
	
		return res;
	}
	
	
	public List<CompletedTask> findTaskOfUserInDay(UserProfile user, Date day, WorkTask task) {
		if(task==null || user==null || day==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QCT.userProfile.id.eq(user.getId()));
		cond.and(QCT.day.eq(day));
		cond.and(QCT.taskCode.id.eq(task.getId()));
		
		List<CompletedTask> res = this.iterableToList(
				this.completedTaskRepo.findAll(cond)
			);
	
		return res;
	
	}
	
	public List<CompletedTask> findAllTasksOfUserInDay(UserProfile user, Date day, Pageable pageable){
		if(pageable==null || user==null || day==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QCT.userProfile.id.eq(user.getId()));
		cond.and(QCT.day.eq(day));
		
		List<CompletedTask> res = this.iterableToList(
					this.completedTaskRepo.findAll(cond, pageable)
				);
		
		return res;
	}
	
	public long countAllTasksOfUserInDay(UserProfile user, Date day){
		if(user==null || day==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QCT.userProfile.id.eq(user.getId()));
		cond.and(QCT.day.eq(day));
		
		return this.completedTaskRepo.count(cond);
	}
	
	public long sumOnlyWorkedHoursMadeOnDateRangeByUser(WorkTask task, UserProfile employee, Date startDate,
			Date endDate) {
		if(task==null || employee==null || startDate==null || endDate==null) {
			throw new NullPointerException();
		}
		return this.completedTaskRepo.sumOnlyWorkedHoursMadeOnDateRangeByUser(task.getId(), employee.getId(),
				startDate, endDate);
	}
	
	public long sumRegisteredHoursMadeOnDateRangeByUser(WorkTask task, UserProfile employee, Date startOfDayOfDate,
			Date endOfDayOfDate) {
		if(task==null || employee==null || startOfDayOfDate==null || endOfDayOfDate==null) {
			throw new NullPointerException();
		}
		
		return this.completedTaskRepo.sumRegisteredHoursMadeOnDateRangeByUser(task.getId(), employee.getId(),
				startOfDayOfDate, endOfDayOfDate);
	}
	
	
	public long sumOnlyWorkedHoursMadeOnDay(UserProfile user, Date day) {
		if(user==null || day==null) {
			throw new NullPointerException();
		}
		
		return this.completedTaskRepo.sumOnlyWorkedHoursMadeOnDay(user.getId(),  day);
	}
	
	public long sumOnlyHoursOfAbsenceMadeOnDay(UserProfile user, Date day) {
		if(user==null || day==null) {
			throw new NullPointerException();
		}
		
		return this.completedTaskRepo.sumOnlyHoursOfAbsenceMadeOnDay(user.getId(),  day);
	}
	
	public long sumAllUserHoursMadeOnDay(UserProfile user, Date day) {
		if(user==null || day==null) {
			throw new NullPointerException();
		}
		
		return this.completedTaskRepo.sumAllUserHoursMadeOnDay(user.getId(),  day);
	}
	
	
	
	public long countIncarichiEseguitiOfIncarico(String taskCode) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QCT.taskCode.taskCode.eq(taskCode));
		
		return this.completedTaskRepo.count(condition);
	}


	public void delete(CompletedTask task) {
		if(task==null) {
			throw new NullPointerException();
		}
		this.completedTaskRepo.delete(task);
	}


	public List<CompletedTask> findTaskOfUserInDateRange(UserProfile profile, Sort sort, Date startOfDayOfDate,
			Date endOfDayOfDate) {
		if(profile==null || sort==null || startOfDayOfDate==null || endOfDayOfDate==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QCT.userProfile.id.eq(profile.getId()));
		cond.and(QCT.day.goe(startOfDayOfDate));
		cond.and(QCT.day.loe(endOfDayOfDate));
		
		List<CompletedTask> res = this.iterableToList(
									this.completedTaskRepo.findAll(cond, sort)
								);
		
		return res;
	}

	/**
	 * 
	 * @param userProfileId
	 * @param taskId
	 * @param dateFrom
	 * @param dateTo
	 * @param pageable
	 * @return a pair with the list ad key and total count of element as value
	 */
	public Pair<List<CompletedTask>, Long> findCompletedTaskDetaildOfUserInDateRange(Integer userProfileId,
			Long taskId, Date dateFrom, Date dateTo, Pageable pageable) {
		if(taskId==null || userProfileId==null || pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QCT.userProfile.id.eq(userProfileId));
		cond.and(QCT.taskCode.id.eq(taskId));
		
		if(dateFrom!=null) {
			cond.and(QCT.day.goe(dateFrom));
		}
		
		if(dateTo!=null) {
			cond.and(QCT.day.loe(dateTo));
		}
		
		long count = this.completedTaskRepo.count(cond);
		
		List<CompletedTask> res = this.iterableToList(
					this.completedTaskRepo.findAll(cond, pageable)
				);
		
		return Pair.of(res, count);
	}


	


	


	
	
	
}
