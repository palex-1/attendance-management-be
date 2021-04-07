package it.palex.attendanceManagement.data.service.core;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.core.QTaskCompletionsLocks;
import it.palex.attendanceManagement.data.entities.core.TaskCompletionsLocks;
import it.palex.attendanceManagement.data.entities.enumTypes.TaskCompletionLocksStatusEnum;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.TaskCompletionsLocksRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class TaskCompletionsLocksService implements BasicGenericService {

	private final QTaskCompletionsLocks QTCL = QTaskCompletionsLocks.taskCompletionsLocks;

	@Autowired
	private TaskCompletionsLocksRepository taskCompletionsLocksRepository;
	

	public TaskCompletionsLocks saveOrUpdate(TaskCompletionsLocks lock) {
		if (lock == null) {
			throw new NullPointerException();
		}
		if (!lock.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(lock);
		}

		return this.taskCompletionsLocksRepository.save(lock);
	}
	
	public TaskCompletionsLocks saveOrUpdateAndFlush(TaskCompletionsLocks toEvaluate) {
		TaskCompletionsLocks saved = this.saveOrUpdate(toEvaluate);
		this.taskCompletionsLocksRepository.flush();
		
		return saved;
	}
	
	public TaskCompletionsLocks findById(Integer id) {
		if (id == null) {
			return null;
		}
		Optional<TaskCompletionsLocks> res = this.taskCompletionsLocksRepository.findById(id);

		return this.getFromOptional(res);
	}

	
	public TaskCompletionsLocks findByYearAndMonth(Integer year, Integer month) {
		if (year == null || month == null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QTCL.year.eq(year));
		cond.and(QTCL.month.eq(month));

		TaskCompletionsLocks res = this.getFirstResultFromIterable(this.taskCompletionsLocksRepository.findAll(cond));

		return res;
	}


	public boolean isLockedYearAndMonth(int year, int month) {
		TaskCompletionsLocks lock = this.findByYearAndMonth(year, month);
		
		return lock!=null;
	}


	public Pair<List<TaskCompletionsLocks>, Long> findAllAndCount(Integer month, Integer year, String status,
			Pageable pageable) {
		if(pageable==null) {
			throw new NullPointerException();
		}
		
		BooleanBuilder cond = new BooleanBuilder();
		
		if(month!=null) {
			cond.and(QTCL.month.eq(month));
		}
		
		if(year!=null) {
			cond.and(QTCL.year.eq(year));
		}
		
		if(status!=null) {
			cond.and(QTCL.status.eq(status));
		}
		
		long totalCount = this.taskCompletionsLocksRepository.count(cond);
		
		List<TaskCompletionsLocks> list = this.iterableToList(
					this.taskCompletionsLocksRepository.findAll(cond, pageable)
				);
		
		return Pair.of(list, totalCount);
	}
	

	public void delete(TaskCompletionsLocks lock) {
		if(lock==null) {
			throw new NullPointerException();
		}
		this.taskCompletionsLocksRepository.delete(lock);
	}


	public List<TaskCompletionsLocks> findAllLockToBeProcessed(Pageable pageable) {
		if(pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QTCL.status.eq(TaskCompletionLocksStatusEnum.TO_BE_PROCESSED.name()));
		
		List<TaskCompletionsLocks> list = this.iterableToList(
				this.taskCompletionsLocksRepository.findAll(cond, pageable)
			);
		
		return list;
	}


	
	
}
