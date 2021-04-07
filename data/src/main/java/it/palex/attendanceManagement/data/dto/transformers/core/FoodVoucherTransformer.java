package it.palex.attendanceManagement.data.dto.transformers.core;

import it.palex.attendanceManagement.data.dto.core.FoodVoucherRequestDTO;
import it.palex.attendanceManagement.data.entities.FoodVoucherRequest;

public class FoodVoucherTransformer {

	public static FoodVoucherRequestDTO mapToDTO(FoodVoucherRequest req) {
		if(req==null) {
			return null;
		}
		FoodVoucherRequestDTO dto = new FoodVoucherRequestDTO();
		dto.setDay(req.getDay());
		dto.setEditable(req.getEditable());
		dto.setId(req.getId());
		
		return dto;
	}
}
