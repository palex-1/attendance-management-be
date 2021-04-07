package it.palex.attendanceManagement.commons.messaging.sms;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import it.palex.attendanceManagement.data.entities.GlobalConfigurations;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.exceptions.InvalidConfigurationException;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.utils.GlobalConfigurationUtils;
import it.palex.attendanceManagement.library.utils.StringUtility;

public class TwilioSmsSenderService implements SmsSenderService{

	private String accountSid;	
	private String authToken;
    private String twilioNumber; 
    
    @Autowired
    private GlobalConfigurationsService globalConfigurationService;
    
    
    @PostConstruct
    private void inizialize() {
    	this.reinit();
    }
    
    private void reinit() {
    	List<GlobalConfigurations> twilioList = this.globalConfigurationService.findAllByArea(
    			GlobalConfigurationSettingsTuple.PROFILE_TWILIO.AREA_NAME);
    	
    	this.accountSid = GlobalConfigurationUtils.getValueSkippingArea(twilioList, GlobalConfigurationSettingsTuple.PROFILE_TWILIO.ACCOUNT_SID );
    	this.authToken = GlobalConfigurationUtils.getValueSkippingArea(twilioList, GlobalConfigurationSettingsTuple.PROFILE_TWILIO.AUTH_TOKEN);
    	this.twilioNumber = GlobalConfigurationUtils.getValueSkippingArea(twilioList, GlobalConfigurationSettingsTuple.PROFILE_TWILIO.TWILIO_NUMBER);
	
    	if( StringUtility.isEmptyOrWhitespace(this.accountSid)  || StringUtility.isEmptyOrWhitespace(this.authToken) ||
    			StringUtility.isEmptyOrWhitespace(this.twilioNumber)) {
    		throw new InvalidConfigurationException("Problems with "+GlobalConfigurationSettingsTuple.PROFILE_TWILIO.AREA_NAME);
    	}
    }
    
    
    @Override
    public void sendSMS(String sendNumber, String msg) throws Exception {
    	if(sendNumber==null || msg==null) {
    		throw new NullPointerException();
    	}
     	
        Twilio.init(accountSid, authToken);
        final Message message = Message.creator(
                new com.twilio.type.PhoneNumber(sendNumber),//The phone number you are sending text to
                new com.twilio.type.PhoneNumber(twilioNumber),//The Twilio phone number
                msg).create();
        
    }
    
    
    
    
}
