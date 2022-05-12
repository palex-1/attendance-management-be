package it.palex.attendanceManagement.data.service.incarico;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.QTeamComponentTask;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.pseudoentities.WorkTaskSummaryPseudoEntity;
import it.palex.attendanceManagement.data.repository.incarico.CompletedTaskRepository;
import it.palex.attendanceManagement.data.repository.incarico.TeamComponentTaskRepository;

@Service
public class TaskSummaryService {

	@Autowired
	private CompletedTaskRepository completedTaskRepo;
	
	@Autowired
	private TeamComponentTaskRepository teamComponentTaskRepository;
	
	
	public Pair<List<WorkTaskSummaryPseudoEntity>, Long> findWorkTaskSummary(WorkTask task, Date startDate, Date endDate, 
			String name, String surname, String email, Pageable pageable){
		if(task==null || pageable==null) {
			throw new NullPointerException();
		}		
		
		return this.completedTaskRepo.findWorkTaskSummary(task, startDate, endDate, name, surname, email, pageable);
	}


	public Double computeTotalHumanCost(WorkTask task, Date startDate, Date endDate) {
		if(task==null) {
			throw new NullPointerException();
		}

		return this.completedTaskRepo.computeTotalHumanCost(task, startDate, endDate);
	}
}
