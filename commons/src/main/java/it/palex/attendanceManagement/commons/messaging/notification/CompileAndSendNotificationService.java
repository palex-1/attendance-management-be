package it.palex.attendanceManagement.commons.messaging.notification;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.commons.messaging.firebase.FCMSingleRecipientNotification;
import it.palex.attendanceManagement.commons.messaging.firebase.NotificationData;
import it.palex.attendanceManagement.commons.messaging.strategies.MessageCompileStrategy;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.FcmUserToken;
import it.palex.attendanceManagement.data.entities.core.MessageTemplate;
import it.palex.attendanceManagement.data.entities.core.UserNotification;
import it.palex.attendanceManagement.data.service.core.FcmUserTokenService;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.service.core.MessageTemplateService;
import it.palex.attendanceManagement.data.service.core.UserNotificationService;
import it.palex.attendanceManagement.data.service.core.UserProfileSettingService;

@Component
public class CompileAndSendNotificationService {

	public static final int TEXT_MAX_SIZE = 200;
	public static final int TITLE_MAX_SIZE = 60;
	public static final String DEFAULT_SOUND = "default";
	
	@Autowired
	private MessageTemplateService messageTemplateSrv;
	
    @Autowired
    private GlobalConfigurationsService globalConfigurationService;
    
    @Autowired
    private NotificationSenderService notificationSender;
    
    @Autowired
	private FcmUserTokenService fcmUserTokenService;
    
    @Autowired
    private UserNotificationService userNotificationService;
    
    @Autowired
	private UserProfileSettingService userProfileSettingService;
    
    /**
     * 
     * @param profile
     * @return true if user has notification enabled
     */
    private boolean userHasPushNotificationEnabled(UserProfile profile) {
    	if(profile==null) {
    		return false;
    	}
    	return this.userProfileSettingService.checkIfPushNotificationAreEnabled(profile);
    }

    
    private void compileAndSendSingleRecipient(MessageTemplate template, 
    		MessageCompileStrategy compiler, List<FcmUserToken> tokenList, UserProfile recipient,
    		NotificationData data) throws Exception {
		
    	sendSingleRecipient(template.getSubject(), compiler.compileMsg(), 
    			tokenList, recipient, data);
	}
    
    private void sendSingleRecipient(String title, String messageBody, 
    		String token, UserProfile recipient, NotificationData data) throws Exception {
    	FCMSingleRecipientNotification notification = new FCMSingleRecipientNotification();

    	notification.setToken(token);
    	notification.setBody(messageBody);
    	notification.setTitle(title);
    	notification.setData(data);
    	notification.setBody(StringUtils.substring(notification.getBody(), 0, TEXT_MAX_SIZE));
    	notification.setTitle(StringUtils.substring(notification.getTitle(), 0, TITLE_MAX_SIZE));
    	notification.setSound(DEFAULT_SOUND);
    	
    	this.notificationSender.sendNotification(notification);
    }
    
    
    private void sendSingleRecipient(String title, String messageBody, 
    		List<FcmUserToken> tokenList, UserProfile recipient, NotificationData data) throws Exception {
    	if(recipient!=null && data!=null) {
    		this.saveNotificationSent(recipient, messageBody, title, 
    				data.getLandingPage(), data.getTargetId(), data.getTargetSubId());
    	}
    	
    	for(FcmUserToken token: tokenList) {
    		this.sendSingleRecipient(title, messageBody, 
    	    		token.getToken(), recipient, data);
    	}
    }
    
    private void saveNotificationSent(UserProfile recipient, 
    		String notificationBody, String notificationTitle, String landingPage,
    		String targetId, String targetSubId) {
    	UserNotification toAdd = new UserNotification();
    	toAdd.safeSetText(notificationBody);
    	toAdd.safeSetTitle(notificationTitle);
    	toAdd.setUserProfile(recipient);
    	toAdd.setLandingPage(landingPage);
    	toAdd.setTargetId(targetId);
    	toAdd.setTargetSubId(targetSubId);
    	
    	this.userNotificationService.saveOrUpdate(toAdd);
    }
    
  
       
}
