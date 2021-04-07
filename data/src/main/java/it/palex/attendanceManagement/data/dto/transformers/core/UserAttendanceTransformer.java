package it.palex.attendanceManagement.data.dto.transformers.core;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.core.UserAttendanceDTO;
import it.palex.attendanceManagement.data.dto.transformers.UserProfileTransformer;
import it.palex.attendanceManagement.data.entities.core.UserAttendance;

public class UserAttendanceTransformer {

	public static UserAttendanceDTO mapToDTO(UserAttendance attendance) {
		if(attendance==null) {
			return null;
		}
		UserAttendanceDTO res = new UserAttendanceDTO();
		res.setId(attendance.getId());
		res.setTimestamp(attendance.getTimestamp());
		res.setType(attendance.getType());
		
		res.setUserProfile(UserProfileTransformer.mapToSmallDTO(attendance.getUserProfile()));
		res.setTurnstile(TurnstileTransformer.mapToDTO(attendance.getTurnstile()));
		
		
		return res;
	}
	
	
	public static List<UserAttendanceDTO> mapToDTO(List<UserAttendance> attendances) {
		if(attendances==null) {
			return null;
		}
		
		List<UserAttendanceDTO> res = new ArrayList<>(attendances.size());
		
		for (UserAttendance attendance : attendances) {
			res.add(mapToDTO(attendance));
		}
		
		return res;
	}
	
	
	
	
	
}
