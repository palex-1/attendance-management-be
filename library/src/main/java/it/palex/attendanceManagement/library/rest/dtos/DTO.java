package it.palex.attendanceManagement.library.rest.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface DTO extends Serializable {

	default String toJsonString() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		return objectMapper.writeValueAsString(this);
	}
	
}