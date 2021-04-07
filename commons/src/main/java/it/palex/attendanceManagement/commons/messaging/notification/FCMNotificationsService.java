package it.palex.attendanceManagement.commons.messaging.notification;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.palex.attendanceManagement.commons.messaging.firebase.FCMNotificationRequest;
import it.palex.attendanceManagement.commons.messaging.firebase.FCMSingleRecipientNotification;
import it.palex.attendanceManagement.commons.messaging.firebase.FCMTopicNotification;
import it.palex.attendanceManagement.commons.messaging.notification.firebase.FirebaseNotification;
import it.palex.attendanceManagement.commons.messaging.notification.firebase.NotificationBody;
import it.palex.attendanceManagement.commons.utils.ErrorCodeFCM;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedProvidersEnum;
import it.palex.attendanceManagement.data.service.core.FcmUserTokenService;


public class FCMNotificationsService implements NotificationSenderService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FCMNotificationsService.class);

	
	@Value("${firebase.server.key:NULL}")
	private String firebaseServerKey;
	
	@Value("${firebase.api.url:NULL}")
	private String firebaseApiUrl;
	
	@Autowired
	private FcmUserTokenService fcmUserTokenService;
	
	public static final String AUTH_HEADER = "Authorization";
	public static final String CONTENT_TYPE_HEADER = "Content-Type";
	
	public static final String CONTENT_TYPE_HEADER_VALUE = "application/json;charset=UTF-8";
	
		
	private Logger logger = LoggerFactory.getLogger(getClass());

	private String firebaseKey = "";
	
	@PostConstruct
	private void initializekey() {
		this.firebaseKey = "key=" + this.firebaseServerKey;
	}
	
	/**
	 * 
	 * @param request
	 * @return true if notification is sent successfully false otherwise
	 */
	public boolean sendNotification(FCMNotificationRequest request) {
		if(request instanceof FCMSingleRecipientNotification) {
			return this.sendToNotificationSingle((FCMSingleRecipientNotification) request);
		}
		if(request instanceof FCMTopicNotification) {
			return this.sendToNotificationTopic((FCMTopicNotification)request);
		}
		throw new UnsupportedOperationException();
	}
	
	private boolean sendToNotificationSingle(FCMSingleRecipientNotification fcmRequest) {
		return this.sendNotificationTo(fcmRequest.getToken(), fcmRequest);
	}
	
	private boolean sendToNotificationTopic(FCMTopicNotification fcmRequest) {
		return this.sendNotificationTo("/topics/"+fcmRequest.getTopic(), fcmRequest);
	}
	
	private boolean sendNotificationTo(String dest, FCMNotificationRequest fcmRequest) {
		if (fcmRequest == null || fcmRequest.getTitle()==null || fcmRequest.getBody()==null) {
			throw new NullPointerException();
		}

		NotificationBody body = new NotificationBody();
		body.setTo(dest);
		body.setPriority(fcmRequest.getPriority());
		
		FirebaseNotification notification = new FirebaseNotification();
		notification.setTitle(fcmRequest.getTitle());
		notification.setBody(fcmRequest.getBody());
		notification.setSound(fcmRequest.getSound());
		
		if( fcmRequest.getIcon()!=null) {
			notification.setIcon(fcmRequest.getIcon());
		}
		
		body.setNotification(notification);
		body.setData(fcmRequest.getData());
			
		ObjectMapper mapper = new ObjectMapper();
	 
	    
		try {
			String bodyAsString = mapper.writeValueAsString(body);    
			
			HttpEntity<String> request = new HttpEntity<>(bodyAsString);
			
		    this.send(request, dest);
		    
			return true;
		} catch (Exception e) {
			logger.error("FCM Push Notification ERROR! ", e);
			return false;
		}
	}
	
	
	@Async
	private Future<FcmNotificationSendResult> send(HttpEntity<String> entity,String token) {

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters()
        .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		
		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor(AUTH_HEADER, this.firebaseKey));
		interceptors.add(new HeaderRequestInterceptor(CONTENT_TYPE_HEADER, CONTENT_TYPE_HEADER_VALUE));
		restTemplate.setInterceptors(interceptors);

		String firebaseResponse = restTemplate
				.postForObject(firebaseApiUrl, entity, String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		FcmNotificationSendResult res = null;
		
		try {
			res = objectMapper.readValue(firebaseResponse, FcmNotificationSendResult.class);
			if(res!=null && res.getFailure()!=null && res.getFailure().intValue()>0) {
				
				for (int i=0; i<res.getResults().length; i++) {
					firebaseResponse=res.getResults()[i].getError();
					
					if(ErrorCodeFCM.NotRegistered.name().equals(firebaseResponse)) {
						
						boolean deleted = this.fcmUserTokenService.deleteByTokenAndProvider(token, SupportedProvidersEnum.FIREBASE.name());
						
						if(deleted) {
							LOGGER.info("deleted firebase token:"+token);
						}
					}
				}
				
							
				LOGGER.warn("Error sending push notification to user:"+firebaseResponse.toString());
			}
				
			
			if(res!=null && res.getFailure()!=null && res.getFailure().intValue()>0) {
				LOGGER.warn("Error sending push notification to user:"+firebaseResponse.toString());
			}
		} catch (Exception e) {
			LOGGER.warn("Error sending push notification to user:"+e);
		} 
		
		return new AsyncResult<FcmNotificationSendResult>(res);
	}

}
