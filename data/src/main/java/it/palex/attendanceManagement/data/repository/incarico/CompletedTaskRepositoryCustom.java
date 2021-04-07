package it.palex.attendanceManagement.data.repository.incarico;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;

import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.pseudoentities.WorkTaskSummaryPseudoEntity;

public interface CompletedTaskRepositoryCustom {

	public Pair<List<WorkTaskSummaryPseudoEntity>, Long> findWorkTaskSummary(WorkTask task, Date startDate, Date endDate,
			String name, String surname, String email, Pageable pageable);

}
