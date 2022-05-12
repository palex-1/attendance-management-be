package it.palex.attendanceManagement.data.entities.enumTypes;


public class GlobalConfigurationSettingsTuple {
	
	public static class PROFILE_IMAGE_AREA {
		public static String AREA_NAME = "PROFILE_IMAGE";
		
		public static final String MAX_IMAGE_WEIGHT = "MAX_IMAGE_WEIGHT";
		public static final String IMAGE_WIDTH = "IMAGE_WIDTH";
		public static final String IMAGE_HEIGTH = "IMAGE_HEIGTH";
	}

	public static class PROFILE_TWILIO {
		public static final String AREA_NAME = "PROFILE_TWILIO";
		
		public static final String ACCOUNT_SID = "ACCOUNT_SID";
		public static final String AUTH_TOKEN = "AUTH_TOKEN";
		public static final String TWILIO_NUMBER = "TWILIO_NUMBER";
	}
	
	public static class FOOD_VOURCHER {
		public static final String AREA_NAME = "FOOD_VOURCHER";
		
		public static final String ENABLED = "ENABLED";
		public static final String MIN_WORKED_HOURS_TO_REQUEST_IT = "MIN_WORKED_HOURS_TO_REQUEST_IT";
	}
	
	
	public static class PROFILE_SENDGRID {
		public static final String AREA_NAME = "PROFILE_SENDGRID";
		
		public static final String API_KEY = "API_KEY";
		public static final String MAIL_FROM = "MAIL_FROM";
	}

	public static class EMAIL_SENDER {
		public static final String AREA_NAME = "EMAIL_SENDER";

		public static final String TYPE = "TYPE";
	}

	public static class PROFILE_SMTP {
		public static final String AREA_NAME = "PROFILE_SMTP";
		
		public static final String MAIL_SMTP_HOST = "MAIL_SMTP_HOST";
		public static final String MAIL_SMTP_USER = "MAIL_SMTP_USER";
		public static final String MAIL_SMTP_PASSWORD = "MAIL_SMTP_PASSWORD";
		public static final String MAIL_SMTP_PORT = "MAIL_SMTP_PORT";
		public static final String MAIL_SMTP_AUTH = "MAIL_SMTP_AUTH";
		public static final String MAIL_SMTP_STARTTLS_ENABLE = "MAIL_SMTP_STARTTLS_ENABLE";
		public static final String MAIL_SMTP_SSL_ENABLE = "MAIL_SMTP_SSL_ENABLE";
		public static final String MAIL_TRANSPORT_PROTOCOL = "MAIL_TRANSPORT_PROTOCOL";
	}
	
	public static class SUPPORT {
		public static final String AREA_NAME = "SUPPORT";
		public static final String SECURITY_SUPPORT_MAIL = "SECURITY_SUPPORT_MAIL";
	}
	
	public static class FRONTEND_LINK {
		public static final String AREA_NAME = "FRONTEND_LINK";
		
		public static final String CONFIRMATION_EMAIL_LINK = "CONFIRMATION_EMAIL_LINK";
		public static final String WEB_APP_RESET_PSW_LINK = "WEB_APP_RESET_PSW_LINK";
		public static final String WEB_APP_LINK = "WEB_APP_LINK";
	}
	
	public static class LOGIN_SETTINGS {
		public static final String AREA_NAME = "LOGIN_SETTINGS";
		public static final String USE_ONLY_ONE_SESSION = "USE_ONLY_ONE_SESSION";
	}
	
	public static class FILE_MANAGER_CONFIG {
		public static final String AREA_NAME = "FILE_MANAGER_CONFIG";
		public static final String DEFAULT_FILE_MANAGER = "DEFAULT_FILE_MANAGER";
		public static final String DEFAULT_FILE_MANAGER_CRYPTED = "DEFAULT_FILE_MANAGER_CRYPTED";
		public static final String DEFAULT_IMAGE_CONVERSION_EXTENSION = "DEFAULT_IMAGE_CONVERSION_EXTENSION";
		public static final String DEFAULT_COMPRESSION_LEVEL = "DEFAULT_COMPRESSION_LEVEL";
	}

	public static class REGISTRATION_SETTING {
		public static final String AREA_NAME = "REGISTRATION_SETTING_CONFIG";
		public static final String UNIQUE_CONTACTS = "UNIQUE_CONTACTS";
	}
	
	public static class CODICI_INCARICO_SPECIALI {
		public static final String AREA_NAME = "CODICI_INCARICO_SPECIALI";
	}
	
	public static class CODICI_INCARICO_SPECIALI_COLOR {
		public static final String AREA_NAME = "CODICI_INCARICO_SPECIALI_COLOR";
	}
	
	public static class BADGE {
		public static final String AREA_NAME = "BADGE";
		public static final String COMPANY_NAME = "COMPANY_NAME";
		public static final String COMPANY_LOGO_DOCUMENT_ID = "COMPANY_LOGO_DOCUMENT_ID";
	}
	
	public static class REPORT {
		public static final String AREA_NAME = "REPORT";
		public static final String REPORT_CREATOR = "REPORT_CREATOR";
	}
	
	public static class PUBLIC_HOLIDAYS {
		public static final String AREA_NAME = "PUBLIC_HOLIDAYS";
	}	
	
	public static class CODICI_INCARICO_PER_CALCOLO_MENSILE {
		public static final String AREA_NAME = "CODICI_INCARICO_PER_CALCOLO_MENSILE";
		public static final String FERIE = "FERIE";
		public static final String ROL = "ROL";
	}


	public static class TASK_EXPENSE_TYPES {
		public static String AREA_NAME = "EXPENSES_TYPES";
	}

}
