package it.palex.attendanceManagement.commons.messaging.email;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.commons.messaging.Attachment;
import it.palex.attendanceManagement.commons.messaging.Email;
import it.palex.attendanceManagement.commons.messaging.strategies.MessageCompileStrategy;
import it.palex.attendanceManagement.commons.messaging.strategies.email.EmailOneTimePasswordCompileStrategy;
import it.palex.attendanceManagement.commons.messaging.strategies.email.InvitationEmailCompileStrategy;
import it.palex.attendanceManagement.commons.messaging.strategies.email.PasswordChangeCommunicationEmailCompileStrategy;
import it.palex.attendanceManagement.commons.messaging.strategies.email.ResetPswWebMobileEmailStrategy;
import it.palex.attendanceManagement.commons.messaging.strategies.email.WelcomeEmailCompileStrategy;
import it.palex.attendanceManagement.data.entities.GlobalConfigurations;
import it.palex.attendanceManagement.data.entities.core.MessageTemplate;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.entities.enumTypes.MessageTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedLangsEnumI18N;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.service.core.MessageTemplateService;
import it.palex.attendanceManagement.library.utils.FileUtility;

@Component
public class CompileAndSendEmailService {

	@Autowired
	private EmailSenderFactory emailSenderFactory;

	@Autowired
	private MessageTemplateService messageTemplateSrv;
	
    @Autowired
    private GlobalConfigurationsService globalConfigurationService;


	private EmailSenderService getEmailSenderService(){
		GlobalConfigurations senderTypeConfig = this.globalConfigurationService.findByAreaAndKey(
				GlobalConfigurationSettingsTuple.EMAIL_SENDER.AREA_NAME,
				GlobalConfigurationSettingsTuple.EMAIL_SENDER.TYPE);

		if(senderTypeConfig==null){
			throw new IllegalArgumentException("Sender type is invalid");
		}

		EmailSenderService service = this.emailSenderFactory.getEmailSender(senderTypeConfig.getSettingValue());

		return service;
	}
    
	public void sendWelcomeEmail(String destinationEmail, SupportedLangsEnumI18N lang, String nameOfUser) throws Exception {
		MessageTemplate template = this.messageTemplateSrv.findByKey(MessageTypeEnum.WELCOME_EMAIL, lang);
		
		WelcomeEmailCompileStrategy compiler = new 
				WelcomeEmailCompileStrategy(template.getMessage(), nameOfUser, template.getSubject());

		this.compileAndSendEmail(template, compiler, destinationEmail);
	}
	
	public void sendResetPswEmail(String destinationEmail, SupportedLangsEnumI18N lang, String token) throws Exception {
		MessageTemplate template = this.messageTemplateSrv.findByKey(MessageTypeEnum.RESET_PSW_EMAIL, lang);
		
	  	GlobalConfigurations config = this.globalConfigurationService.findByAreaAndKey(GlobalConfigurationSettingsTuple.FRONTEND_LINK.AREA_NAME, 
	  			GlobalConfigurationSettingsTuple.FRONTEND_LINK.WEB_APP_RESET_PSW_LINK);  
	  	
	    String resetLinkWebApp = FileUtility.concatPath(config.getSettingValue(), token);
	    
	    ResetPswWebMobileEmailStrategy compiler = new ResetPswWebMobileEmailStrategy(template.getMessage(),
	    		resetLinkWebApp, template.getSubject());

		this.compileAndSendEmail(template, compiler, destinationEmail);
	}
		
	public void sendPasswordChangeCommunicationEmail(String mailUser, SupportedLangsEnumI18N lang) throws Exception {
		GlobalConfigurations config = this.globalConfigurationService.findByAreaAndKey(
				GlobalConfigurationSettingsTuple.SUPPORT.AREA_NAME, 
	  			GlobalConfigurationSettingsTuple.SUPPORT.SECURITY_SUPPORT_MAIL); 
		
		String supportMail = (config==null || config.getSettingValue()==null) ? "":config.getSettingValue();
		
		MessageTemplate template = this.messageTemplateSrv.findByKey(MessageTypeEnum.PASSWORD_CHANGE_COMMUNICATION_EMAIL, lang);
		
		PasswordChangeCommunicationEmailCompileStrategy compiler = 
				new PasswordChangeCommunicationEmailCompileStrategy(template.getMessage(), 
						template.getSubject(), mailUser, supportMail);
		
		this.compileAndSendEmail(template, compiler, mailUser);
	}

	public void sendOneTimePasswordEmail(String destinationEmail, SupportedLangsEnumI18N lang, String oneTimePassword) throws Exception {
		MessageTemplate template = this.messageTemplateSrv.findByKey(MessageTypeEnum.ONE_TIME_PSW_EMAIL, lang);
		EmailOneTimePasswordCompileStrategy compiler = 
				new EmailOneTimePasswordCompileStrategy(template.getMessage(), oneTimePassword, template.getSubject());
		
		this.compileAndSendEmail(template, compiler, destinationEmail);
	}
	
	public void sendInvitationEmail(String destinationEmail, String username, String password,
			SupportedLangsEnumI18N lang) throws Exception {
		GlobalConfigurations config = this.globalConfigurationService.findByAreaAndKey(
				GlobalConfigurationSettingsTuple.FRONTEND_LINK.AREA_NAME, 
	  			GlobalConfigurationSettingsTuple.FRONTEND_LINK.WEB_APP_LINK); 
		
		String webAppLink = (config==null || config.getSettingValue()==null) ? "":config.getSettingValue();
		
		MessageTemplate template = this.messageTemplateSrv.findByKey(MessageTypeEnum.USER_INVITED_TO_APP, lang);
		
		InvitationEmailCompileStrategy compiler = new InvitationEmailCompileStrategy(
				template.getMessage(), template.getSubject(), username, password, webAppLink);
		
		this.compileAndSendEmail(template, compiler, destinationEmail);
	}
	
	private void compileAndSendEmail(MessageTemplate template, MessageCompileStrategy compiler,
			List<String> recipients) throws Exception {
		Email mail = new Email();
		
		mail.setSubject(template.getSubject());
		mail.setText(compiler.compileMsg());
		mail.setToAddress(recipients);

		EmailSenderService emailSenderService = this.getEmailSenderService();

		emailSenderService.sendEmail(mail);
	}

	private void compileAndSendEmail(MessageTemplate template, MessageCompileStrategy compiler, 
			String destinationEmail) throws Exception {
		this.compileAndSendEmail(template, compiler, Arrays.asList(destinationEmail));
	}

	
	private void compileAndSendEmailWithImgs(MessageTemplate template, MessageCompileStrategy compiler, 
			String destinationEmail, Map<String, DataSource> imgs) throws Exception {
		Email mail = new Email();
		
		mail.setSubject(template.getSubject());
		mail.setText(compiler.compileMsg());
		mail.setToAddress(Arrays.asList(destinationEmail));
		mail.setImageResourcesToShow(imgs);

		EmailSenderService emailSenderService = this.getEmailSenderService();

		emailSenderService.sendEmail(mail);
	}
	
	
	
	private void compileAndSendEmail(MessageTemplate template, MessageCompileStrategy compiler, 
			String destinationEmail, List<Attachment> attachments) throws Exception {
		Email mail = new Email();
		
		mail.setSubject(template.getSubject());
		mail.setText(compiler.compileMsg());
		mail.setToAddress(Arrays.asList(destinationEmail));
		mail.setAttachment(attachments);

		EmailSenderService emailSenderService = this.getEmailSenderService();

		emailSenderService.sendEmail(mail);
	}
	
	public static List<String> splitCsvEmailAddresses(String csvStr) {
		if(csvStr==null) {
			return null;
		}
		String[] addresses = csvStr.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		
		return Arrays.asList(addresses);
	}

}
