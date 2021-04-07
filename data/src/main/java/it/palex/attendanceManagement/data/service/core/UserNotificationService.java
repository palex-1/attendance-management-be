package it.palex.attendanceManagement.data.service.core;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.dto.core.UserNotificationDTO;
import it.palex.attendanceManagement.data.dto.transformers.UserNotificationTransformer;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.QUserNotification;
import it.palex.attendanceManagement.data.entities.core.UserNotification;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.UserNotificationRepository;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.IterableUtils;

@Service
public class UserNotificationService implements GenericService {

	private final QUserNotification QUN = QUserNotification.userNotification;
	
	@Autowired
	private UserNotificationRepository userNotificationRepo;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticateduserService;
	
	
	public UserNotification saveOrUpdate(UserNotification notification) {
		if(notification==null) {
			throw new NullPointerException();
		}
		if(!notification.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(notification);
		}
		
		return this.userNotificationRepo.save(notification);
	}
	
	
	public GenericResponse<Page<UserNotificationDTO>> findAllMyNotification(
			Pageable pageable){
		if(pageable==null) {
			return this.buildBadDataResponse();
		}
		
        UserProfile profile = this.currentAuthenticateduserService.getCurrentAuthenticatedUserProfile();
        
        if(profile==null) {
        	return this.buildUnauthorizedResponse();
        }
        
        BooleanBuilder cond = new BooleanBuilder();
        cond.and(QUN.userProfile.id.eq(profile.getId()));
        
		
		long totalCount = this.userNotificationRepo.count(cond);
		
		List<UserNotification> userNotifications = 
				IterableUtils.iterableToList(this.userNotificationRepo.findAll(cond, pageable));
		
		
		return this.buildPageableOkResponse(UserNotificationTransformer.mapToDTO(userNotifications), totalCount, pageable);
	}


	public int deleteAllNotificationSentBefore(Date date) {
		return this.userNotificationRepo.deleteAllNotificationCreatedBefore(date);
	}
	
	
	
	
	
}
