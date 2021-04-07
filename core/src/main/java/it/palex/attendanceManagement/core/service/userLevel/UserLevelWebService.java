package it.palex.attendanceManagement.core.service.userLevel;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.dto.core.UserLevelDTO;
import it.palex.attendanceManagement.data.dto.transformers.UserLevelTransformer;
import it.palex.attendanceManagement.data.entities.core.UserLevel;
import it.palex.attendanceManagement.data.service.impiegato.UserLevelService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileContractInfoService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.HttpCodes;

@Service
public class UserLevelWebService implements GenericService {

	@Autowired
	private UserLevelService userLevelService;
	
	@Autowired
	private UserProfileContractInfoService userProfileContractInfoService;
	
	
	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST, HttpCodes.CONFLICT })
	public GenericResponse<UserLevelDTO> addNewUserLevel(UserLevelDTO toAdd){
		if(toAdd==null || toAdd.getLevel()==null) {
			return this.buildBadDataResponse();
		}
		if(toAdd.getMonthlyLeaveHours()==null || toAdd.getMonthlyLeaveHours()<0 || 
				toAdd.getMonthlyVacationDays()==null || toAdd.getMonthlyVacationDays()<0) {
			return this.buildBadDataResponse();
		}
		
		UserLevel newLevel = new UserLevel();
		newLevel.setBankHourEnabled(toAdd.getBankHourEnabled());
		newLevel.setExtraWorkPaid(toAdd.getExtraWorkPaid());
		newLevel.setLevel(StringUtils.trim(toAdd.getLevel()));
		newLevel.setMonthlyLeaveHours(toAdd.getMonthlyLeaveHours());
		newLevel.setMonthlyVacationDays(toAdd.getMonthlyVacationDays());
		
		if(!newLevel.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		UserLevel oldLevel = this.userLevelService.findByLevel(newLevel.getLevel());
		
		if(oldLevel!=null) {
			return this.buildConflictEntity("Level already exist");
		}
		
		newLevel = this.userLevelService.saveOrUpdate(newLevel);
		
		return this.buildOkResponse(UserLevelTransformer.mapToDTO(newLevel));
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.BAD_REQUEST, HttpCodes.CONFLICT })
	public GenericResponse<UserLevelDTO> updateUserLevel(UserLevelDTO toAdd){
		if(toAdd==null || toAdd.getId()==null || toAdd.getLevel()==null) {
			return this.buildBadDataResponse();
		}
		
		if(toAdd.getMonthlyLeaveHours()==null || toAdd.getMonthlyLeaveHours()<0 || 
				toAdd.getMonthlyVacationDays()==null || toAdd.getMonthlyVacationDays()<0) {
			return this.buildBadDataResponse();
		}
		
		UserLevel levelToUpdate = this.userLevelService.findById(toAdd.getId());
		
		if(levelToUpdate==null) {
			return this.buildNotFoundResponse();
		}
		
		if(!StringUtils.equalsIgnoreCase(levelToUpdate.getLevel(), toAdd.getLevel())) {
			String levelToAddLabel = StringUtils.trim(toAdd.getLevel());
			
			UserLevel oldLevel = this.userLevelService.findByLevel(levelToAddLabel);
			
			if(oldLevel!=null) {
				return this.buildConflictEntity("Level already exist");
			}
			
			levelToUpdate.setLevel(levelToAddLabel);
		}
		
		levelToUpdate.setBankHourEnabled(toAdd.getBankHourEnabled());
		levelToUpdate.setExtraWorkPaid(toAdd.getExtraWorkPaid());
		levelToUpdate.setMonthlyLeaveHours(toAdd.getMonthlyLeaveHours());
		levelToUpdate.setMonthlyVacationDays(toAdd.getMonthlyVacationDays());
		
		if(!levelToUpdate.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		levelToUpdate = this.userLevelService.saveOrUpdate(levelToUpdate);
		
		return this.buildOkResponse(UserLevelTransformer.mapToDTO(levelToUpdate));
	}

	
	
	public GenericResponse<Page<UserLevelDTO>> findAll(String levelFilter, Pageable pageable){
		if(pageable==null) {
			throw new NullPointerException();
		}
		
		Pair<List<UserLevel>, Long> levelAndCount = this.userLevelService.findAllAndCount(levelFilter, pageable);
		
		List<UserLevelDTO> res = UserLevelTransformer.mapToDTO(levelAndCount.getKey());
		long totalCount = levelAndCount.getValue();
		
		return this.buildPageableOkResponse(res, totalCount, pageable);
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<StringDTO> delete(Integer levelId) {
		if(levelId==null) {
			return this.buildBadDataResponse();
		}
		
		UserLevel levelToDelete = this.userLevelService.findById(levelId);
		
		if(levelToDelete==null) {
			return this.buildNotFoundResponse();
		}
		
		boolean isUsedByAtLeastAUser = this.userProfileContractInfoService.checkIfAnyUserHasTheLevel(levelToDelete);
		
		if(isUsedByAtLeastAUser) {
			return this.buildUnprocessableEntity("Some user has the level and cannot be deleted");
		}
		
		this.userLevelService.delete(levelToDelete);
		
		return this.buildStringOkResponse("Successfully deleted");
	}

	
	
	
	
}
