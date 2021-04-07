package it.palex.attendanceManagement.data.entities.enumTypes;

import java.util.ArrayList;

public enum CompanyBranchType {
	COMPANY_OFFICE, EXTERNAL_OFFICE;
	
	public static ArrayList<CompanyBranchType> getAll(){
		ArrayList<CompanyBranchType> list = new ArrayList<>();
		CompanyBranchType[] tipi = CompanyBranchType.values();
		for(int i=0;i<tipi.length;i++) {
			list.add(tipi[i]);
		}
		return list;
	}
	
	public static boolean isValid(String value) {
		if(value==null) {
			return false;
		}
		try {
			CompanyBranchType.valueOf(value);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
	
}
