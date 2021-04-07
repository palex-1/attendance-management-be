package it.palex.attendanceManagement.commons.messaging.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.UUID;

import javax.activation.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import it.palex.attendanceManagement.commons.messaging.Attachment;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.exceptions.InvalidConfigurationException;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.library.utils.StringUtility;

public class SendGridEmailSenderService implements EmailSenderService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SendGridEmailSenderService.class);

	@Autowired
    private GlobalConfigurationsService globalConfigurationService;
	
	
	private String getFromAddress() {
		String fromAddress = globalConfigurationService.getConfigValue(
				GlobalConfigurationSettingsTuple.PROFILE_SENDGRID.AREA_NAME, GlobalConfigurationSettingsTuple.PROFILE_SENDGRID.MAIL_FROM);
				
		if (StringUtility.isEmptyOrWhitespace(fromAddress)) {
			throw new InvalidConfigurationException("Invalid configuration of area:"
							+ GlobalConfigurationSettingsTuple.PROFILE_SENDGRID.AREA_NAME+"\n "
							+ "key:"+GlobalConfigurationSettingsTuple.PROFILE_SENDGRID.MAIL_FROM);
		}
		
		return fromAddress;
	}
	
	private SendGrid getSendGridClient() {
		String apiKey = globalConfigurationService.getConfigValue(
				GlobalConfigurationSettingsTuple.PROFILE_SENDGRID.AREA_NAME, GlobalConfigurationSettingsTuple.PROFILE_SENDGRID.API_KEY);
				
		if (StringUtility.isEmptyOrWhitespace(apiKey)) {
			throw new InvalidConfigurationException("Invalid configuration of area:"
					+ GlobalConfigurationSettingsTuple.PROFILE_SENDGRID.AREA_NAME+"\n "
					+ "key:"+GlobalConfigurationSettingsTuple.PROFILE_SENDGRID.API_KEY);		}
		
		SendGrid sg = new SendGrid(apiKey);
	
		return sg;
	}
 
	

	@Override
	public void sendEmail(it.palex.attendanceManagement.commons.messaging.Email customEmail) throws Exception {
		
	    Personalization personalization = new Personalization();
	    personalization.setSubject(customEmail.getSubject());
	    
	    if(customEmail.getToAddress()!=null && customEmail.getToAddress().size()>0){
			for (String destPark : customEmail.getToAddress()) {
				Email to = new Email(destPark);
				personalization.addTo(to);
			}
		}else{
			throw new IllegalArgumentException("To address not found");
		}
	    
	    if(customEmail.getToCC()!=null){
			for (String destParkCC : customEmail.getToCC()) {
				Email cc = new Email(destParkCC);
				personalization.addCc(cc);
			}
		}
		
		// Set To BCC: header field of the header.
		if(customEmail.getToBcc()!=null){
			for (String destParkBCC : customEmail.getToBcc()) {
				Email bcc = new Email(destParkBCC);
				personalization.addBcc(bcc);
			}
		}
		
		
		
	    Mail mail = new Mail();
	    //add personalization as to,bcc, cc
	    mail.addPersonalization(personalization);
	    
	    Email from = new Email(this.getFromAddress());
	    mail.setFrom(from);
	    
	    //set reply to email
	    Email replyTo = new Email(this.getFromAddress());
	    mail.setReplyTo(replyTo);
	    
	    //create and add email content
	    Content content = new Content("text/html", customEmail.getText());
	    mail.addContent(content);
	    
	    //set sent date
	    Calendar c = GregorianCalendar.getInstance();
	    mail.setSendAt(c.getTimeInMillis()/1000);
	    
	    
	    
	    InputStream stream = GmailEmailSenderService.class.getClassLoader().getResourceAsStream("logo.png");
		
		if(stream!=null) {
			Attachments companyLogo = new Attachments(); 
			byte[] logoData = IOUtils.toByteArray(stream);
			byte[] logoBase64 = Base64.getEncoder().encode(logoData);
			
			companyLogo.setContentId("companyLogo");
			companyLogo.setType("image/png");
			companyLogo.setDisposition("inline");
			companyLogo.setFilename(UUID.randomUUID().toString());
			companyLogo.setContent(new String(logoBase64, 0, (int) logoBase64.length, "UTF-8"));
			
			mail.addAttachments(companyLogo);
		}

		if(customEmail.getImageResourcesToShow()!=null && !customEmail.getImageResourcesToShow().isEmpty()) {
			Set<String> contentIds = customEmail.getImageResourcesToShow().keySet();
			
			for(String contentId: contentIds) {
				Attachments imageAttachment = new Attachments(); 
				DataSource dcPark = customEmail.getImageResourcesToShow().get(contentId);
				
				byte[] dataImage = IOUtils.toByteArray(dcPark.getInputStream());
				byte[] dataImageBase64 = Base64.getEncoder().encode(dataImage);
				
				imageAttachment.setContentId(contentId);
				imageAttachment.setType("image/png");
				imageAttachment.setDisposition("inline");
				imageAttachment.setFilename(UUID.randomUUID().toString());
				imageAttachment.setContent(new String(dataImageBase64, 0, (int) dataImageBase64.length, "UTF-8"));
				
				mail.addAttachments(imageAttachment);
			}
			
		}
	    
		if (customEmail.getAttachment() != null && !customEmail.getAttachment().isEmpty()) {
			// Create a multipar message
			
			
			// inserisci tutti gli allegati
			for (Attachment daAllegare : customEmail.getAttachment()) {				

				if (daAllegare != null) {
					Attachments attachment = new Attachments(); 
					byte[] attachmentData = IOUtils.toByteArray(daAllegare.getInputStream());
					attachment.setContent(new String(attachmentData, 0, (int) attachmentData.length, "UTF-8"));
					attachment.setFilename(daAllegare.getFullName());
					attachment.setType(daAllegare.getMimeType());
					attachment.setDisposition("attachment");
					attachment.setContentId(UUID.randomUUID().toString());
					
					mail.addAttachments(attachment);
				}
			}

		}
	    
		SendGrid sg = getSendGridClient();
	    
		Request request = new Request();
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      
	      Response response = sg.api(request);
	      
	      //if the status code is invalid
	      if(response.getStatusCode()<200 || response.getStatusCode()>299) {
	    	  throw new RuntimeException("Error sending email. code:"+response.getStatusCode()+""
	    	  		+ "\n response:"+response.getBody());
	      }
	      
	    } catch (IOException ex) {
	    	LOGGER.error("Error sending email with sendgrid:", ex);
	      throw ex;
	    }
	    
	}
	
}
