package it.palex.attendanceManagement.data.dto.transformers;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.impiegato.PaycheckDTO;
import it.palex.attendanceManagement.data.entities.core.Paycheck;

public class PaycheckTransformer {
	
	public static PaycheckDTO mapToDTO(Paycheck paycheck) {
		if(paycheck==null) {
			return null;
		}
		PaycheckDTO res = new PaycheckDTO();
		res.setId(paycheck.getId());
		res.setMonth(paycheck.getMonth());
		res.setSendEmailDate(paycheck.getSendEmailDate());
		res.setTitle(paycheck.getTitle());
		res.setYear(paycheck.getYear());
		
		return res;
	}
	
	public static List<PaycheckDTO> mapToDTO(List<Paycheck> paychecks){
		if(paychecks==null) {
			return null;
		}
		List<PaycheckDTO> res = new ArrayList<>(paychecks.size());
		
		for (Paycheck paycheck : paychecks) {
			res.add(mapToDTO(paycheck));
		}
		
		return res;
	}
	
}
