package it.palex.attendanceManagement.core.service.tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.core.dtos.tasks.CompletedTaskAddDTO;
import it.palex.attendanceManagement.core.dtos.tasks.CompletedTaskUpdateDTO;
import it.palex.attendanceManagement.core.service.userProfile.FoodVoucherRequestWebService;
import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.dto.tasks.CompletedTaskDTO;
import it.palex.attendanceManagement.data.dto.transformers.CompletedTaskTransformer;
import it.palex.attendanceManagement.data.entities.CompletedTask;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.core.TaskCompletionsLocksService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.data.service.incarico.CompletedTaskService;
import it.palex.attendanceManagement.data.service.incarico.TeamComponentTaskService;
import it.palex.attendanceManagement.data.service.incarico.WorkTaskService;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.LongDTO;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.HttpCodes;

@Component
public class UserTaskCoreService implements GenericService {

	@Autowired
	private CompletedTaskService completedTaskService;

	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	@Autowired
	private TaskCompletionsLocksService taskCompletionsLocksService;
	
	@Autowired
	private WorkTaskService workTaskService;
	
	@Autowired
	private TeamComponentTaskService teamComponentTaskService;
	
	@Autowired
	private FoodVoucherRequestWebService foodVoucherRequestWebService;
	
	@Autowired
	private UserProfileService userProfileService;
	
	
	
	
	public GenericResponse<Page<CompletedTaskDTO>> findAllCurrentLoggedUserTasks(Date day, Pageable pageable) {
		if(day==null || pageable==null) {
			return this.buildBadDataResponse();
		}	
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		List<CompletedTask> tasks = this.completedTaskService.findAllTasksOfUserInDay(profile, day, pageable);
		
		List<CompletedTaskDTO> list = CompletedTaskTransformer.mapToDTO(tasks);
		
		long totalCount = this.completedTaskService.countAllTasksOfUserInDay(profile, day);
		
		
		return this.buildPageableOkResponse(list, totalCount, pageable);
	}
	
	public GenericResponse<LongDTO> findAllCurrentLoggedUserTasksSumOfDay(Date day){
		if(day==null) {
			return this.buildBadDataResponse();
		}	
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		long count = this.completedTaskService.countAllTasksOfUserInDay(profile, day);
		
		LongDTO res = new LongDTO();
		res.setValue(count);
		
		return this.buildOkResponse(res);
	}

	
	
	
	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.INTERNAL_SERVER_ERROR})
	public GenericResponse<CompletedTaskDTO> addNewTaskOfCurrentLoggedUserTasks(CompletedTaskAddDTO completedTask) {
		if(completedTask==null || completedTask.getSmartworked()==null || completedTask.getDay()==null 
			|| completedTask.getTaskCode()==null || completedTask.getWorkedHours()==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		Calendar date = DateUtility.dateToCalendar(completedTask.getDay());
		
		if(profile.getDateOfEmployment()!=null) {
			if(date.before(profile.getDateOfEmployment())) {
				return this.buildUnprocessableEntity(StandardReturnCodesEnum.USER_IS_NOT_AN_EMPLOYEE);
			}
		}
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
	
		WorkTask workTask = this.workTaskService.findByTaskCode(completedTask.getTaskCode());
		
		if(workTask==null) {
			return this.buildNotFoundResponse(StandardReturnCodesEnum.WORK_TASK_NOT_FOUND);
		}
		
		
		//tha task was disabled  
		if(workTask.getDeactivationDate()!=null &&  completedTask.getDay().after(workTask.getDeactivationDate())) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.TASK_IN_NOT_YET_ENABLED);
		}
		
		boolean userIsPartOfTheTeam = this.teamComponentTaskService.isPartOfTheTeam(profile, workTask);
		
		if(!userIsPartOfTheTeam) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.USER_IS_NOT_PART_OF_THE_TEAM);
		}
				
		int alreadyWorkedHours = (int) this.completedTaskService.sumAllUserHoursMadeOnDay(profile, completedTask.getDay());
				
		int totalHours = completedTask.getWorkedHours() + alreadyWorkedHours;
		
		if(totalHours>CompletedTask.HOURS_OF_DAY) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.TOTAL_WORKED_HOURS_REACHES_THE_LIMIT);
		}
		
		if(profile.getUserProfileContractInfo()!=null) {
			long absenceHours = this.completedTaskService.sumOnlyHoursOfAbsenceMadeOnDay(profile, completedTask.getDay());
			
			if(BooleanUtils.isTrue(workTask.getIsAbsenceTask())) {
				absenceHours = absenceHours + completedTask.getWorkedHours();
			}
			int hoursToWork = profile.getUserProfileContractInfo().getWorkDayHours();
			
			if(absenceHours>hoursToWork) {
				return this.buildUnprocessableEntity(StandardReturnCodesEnum.TOO_MUCH_ABSENCE_TASK_ADDED_IN_THIS_DAY);
			}
		}
		
		
		
		CompletedTask task = new CompletedTask();
		task.setDay(completedTask.getDay());
		task.setEditable(CompletedTask.DEFAULT_EDITABLE);
		task.setSmartworked(completedTask.getSmartworked());
		task.setTaskCode(workTask);
		task.setUserProfile(profile);
		task.setWorkedHours(completedTask.getWorkedHours());
		
		task = this.completedTaskService.saveOrUpdate(task);
		
		CompletedTaskDTO res = CompletedTaskTransformer.mapToDTO(task);
		
		return this.buildOkResponse(res);
	}

	public GenericResponse<List<CompletedTaskDTO>> findCompletedTaskOfMonth(Integer year, Integer month,
			Integer userProfileId) {
		if(userProfileId==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.userProfileService.findById(userProfileId);
		
		if(profile==null) {
			return this.buildNotFoundResponse();
		}
		
		return this.findCompletedTaskOfMonthOfUserPrivate(profile, year, month);
	}

	
	public GenericResponse<List<CompletedTaskDTO>> findCompletedTaskOfMonth(Integer year, Integer month) {
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		return this.findCompletedTaskOfMonthOfUserPrivate(profile, year, month);
	}
	
	
	private GenericResponse<List<CompletedTaskDTO>> findCompletedTaskOfMonthOfUserPrivate(
			UserProfile profile, Integer year, Integer month) {
		if(year==null || month==null) {
			return this.buildBadDataResponse();
		}
		
		Calendar startRange = Calendar.getInstance();
		startRange.set(Calendar.YEAR, year);
		startRange.set(Calendar.MONTH, month);
		
		startRange.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endRange = Calendar.getInstance();
		endRange.set(Calendar.YEAR, year);
		endRange.set(Calendar.MONTH, month);
		int lastDayOfMonth = startRange.getActualMaximum(Calendar.DAY_OF_MONTH);
		endRange.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
		
		Sort sort = Sort.by(Direction.DESC, "day");
		
		List<CompletedTask> completesTasks = this.completedTaskService.findTaskOfUserInDateRange(
				profile, sort, DateUtility.startOfDayOfDate(startRange.getTime()), 
				DateUtility.endOfDayOfDate(endRange.getTime()));
		
		List<CompletedTaskDTO> list = CompletedTaskTransformer.mapToDTO(completesTasks);
		
		return this.buildOkResponse(list);
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<CompletedTaskDTO> updateTaskOfCurrentLoggedUserTasks(
			CompletedTaskUpdateDTO completedTask) {
		if(completedTask==null || completedTask.getSmartworked()==null  || completedTask.getWorkedHours()==null
				|| completedTask.getId()==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		//use this function to overcome the security issue that user try to change tasks of other users
		CompletedTask task = this.completedTaskService.findByIdAndUser(completedTask.getId(), profile);
		
		if(task==null) {
			return this.buildNotFoundResponse();
		}
		
		Calendar date = DateUtility.dateToCalendar(task.getDay());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		if(!BooleanUtils.isTrue(task.getEditable())) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.THIS_TASK_IS_NOT_EDITABLE);
		}
		
		
		int alreadyWorkedHours = (int) this.completedTaskService.sumAllUserHoursMadeOnDay(profile, 
				task.getDay());
		
		int totalHours = completedTask.getWorkedHours() + alreadyWorkedHours - task.getWorkedHours();
		
		if(totalHours>CompletedTask.HOURS_OF_DAY) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.TOTAL_WORKED_HOURS_REACHES_THE_LIMIT);
		}
				
		task.setSmartworked(completedTask.getSmartworked());
		task.setWorkedHours(completedTask.getWorkedHours());

		task = this.completedTaskService.saveOrUpdate(task);
		
		//delete food voucher request if the hours are less than 0
		this.foodVoucherRequestWebService.sanitizeFoodVoucherRequest(profile, task.getDay());
		
		CompletedTaskDTO res = CompletedTaskTransformer.mapToDTO(task);
		
		return this.buildOkResponse(res);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> deleteTaskOfCurrentLoggedUserTasks(Long completedTaskId) {
		if(completedTaskId==null) {
			return this.buildBadDataResponse();
		}
		
		UserProfile profile = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserProfile();
		
		if(profile==null) {
			return this.buildUnauthorizedResponse();
		}
		
		//use this function to overcome the security issue that user try to change tasks of other users
		CompletedTask task = this.completedTaskService.findByIdAndUser(completedTaskId, profile);
		
		if(task==null) {
			return this.buildNotFoundResponse();
		}
		
		Calendar date = DateUtility.dateToCalendar(task.getDay());
		
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		
		boolean locked = this.taskCompletionsLocksService.isLockedYearAndMonth(year, month);
		
		if(locked) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.SUBMISSION_FOR_THIS_DATE_IS_LOCKED);
		}
		
		if(!BooleanUtils.isTrue(task.getEditable())) {
			return this.buildUnprocessableEntity(StandardReturnCodesEnum.THIS_TASK_IS_NOT_EDITABLE);
		}
	
		this.completedTaskService.delete(task);
		
		//delete food voucher request if the hours are less than 0
		this.foodVoucherRequestWebService.sanitizeFoodVoucherRequest(profile, task.getDay());
		
		return this.buildStringOkResponse("Successfully deleted");
	}

	
	
	
	
	
	
}
