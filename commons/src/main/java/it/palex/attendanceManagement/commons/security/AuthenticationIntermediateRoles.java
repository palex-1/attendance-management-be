package it.palex.attendanceManagement.commons.security;

/**
 * Thsi class is intended to be used for mapping fake authorities of user useful to block user with an
 * invalid authentication status
 * @author Alessandro Pagliaro
 *
 */
public class AuthenticationIntermediateRoles {
	
	public static final String TWO_FA_IN_PROGRESS = "TWO_FA_IN_PROGRESS";
	public static final String SOCIAL_AUTHENTICATION_COMPLETED = "SOCIAL_AUTHENTICATION_COMPLETED";
	public static final String LOGGING_WITH_SOCIAL = "LOGGING_WITH_SOCIAL";
	public static final String USER_MUST_CHANGE_PASSWORD = "USER_MUST_CHANGE_PASSWORD";
	
}
