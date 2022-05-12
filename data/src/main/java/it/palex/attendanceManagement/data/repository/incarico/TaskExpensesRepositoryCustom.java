package it.palex.attendanceManagement.data.repository.incarico;

import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.pseudoentities.WorkTaskSummaryPseudoEntity;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface TaskExpensesRepositoryCustom {

    public Double computeTotalExpensesCost(WorkTask task, Date startDate, Date endDate);

}
