package it.palex.attendanceManagement.commons.messaging.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.palex.attendanceManagement.commons.messaging.Attachment;
import it.palex.attendanceManagement.commons.messaging.Email;
import it.palex.attendanceManagement.data.entities.GlobalConfigurations;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.utils.GlobalConfigurationUtils;
import it.palex.attendanceManagement.library.utils.GenericValidator;
import it.palex.attendanceManagement.library.utils.StringUtility;
import net.sf.ehcache.config.InvalidConfigurationException;
import org.springframework.stereotype.Component;

@Component("SmtpEmailSender")
public class SmtpEmailSenderService implements EmailSenderService {

	public static final String COMPONENT_BEAN_NAME = "SmtpEmailSender";

	private static final Logger logger = LogManager.getLogger(SmtpEmailSenderService.class);

	@Autowired
    private GlobalConfigurationsService globalConfigurationService;

	private Properties properties;
	private Authenticator authenticator;
	private String fromAddress;
	
	
	public SmtpEmailSenderService() {
	}
	
	
	private void loadMailData() {
		List<GlobalConfigurations> propList = globalConfigurationService.findAllByArea(
				GlobalConfigurationSettingsTuple.PROFILE_SMTP.AREA_NAME);

		String hostNameStr = GlobalConfigurationUtils.getValueSkippingArea(propList, GlobalConfigurationSettingsTuple.PROFILE_SMTP.MAIL_SMTP_HOST);
		String usernameStr = GlobalConfigurationUtils.getValueSkippingArea(propList, GlobalConfigurationSettingsTuple.PROFILE_SMTP.MAIL_SMTP_USER);
		String passwordStr = GlobalConfigurationUtils.getValueSkippingArea(propList, GlobalConfigurationSettingsTuple.PROFILE_SMTP.MAIL_SMTP_PASSWORD);
		String portStr = GlobalConfigurationUtils.getValueSkippingArea(propList, GlobalConfigurationSettingsTuple.PROFILE_SMTP.MAIL_SMTP_PORT);
		String authSmtpStr = GlobalConfigurationUtils.getValueSkippingArea(propList, GlobalConfigurationSettingsTuple.PROFILE_SMTP.MAIL_SMTP_AUTH);
		String startTslEnabledStr = GlobalConfigurationUtils.getValueSkippingArea(propList, GlobalConfigurationSettingsTuple.PROFILE_SMTP.MAIL_SMTP_STARTTLS_ENABLE);
		String mailSmtpSSLEnable = GlobalConfigurationUtils.getValueSkippingArea(propList, GlobalConfigurationSettingsTuple.PROFILE_SMTP.MAIL_SMTP_SSL_ENABLE);
		String mailTransportProtocol = GlobalConfigurationUtils.getValueSkippingArea(propList, GlobalConfigurationSettingsTuple.PROFILE_SMTP.MAIL_TRANSPORT_PROTOCOL);
		
		
		
		if (StringUtility.isEmptyOrWhitespace(hostNameStr) || StringUtility.isEmptyOrWhitespace(usernameStr) 
				|| StringUtility.isEmptyOrWhitespace(portStr) || StringUtility.isEmptyOrWhitespace(passwordStr) 
				|| StringUtility.isEmptyOrWhitespace(authSmtpStr) || StringUtility.isEmptyOrWhitespace(startTslEnabledStr)) {
			throw new InvalidConfigurationException("Invalid configuration of "+GlobalConfigurationSettingsTuple.PROFILE_SMTP.AREA_NAME);
		}
	
		if(!GenericValidator.isANumber(portStr)) {
			throw new InvalidConfigurationException("Invalid port configuration of "+GlobalConfigurationSettingsTuple.PROFILE_SMTP.AREA_NAME);
		}
		
		boolean startTslEnabled = Boolean.parseBoolean(startTslEnabledStr);
		boolean authSmtp = Boolean.parseBoolean(authSmtpStr);
		
		this.properties = new Properties();
		
		EmailSenderService.putInProperties(properties, "mail.smtp.host", hostNameStr);
		EmailSenderService.putInProperties(properties, "mail.smtp.user", usernameStr);
		EmailSenderService.putInProperties(properties, "mail.smtp.password", passwordStr);
		EmailSenderService.putInProperties(properties, "mail.smtp.port", portStr);
		EmailSenderService.putInProperties(properties, "mail.smtp.auth", authSmtp);
		EmailSenderService.putInProperties(properties, "mail.smtp.starttls.enable", startTslEnabled);
		EmailSenderService.putInProperties(properties, "mail.transport.protocol", mailTransportProtocol);
		EmailSenderService.putInProperties(properties, "mail.smtp.ssl.enable", mailSmtpSSLEnable);
		
		this.fromAddress = usernameStr;
		
		this.authenticator = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(usernameStr, passwordStr);
			}
		};
	}
	
	
	/**
	 * 
	 * @param mail
	 * @throws Exception if mail send fails
	 * 
	 */
	public void sendEmail(Email mail) throws MessagingException {
		if (mail == null) {
			throw new IllegalArgumentException();
		}
		this.loadMailData();

		Session session = Session.getDefaultInstance(properties, this.authenticator);
		
		sendEmailImpl(mail, this.properties, session, this.fromAddress);
	}
	
	

	private static void sendEmailImpl(Email mail, Properties properties, Session session, 
			String username) throws MessagingException {

		try {

			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			if(mail.getFromAddress()==null) {
				message.setFrom(username);
			}else {
				message.setFrom(new InternetAddress(mail.getFromAddress()));
			}
			// Set From: header field of the header.
			

			// Set To: header field of the header.
			if(mail.getToAddress()!=null && mail.getToAddress().size()>0){
				for (String destPark : mail.getToAddress()) {
					message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(destPark));
				}
			}else{
				throw new IllegalArgumentException("To address not found");
			}
			// Set To CC: header field of the header.
			if(mail.getToCC()!=null){
				for (String destParkCC : mail.getToCC()) {
					message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(destParkCC));
				}
			}
			
			// Set To BCC: header field of the header.
			if(mail.getToBcc()!=null){
				for (String destParkBCC : mail.getToBcc()) {
					message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(destParkBCC));
				}
			}
			// Set Subject: header field
			if (mail.getSubject() == null) {
				message.setSubject("");
			} else {
				message.setSubject(mail.getSubject());
			}

			Calendar c = GregorianCalendar.getInstance();

			message.setSentDate(c.getTime());

			// Set body text of message
			//message.setText(mail.getText(), "utf-8", "html");
			
			
			Multipart multipart = new MimeMultipart("related");
			
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(mail.getText(), "UTF-8", "html");

	        // add it
	        multipart.addBodyPart(textPart);
			
	        
			InputStream stream = SmtpEmailSenderService.class.getClassLoader().getResourceAsStream("logo.png");
			
			DataSource ds = new ByteArrayDataSource(stream, "image/png");
			
			MimeBodyPart iconPart = new MimeBodyPart();
			iconPart.setDataHandler(new DataHandler(ds));
			iconPart.setHeader("Content-ID", "<companyLogo>");
	        
			multipart.addBodyPart(iconPart);

			
			if(mail.getImageResourcesToShow()!=null && !mail.getImageResourcesToShow().isEmpty()) {
				Set<String> contentIds = mail.getImageResourcesToShow().keySet();
				
				for(String contentId: contentIds) {
					
					DataSource dcPark = mail.getImageResourcesToShow().get(contentId);
							
					MimeBodyPart park = new MimeBodyPart();
					park.setDataHandler(new DataHandler(dcPark));
					park.setHeader("Content-ID", "<"+contentId+">");
					multipart.addBodyPart(park);
				}
				
			}
			

			if (mail.getAttachment() != null && !mail.getAttachment().isEmpty()) {
				// Create a multipar message
				
				
				// inserisci tutti gli allegati
				for (Attachment daAllegare : mail.getAttachment()) {
					// Part two is attachment
					

					if (daAllegare != null) {
						MimeBodyPart messageBodyPart = new MimeBodyPart();
						ByteArrayDataSource dataSource = new ByteArrayDataSource(daAllegare.getInputStream(), 
								daAllegare.getMimeType());
						
						messageBodyPart.setDataHandler(new DataHandler(dataSource));
						messageBodyPart.setFileName(daAllegare.getFullName());
						multipart.addBodyPart(messageBodyPart);
					}
				}

				// Send the complete message parts
				message.setContent(multipart);
			}else {
				message.setContent(multipart);
			}
			
			for (String mailto : mail.getToAddress()) {
				InternetAddress internetAddress = new InternetAddress(mailto);
				internetAddress.validate();
			}
			
			if(mail.getToCC()!=null){
				for (String mailtoCc : mail.getToCC()) {
					InternetAddress internetAddress = new InternetAddress(mailtoCc);
					internetAddress.validate();
				}
			}
			
			if(mail.getToBcc()!=null){
				for (String mailtoBcc : mail.getToBcc()) {
					InternetAddress internetAddress = new InternetAddress(mailtoBcc);
					internetAddress.validate();
				}
			}
			
			
	         
			Transport.send(message);

		} catch (SendFailedException | AddressException e) {
			logger.warn("Some Recipients has not found " + mail.getToAddress());
			throw e;
		} catch (MessagingException e) {
			logger.error("Errore invio mail " + e);
			throw e;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
