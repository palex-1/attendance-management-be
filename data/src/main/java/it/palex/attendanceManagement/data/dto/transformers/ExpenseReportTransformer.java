package it.palex.attendanceManagement.data.dto.transformers;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.core.ExpenseReportDTO;
import it.palex.attendanceManagement.data.entities.core.ExpenseReport;

public class ExpenseReportTransformer {

	public static ExpenseReportDTO mapToDTO(ExpenseReport report){
		if(report==null) {
			return null;
		}
		ExpenseReportDTO res = new ExpenseReportDTO();
		res.setAmount(report.getAmount());
		res.setAmountAccepted(report.getAmountAccepted());
		res.setDateOfExpence(report.getDateOfExpence());
		res.setId(report.getId());
		res.setLocation(report.getLocation());
		res.setTitle(report.getTitle());
		res.setStatus(report.getStatus());
		res.setNotes(report.getNotes());
		
		res.setProcessingBy(UserProfileTransformer.mapToSmallDTO(report.getProcessingBy()));
		res.setProcessedBy(UserProfileTransformer.mapToSmallDTO(report.getProcessedBy()));
		res.setMadeBy(UserProfileTransformer.mapToSmallDTO(report.getMadeBy()));
		
		return res;
	}

	
	public static List<ExpenseReportDTO> mapToDTO(List<ExpenseReport> list){
		if(list==null) {
			return null;
		}
		List<ExpenseReportDTO> res = new ArrayList<ExpenseReportDTO>(list.size());
		
		for (ExpenseReport expenseReport : list) {
			res.add(mapToDTO(expenseReport));
		}
		
		return res;
	}
	
	
}
