package it.palex.attendanceManagement.batch.config;

public class JobNamesAndGroup {
	
	public static class DELETE_EXPIRED_TOKEN {
		public static final String JOB_NAME = "DELETE_EXPIRED_TOKEN";
		public static final String GROUP = "DELETE_EXPIRED_TOKEN_GROUP";
	}
	
	public static class DELETE_SUCCESSFULLY_LOGIN_LOGS_TASKLET {
		public static final String JOB_NAME = "DELETE_SUCCESSFULLY_LOGIN_LOGS_TASKLET_JOB";
		public static final String GROUP = "DELETE_SUCCESSFULLY_LOGIN_LOGS_TASKLET_JOB_GROUP";
	}

	public static class DELETE_TEMPORARY_FILES_TASKLET {
		public static final String JOB_NAME = "DELETE_TEMPORARY_FILES_TASKLET_JOB";
		public static final String GROUP = "DELETE_TEMPORARY_FILES_TASKLET_JOB_GROUP";
	}
	
	public static class DELETE_FAILED_LOGIN_ATTEMPT_TASKLET {
		public static final String JOB_NAME = "DELETE_FAILED_LOGIN_ATTEMPT_TASKLET_JOB";
		public static final String GROUP = "DELETE_FAILED_LOGIN_ATTEMPT_TASKLET_JOB_GROUP";
	}

	public static class DELETE_CHANGE_PASSWORD_HISTORY_TASKLET {
		public static final String JOB_NAME = "DELETE_CHANGE_PASSWORD_HISTORY_TASKLET_JOB";
		public static final String GROUP = "DELETE_CHANGE_PASSWORD_HISTORY_TASKLET_JOB_GROUP";
	}
	
	public static class DELETE_EXPIRED_CHANGE_PASSWORD_REQUEST {
		public static final String JOB_NAME = "DELETE_EXPIRED_CHANGE_PASSWORD_REQUEST_JOB";
		public static final String GROUP = "DELETE_EXPIRED_CHANGE_PASSWORD_REQUEST_JOB_GROUP";
	}
	
	public static class TASK_REPORT_BATCH {
		public static final String JOB_NAME = "TASK_REPORT_BATCH_JOB";
		public static final String GROUP = "TASK_REPORT_BATCH_JOB_GROUP";
	}
	
	public static class TASK_COMPLETIONS_LOCKS_BATCH {
		public static final String JOB_NAME = "TASK_COMPLETIONS_LOCKS_BATCH_JOB";
		public static final String GROUP = "TASK_COMPLETIONS_LOCKS_BATCH_JOB_GROUP";
	}
	
		
	public static class DELETE_EXPIRED_DOWNLOAD_TICKET {
		public static final String JOB_NAME = "DELETE_EXPIRED_DOWNLOAD_TICKET_JOB";
		public static final String GROUP = "DELETE_EXPIRED_DOWNLOAD_TICKET_JOB_GROUP";
	}
	
	
}
