package it.palex.attendanceManagement.data.service.core;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.core.QReportUserTask;
import it.palex.attendanceManagement.data.entities.core.ReportUserTask;
import it.palex.attendanceManagement.data.entities.enumTypes.ReportStatusEnum;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.ReportUserTaskRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class ReportUserTaskService implements BasicGenericService {

	private final QReportUserTask QRUT = QReportUserTask.reportUserTask;
	
	@Autowired
	private ReportUserTaskRepository reportUserTaskRepository;
	
	
	public ReportUserTask saveOrUpdate(ReportUserTask report) {
		if(report==null) {
			throw new NullPointerException();
		}
		if(!report.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(report);
		}
		
		return this.reportUserTaskRepository.save(report);
	}
	
	public ReportUserTask saveOrUpdateAndFlush(ReportUserTask report) {
		report = this.saveOrUpdate(report);
		this.reportUserTaskRepository.flush();
		
		return report;
	}
	
	
	public void flushChanges() {
		this.reportUserTaskRepository.flush();
	}
	
	public ReportUserTask findById(Long id) {
		if(id==null) {
			return null;
		}
		
		return this.getFromOptional(this.reportUserTaskRepository.findById(id));
	}
	
	
	
	public Pair<List<ReportUserTask>, Long> findAllAndCount(Boolean includeDeleted, Integer year, Integer month, Pageable pageable){
		if(pageable==null) {
			throw new NullPointerException();
		}
		
		BooleanBuilder cond = new BooleanBuilder();
		
		if(year!=null) {
			cond.and(QRUT.year.eq(year));
		}
		
		if(month!=null) {
			cond.and(QRUT.month.eq(month));
		}
		
		if(!BooleanUtils.isTrue(includeDeleted)) {
			cond.and(QRUT.deleted.isFalse());
		}
		
		long totalCount = this.reportUserTaskRepository.count(cond);
		List<ReportUserTask> reports = this.iterableToList(
							this.reportUserTaskRepository.findAll(cond, pageable)
				);
		
	
		return Pair.of(reports, totalCount);
	}

	
	public List<ReportUserTask> findAllReportToBeDone(PageRequest pageable) {
		if(pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QRUT.status.eq(ReportStatusEnum.TODO.name()));
		
		List<ReportUserTask> reports = this.iterableToList(
				this.reportUserTaskRepository.findAll(cond, pageable)
				);
		
		return reports;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
