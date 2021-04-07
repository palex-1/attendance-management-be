package it.palex.attendanceManagement.library.rest;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.palex.attendanceManagement.library.exception.BadDataException;
import it.palex.attendanceManagement.library.rest.dtos.DTO;
import it.palex.attendanceManagement.library.utils.HttpCodes;


public abstract class RestEndpoint {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RestEndpoint.class);
	
	private static final int SUCC_RESPONSE_CODE = 200;
	private static final int INTERNAL_ERROR_CODE = 500;
	private static final String DEFAULT_SUCC_MESSAGE = "The request has been fulfilled";
	private static final String DEFAULT_INTERNAL_ERROR = "Internal Server Error";
	
	
	protected <T> T jsonStringToObject(String content, Class<T> genericClass){
		if(content==null || genericClass==null) {
			return null;
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
			
			return objectMapper.readValue(content, genericClass);
		}catch(Exception e) {
			throw new BadDataException();
		}		
	}
	
	
	protected <T extends DTO> ResponseEntity<GenericResponse<T>> buildSuccessResponse(T data, String version){
		return this.buildResponse(data, version, SUCC_RESPONSE_CODE, DEFAULT_SUCC_MESSAGE);
	}
	
	protected <T extends DTO> ResponseEntity<GenericResponse<T>> buildSuccessResponse(GenericResponse<T> response, String version){
		response.setVersion(version);
		
		return this.buildGenericResponse(response);
	}

	protected <T extends DTO> ResponseEntity<GenericResponse<T>> buildSuccessResponse(T data, String version, String message){
		return this.buildResponse(data, version, SUCC_RESPONSE_CODE, message);
	}
	
	/**
	 * @param e
	 * @param apiVersion
	 * @return
	 */
	protected <T> ResponseEntity<GenericResponse<T>> buildBadRequestError(Exception e,
			String apiVersion) {
		if(e!=null && e.getMessage()!=null){
			return this.buildResponse(null, apiVersion, HttpCodes.BAD_REQUEST, e.getMessage());
		}
		return this.buildResponse(null, apiVersion, HttpCodes.BAD_REQUEST, "bad request");
	}
	
	/**
	 * @param e
	 * @param apiVersion
	 * @return
	 */
	protected <T> ResponseEntity<GenericResponse<T>> buildBadRequestError(
			String apiVersion) {
		return this.buildResponse(null, apiVersion, HttpCodes.BAD_REQUEST, "bad request");
	}
		
	protected <T> ResponseEntity<GenericResponse<T>> buildInternalErrorResponse(Throwable t, String version){
		if(t!=null && t.getMessage()!=null){
			ResponseEntity<GenericResponse<T>> res = this.buildResponse(null, version, INTERNAL_ERROR_CODE, t.getMessage());
			if(res.getBody()!=null) {
				LOGGER.error("id: "+res.getBody().getOperationIdentifier(), t);
			}
			return res;
		}
		
		ResponseEntity<GenericResponse<T>> res = this.buildResponse(null, version, INTERNAL_ERROR_CODE, DEFAULT_INTERNAL_ERROR);
		if(res.getBody()!=null) {
			LOGGER.error("id: "+res.getBody().getOperationIdentifier(), t);
		}
		return res;
	}
	
	protected <T> ResponseEntity<GenericResponse<T>> buildInternalErrorResponse(Throwable t){
		return this.buildInternalErrorResponse(t, null);
	}
	
	private <T> ResponseEntity<GenericResponse<T>> buildResponse(T data, String version, int responseCode, String message){
		GenericResponse<T> park = new GenericResponse<>();
		park.setVersion(version);
		park.setData(data);
		park.setCode(responseCode);
		park.setMessage(message);
		
		return this.buildGenericResponse(park);
	}
	
	
	protected <T> ResponseEntity<GenericResponse<T>> buildGenericResponse(GenericResponse<T> response) {
		if(response==null){
			return ResponseEntity.ok().build();
		}
		return ResponseEntity
					.status(response.getCode())
						.body(response);
	}
	
	protected <T> ResponseEntity<GenericResponse<T>> buildGenericResponse(GenericResponse<T> response, String apiVersion) {
		if(response==null){
			return ResponseEntity.ok().build();
		}
		response.setVersion(apiVersion);
		
		return this.buildGenericResponse(response);
	}
	
	protected <T> ResponseEntity<GenericResponse<Page<T>>> buildGenericPagingResponse(GenericResponse<Page<T>> response, String apiVersion) {
		if(response==null){
			throw new NullPointerException();
		}
		response.setVersion(apiVersion);
		
		return ResponseEntity
				.status(response.getCode())
					.body(response);
	}
	
}
