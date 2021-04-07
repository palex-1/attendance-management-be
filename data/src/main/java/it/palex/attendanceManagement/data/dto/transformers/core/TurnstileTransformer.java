package it.palex.attendanceManagement.data.dto.transformers.core;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.core.TurnstileDTO;
import it.palex.attendanceManagement.data.entities.core.Turnstile;

public class TurnstileTransformer {

	public static TurnstileDTO mapToDTO(Turnstile turnstile) {
		if(turnstile==null) {
			return null;
		}
		TurnstileDTO dto = new TurnstileDTO();
		
		dto.setId(turnstile.getId());
		dto.setDeactivated(turnstile.getDeactivated());
		dto.setDescription(turnstile.getDescription());
		dto.setPosition(turnstile.getPosition());
		dto.setTitle(turnstile.getTitle());
		dto.setType(turnstile.getType());
		
		return dto;
	}
	
	
	public static List<TurnstileDTO> mapToDTO(List<Turnstile> turnstiles) {
		if(turnstiles==null) {
			return null;
		}
		
		List<TurnstileDTO> res = new ArrayList<TurnstileDTO>(turnstiles.size());
		
		for (Turnstile turnstile : turnstiles) {
			res.add(mapToDTO(turnstile));
		}
		
		return res;
	}
	
	
}
