package it.palex.attendanceManagement.commons.messaging.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.commons.messaging.strategies.sms.SMSOneTimePasswordCompileStrategy;
import it.palex.attendanceManagement.commons.messaging.strategies.sms.WelcomeSMSCompileStrategy;
import it.palex.attendanceManagement.data.entities.core.MessageTemplate;
import it.palex.attendanceManagement.data.entities.enumTypes.MessageTypeEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedLangsEnumI18N;
import it.palex.attendanceManagement.data.service.core.MessageTemplateService;

@Component
public class CompileAndSendSMSService {

	@Autowired
	private SmsSenderService smsSenderSrv;
	
	@Autowired
	private MessageTemplateService messageTemplateSrv;
	
	
	public void sendWelcomeSMS(String destinationNumber, SupportedLangsEnumI18N lang, String nameOfUser) throws Exception {
		MessageTemplate template = this.messageTemplateSrv.findByKey(MessageTypeEnum.WELCOME_MSG, lang);
		WelcomeSMSCompileStrategy compile = new WelcomeSMSCompileStrategy(template.getMessage(), nameOfUser);
		
		this.smsSenderSrv.sendSMS(destinationNumber, compile.compileMsg());
	}
	
	public void sendOneTimePasswordSMS(String destinationNumber, SupportedLangsEnumI18N lang, String oneTimePsw) throws Exception {
		MessageTemplate template = this.messageTemplateSrv.findByKey(MessageTypeEnum.ONE_TIME_PSW_SMS, lang);
		SMSOneTimePasswordCompileStrategy compiler = new SMSOneTimePasswordCompileStrategy(template.getMessage(), oneTimePsw);
		
		this.smsSenderSrv.sendSMS(destinationNumber, compiler.compileMsg());
	}
}
