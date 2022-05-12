package it.palex.attendanceManagement.data.dto.transformers;

import java.util.ArrayList;
import java.util.List;

import it.palex.attendanceManagement.data.dto.core.UserNotificationDTO;
import it.palex.attendanceManagement.data.entities.core.UserNotification;

public class UserNotificationTransformer {
	public static List<UserNotificationDTO> mapToDTO(List<UserNotification> notifications){
		if(notifications==null) {
			return null;
		}
		List<UserNotificationDTO> res = new ArrayList<>(notifications.size());
		
		for(UserNotification notification : notifications) {
			res.add(mapToDTO(notification));
		}
		
		return res;
	}
	
	public static UserNotificationDTO mapToDTO(UserNotification notification) {
		if(notification==null) {
			return null;
		}
		UserNotificationDTO res = new UserNotificationDTO();
		res.setId(notification.getId());
		res.setText(notification.getText());
		res.setTitle(notification.getTitle());
		res.setLandingPage(notification.getLandingPage());
		res.setTargetId(notification.getTargetId());
		res.setTargetSubId(notification.getTargetSubId());
		
		return res;
	}
}
