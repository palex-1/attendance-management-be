package it.palex.attendanceManagement.data.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.palex.attendanceManagement.data.entities.core.QVacationAddRemoveLogs;
import it.palex.attendanceManagement.data.entities.core.VacationAddRemoveLogs;
import it.palex.attendanceManagement.data.repository.core.VacationAddRemoveLogsRepository;

@Service
public class VacationAddRemoveLogsService {

	private final QVacationAddRemoveLogs QVARL = QVacationAddRemoveLogs.vacationAddRemoveLogs;
	
	@Autowired
	private VacationAddRemoveLogsRepository vacationAddRemoveLogsRepository;
	
	
	public VacationAddRemoveLogs saveOrUpdate(VacationAddRemoveLogs log) {
		if(log==null) {
			throw new NullPointerException();
		}
		return this.vacationAddRemoveLogsRepository.save(log);
	}
	
	
	
}
