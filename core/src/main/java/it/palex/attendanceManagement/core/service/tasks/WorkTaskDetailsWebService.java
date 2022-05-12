package it.palex.attendanceManagement.core.service.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import it.palex.attendanceManagement.core.dtos.tasks.WorkTaskSummaryDTO;
import it.palex.attendanceManagement.data.dto.core.CompanyDTO;
import it.palex.attendanceManagement.data.dto.core.UserProfileSmallDTO;
import it.palex.attendanceManagement.data.dto.tasks.CompletedTaskDTO;
import it.palex.attendanceManagement.data.dto.transformers.CompletedTaskTransformer;
import it.palex.attendanceManagement.data.entities.CompletedTask;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.pseudoentities.WorkTaskSummaryPseudoEntity;
import it.palex.attendanceManagement.data.service.incarico.CompletedTaskService;
import it.palex.attendanceManagement.data.service.incarico.TaskSummaryService;
import it.palex.attendanceManagement.data.service.incarico.WorkTaskService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;

@Service
public class WorkTaskDetailsWebService implements GenericService {


	@Autowired
	private WorkTaskService workTaskService;
	
	@Autowired
	private CompletedTaskService completedTaskService;
	
	@Autowired
	private TaskSummaryService taskSummaryService;
	

	
	public GenericResponse<Page<WorkTaskSummaryDTO>> getSummaryOfTask(Long taskId, 
			Date startDate, Date endDate, String name, String surname, String email, Pageable pageable) {
		if(taskId==null || pageable==null) {
			return this.buildBadDataResponse();
		}
		
		WorkTask task = this.workTaskService.findById(taskId);
		
		if(task==null) {
			return this.buildNotFoundResponse();
		}
		
		Date startPark = startDate==null? null:DateUtility.startOfDayOfDate(startDate);
		Date endPark = endDate==null ? null:DateUtility.endOfDayOfDate(endDate);
		
		Pair<List<WorkTaskSummaryPseudoEntity>, Long> summaryPseudo = this.taskSummaryService.findWorkTaskSummary(task, 
				startPark, endPark, name, surname, email, pageable);
		
		long totalCount = summaryPseudo.getValue();
		
		
		List<WorkTaskSummaryDTO> summary = new ArrayList<WorkTaskSummaryDTO>(summaryPseudo.getKey().size());
		
		for (WorkTaskSummaryPseudoEntity workTaskSummaryPseudoEntity : summaryPseudo.getKey()) {
			WorkTaskSummaryDTO dto = new WorkTaskSummaryDTO();
			
			UserProfileSmallDTO user = new UserProfileSmallDTO();
			user.setId(workTaskSummaryPseudoEntity.getUserId());
			user.setName(workTaskSummaryPseudoEntity.getUserName());
			user.setPhoneNumber(workTaskSummaryPseudoEntity.getUserPhoneNumber());
			user.setSurname(workTaskSummaryPseudoEntity.getUserSurname());
			user.setEmail(workTaskSummaryPseudoEntity.getUserEmail());
			
			CompanyDTO company = new CompanyDTO();
			company.setId(workTaskSummaryPseudoEntity.getCompanyId());
			company.setName(workTaskSummaryPseudoEntity.getCompanyName());
			company.setDescription(workTaskSummaryPseudoEntity.getCompanyDescription());
			
			user.setCompany(company);
			
			dto.setUserProfile(user);
			dto.setWorkedHours(workTaskSummaryPseudoEntity.getWorkedHours());
			dto.setTotalCost(workTaskSummaryPseudoEntity.getSumOfCost());

			summary.add(dto);
		}
		
		
		return this.buildPageableOkResponse(summary, totalCount, pageable);
	}



	public GenericResponse<Page<CompletedTaskDTO>> getSummaryDetailsOfTaskAndUser(Long taskId, Integer userProfileId,
			Date startDate, Date endDate, Pageable pageable) {
		if(taskId==null || userProfileId==null || pageable==null) {
			return this.buildBadDataResponse();
		}
			
		Date startPark = startDate==null? null:DateUtility.startOfDayOfDate(startDate);
		Date endPark = endDate==null ? null:DateUtility.endOfDayOfDate(endDate);
		
		Pair<List<CompletedTask>, Long> pair = this.completedTaskService.findCompletedTaskDetaildOfUserInDateRange(
				userProfileId, taskId, startPark, endPark, pageable);
		
		List<CompletedTaskDTO> res = CompletedTaskTransformer.mapToDTO(pair.getKey(), true);
		long totalCount = pair.getValue();
		
		return this.buildPageableOkResponse(res, totalCount, pageable);
	}
	
	
}
