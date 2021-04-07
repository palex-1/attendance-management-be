package it.palex.attendanceManagement.data.dto.transformers.core;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.core.ExpenseReportElementDTO;
import it.palex.attendanceManagement.data.entities.core.ExpenseReportElement;

public class ExpenseReportElementTransformer {

	public static ExpenseReportElementDTO mapToDTO(ExpenseReportElement elem) {
		if(elem==null) {
			return null;
		}
		ExpenseReportElementDTO res = new ExpenseReportElementDTO();
		res.setAccepted(elem.getAccepted());
		res.setAmount(elem.getAmount());
		res.setDescription(elem.getDescription());
		res.setId(elem.getId());
		
		res.setAttachment(DocumentTransformer.mapToDTO(elem.getAttachment()));
		
		if(elem.getExpenseReport()!=null) {
			res.setExpenseReportId(elem.getExpenseReport().getId());
		}		
		
		return res;
	}
	
	
	public static List<ExpenseReportElementDTO> mapToDTO(List<ExpenseReportElement> list){
		if(list==null) {
			return null;
		}
		
		List<ExpenseReportElementDTO> res = new ArrayList<ExpenseReportElementDTO>(list.size());
		
		for (ExpenseReportElement expenseReportElement : list) {
			res.add(mapToDTO(expenseReportElement));
		}
		
		return res;
	}
	
}
