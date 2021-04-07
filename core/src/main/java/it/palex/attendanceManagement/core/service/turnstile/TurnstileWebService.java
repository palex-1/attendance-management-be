package it.palex.attendanceManagement.core.service.turnstile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.core.dtos.turnstile.TurnstileAddRequest;
import it.palex.attendanceManagement.core.dtos.turnstile.TurnstileUpdateRequest;
import it.palex.attendanceManagement.data.dto.core.TurnstileDTO;
import it.palex.attendanceManagement.data.dto.transformers.core.TurnstileTransformer;
import it.palex.attendanceManagement.data.entities.core.Turnstile;
import it.palex.attendanceManagement.data.service.core.TurnstileService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;

@Service
public class TurnstileWebService implements GenericService {

	@Autowired
	private TurnstileService turnstileService;
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<TurnstileDTO> addNewTurnstile(TurnstileAddRequest toAdd) {
		if(toAdd==null || toAdd.getTitle()==null || toAdd.getDescription()==null || toAdd.getPosition()==null) {
			return this.buildBadDataResponse();
		}
		
		Turnstile toCreate = new Turnstile();
		toCreate.setDeactivated(toAdd.getDeactivated());
		toCreate.setDescription(toAdd.getDescription());
		toCreate.setPosition(toAdd.getPosition());
		toCreate.setTitle(toAdd.getTitle());
		toCreate.setType(toAdd.getType());
		
		if(!toCreate.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		toCreate = this.turnstileService.saveOrUpdate(toCreate);
				
		TurnstileDTO dto = TurnstileTransformer.mapToDTO(toCreate);
		
		return this.buildOkResponse(dto);
	}
	
	
	public GenericResponse<Page<TurnstileDTO>> findAllTurnstile(String title, String position, 
			String description, Boolean includeDisabled, Pageable pageable){
		if(pageable==null) {
			return this.buildBadDataResponse();
		}
		
		Pair<List<Turnstile>, Long> listAndCount = this.turnstileService.findAllAndCount(title, position, description,
				BooleanUtils.toBoolean(includeDisabled), pageable);
		
		long totalCount = listAndCount.getValue();
		List<TurnstileDTO> res = new ArrayList<>(listAndCount.getKey().size());
		
		for(Turnstile turnstile: listAndCount.getKey()) {
			res.add(TurnstileTransformer.mapToDTO(turnstile));
		}
		
		
		return this.buildPageableOkResponse(res, totalCount, pageable);
	}
	
	
	public GenericResponse<TurnstileDTO> findTurnstileDetails(Long turnstileId) {
		if(turnstileId==null) {
			return this.buildBadDataResponse();
		}
		
		Turnstile turnstile = this.turnstileService.findById(turnstileId);
		
		if(turnstile==null) {
			return this.buildNotFoundResponse();
		}
				
		TurnstileDTO dto = TurnstileTransformer.mapToDTO(turnstile);
		
		return this.buildOkResponse(dto);
	}
	
	public GenericResponse<TurnstileDTO> deactivateTurnstile(Long turnstileId){
		if(turnstileId==null) {
			return this.buildBadDataResponse();
		}
		
		Turnstile turnstile = this.turnstileService.findById(turnstileId);
		
		if(turnstile==null) {
			return this.buildNotFoundResponse();
		}
		
		turnstile.setDeactivated(true);
		this.turnstileService.saveOrUpdate(turnstile);	
		
		TurnstileDTO dto = TurnstileTransformer.mapToDTO(turnstile);
		
		return this.buildOkResponse(dto);
	}

	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<TurnstileDTO> updateTurnstile(TurnstileUpdateRequest turnstile) {
		if(turnstile==null || turnstile.getId()==null) {
			return this.buildBadDataResponse();
		}
		
		Turnstile toUpdate = this.turnstileService.findById(turnstile.getId());
		
		if(toUpdate==null) {
			return this.buildNotFoundResponse();
		}
		
		toUpdate.setDeactivated(turnstile.getDeactivated());
		toUpdate.setDescription(turnstile.getDescription());
		toUpdate.setPosition(turnstile.getPosition());
		toUpdate.setTitle(turnstile.getTitle());
		
		if(!toUpdate.canBeInsertedInDatabase()) {
			return this.buildBadDataResponse();
		}
		
		toUpdate = this.turnstileService.saveOrUpdate(toUpdate);
				
		TurnstileDTO dto = TurnstileTransformer.mapToDTO(toUpdate);
		
		return this.buildOkResponse(dto);
	}
	
	
	
}
