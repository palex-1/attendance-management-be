package it.palex.attendanceManagement.commons.messaging.email;

/**
 * @author Alessandro Pagliaro
 *
 */
public enum EmailSenderType {
    SMTP,
    SENDGRID;

    public static boolean isValid(String type) {
        if(type==null) {
            return false;
        }
        try {
            EmailSenderType.valueOf(type);
            return true;
        }catch(IllegalArgumentException e) {
            return false;
        }
    }

}

