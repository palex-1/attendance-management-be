package it.palex.attendanceManagement.commons.messaging.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component()
public class EmailSenderFactory {

    @Autowired
    private ApplicationContext appContext;

    public EmailSenderService getEmailSender(String emailSenderType) {
        if(emailSenderType==null) {
            throw new NullPointerException();
        }
        return this.buildEmailSender(EmailSenderType.valueOf(emailSenderType));
    }

    private EmailSenderService buildEmailSender(EmailSenderType emailSenderType) {
        if(emailSenderType==null) {
            throw new NullPointerException();
        }

        switch(emailSenderType){
            case SENDGRID:{
				return (EmailSenderService) appContext.getBean(SendGridEmailSenderService.COMPONENT_BEAN_NAME);
			}
            case SMTP:{
                return (EmailSenderService) appContext.getBean(SmtpEmailSenderService.COMPONENT_BEAN_NAME);
            }

            default:
                throw new IllegalArgumentException("Unknown file manager");
        }

    }


}
