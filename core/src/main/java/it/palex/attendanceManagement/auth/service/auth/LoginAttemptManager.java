package it.palex.attendanceManagement.auth.service.auth;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.data.entities.auth.FailedLoginAttempt;
import it.palex.attendanceManagement.data.entities.auth.FailedLoginAttemptPK;
import it.palex.attendanceManagement.data.entities.auth.SuccessfullyLoginLogs;
import it.palex.attendanceManagement.data.entities.auth.SuccessfullyLoginLogsPK;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.exceptions.EntityAlreadyExistsException;
import it.palex.attendanceManagement.data.exceptions.EntityCreationException;
import it.palex.attendanceManagement.data.service.user.FailedLoginAttemptService;
import it.palex.attendanceManagement.data.service.user.SuccessfullyLoginLogsService;
import it.palex.attendanceManagement.data.service.user.UsersAuthDetailsService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.ConfigurationsService;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.InformationHttpRequestExtractor;

/**
 * @author Alessandro Pagliaro
 *
 */
@Component
public class LoginAttemptManager implements GenericService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LoginAttemptManager.class);

	@Autowired
	private ConfigurationsService configSrv;

	@Autowired
	private FailedLoginAttemptService failedLoginAttemptService;

	@Autowired
	private SuccessfullyLoginLogsService successfullyLoginLogsService;

	@Autowired
	private UsersAuthDetailsService userAuthDetailsService;

	@Value("${security.bruteforce.login.max_failed_login_attempt_single_account}")
	private int maxFailedAttemptForSingleAccount;

	@Value("${security.bruteforce.login.consider_only_failed_login_attempt_single_account_past_seconds}")
	private int secondsFailedAttemptForSingleAccount;

	@Value("${security.bruteforce.login.max_failed_login_attempt_single_ip}")
	private int maxFailedAttemptForSingleIp;

	@Value("${security.bruteforce.login.consider_only_failed_login_attempt_single_ip_past_seconds}")
	private int secondsFailedAttemptForSingleIp;

	
	
	public GenericResponse<StringDTO> canMakeAuthentication(String username, HttpServletRequest httpRequest) {
		if (httpRequest == null) {
			throw new NullPointerException();
		}
		Calendar actualDateInUTC = DateUtility.getCurrentCalendarInUTC();

		if (this.checkAuthenticationAttemptMadeForUsernameExeeded(httpRequest, username, actualDateInUTC)) {
			return this.buildTooManyRequest("Too many authentication request. Please try later.");
		}
		if (this.checkAuthenticationAttemptMadeForIpExeeded(httpRequest, actualDateInUTC)) {
			return this.buildTooManyRequest("Too many authentication request from your ip. Please try later.");
		}
		return this.buildOkResponse(new StringDTO("Successfully Authentication"));
	}

	/**
	 * @param httpRequest
	 * @param actualDateInUTC
	 * @return
	 */
	private boolean checkAuthenticationAttemptMadeForIpExeeded(HttpServletRequest httpRequest,
			Calendar actualDateInUTC) {
		String ip = this.getIpFromRequest(httpRequest);
		if (ip == null) {
			LOGGER.error("ip null at LoginAttemptManager.checkAuthenticationAttemptMadeForIpExeeded");
			return false;
		}
		long count = this.countAllAuthenticationMadeByIpBeforeDate(ip, actualDateInUTC);

		return count > this.maxFailedAttemptForSingleIp;
	}

	/**
	 * @param httpRequest
	 * @param username
	 * @param actualDateInUTC
	 * @return
	 */
	private boolean checkAuthenticationAttemptMadeForUsernameExeeded(HttpServletRequest httpRequest, String username,
			Calendar actualDateInUTC) {
		String ip = this.getIpFromRequest(httpRequest);
		String userAgent = this.getUserAgentFromRequest(httpRequest);
		if (ip == null) {
			LOGGER.error("ip null at LoginAttemptManager.checkAuthenticationAttemptMadeForUsernameExeeded");
			return false;
		}

		if (this.existAnotherRequestMadeInTheSameSecond(userAgent, ip, username, actualDateInUTC)) {
			return true;
		}

		long count = countAllAuthenticationAttemptMadeForUsernameBeforeDate(ip, username, actualDateInUTC);
		return count > this.maxFailedAttemptForSingleAccount;
	}

	/**
	 * 
	 * @param userAgent
	 * @param ip
	 * @param username
	 * @param actualDateInUTC
	 * @return true if exist another request made by same user in the same second
	 */
	private boolean existAnotherRequestMadeInTheSameSecond(String userAgent, String ip, String username,
			Calendar actualDateInUTC) {
		if (ip == null || userAgent == null) {
			LOGGER.error("ip null at LoginAttemptManager.existAnotherRequestMadeInTheSameSecond ip" + ip + " userAgent:"
					+ userAgent);
			return false;
		}
		if (username == null || actualDateInUTC == null) {
			throw new NullPointerException();
		}

		Date currentDate = DateUtility.getCurrentDateInUTC();

		return this.failedLoginAttemptService.existsAnAttemptMadeByIpForUsernameInDate(ip, currentDate, username);
	}

	/**
	 * 
	 * @param ip
	 * @param username
	 * @param actualDateInUTC
	 * @return the count of authentication attempt made for username from ip,
	 *         between date range actualDateInUTC and actualDateInUTC - time
	 *         specified in properies file
	 */
	private long countAllAuthenticationAttemptMadeForUsernameBeforeDate(String ip, String username,
			Calendar actualDateInUTC) {
		if (ip == null || username == null || actualDateInUTC == null) {
			throw new NullPointerException();
		}
		Calendar from = ((Calendar) actualDateInUTC.clone());
		// subtract the time to actual date to consider only a range actualDateInUTC
		from.add(Calendar.SECOND, this.secondsFailedAttemptForSingleAccount * -1);

		long count = this.failedLoginAttemptService.getCountOfAllFailedLoginAttemptOfIpAndUsernameInRange(ip, username,
				from, actualDateInUTC);

		return count;
	}

	/**
	 * 
	 * @param ip
	 * @param actualDateInUTC
	 * @return the count of authentication attempt made by ip, between date range
	 *         actualDateInUTC and actualDateInUTC - time specified in properies
	 *         file
	 */
	private long countAllAuthenticationMadeByIpBeforeDate(String ip, Calendar actualDateInUTC) {
		if (ip == null || actualDateInUTC == null) {
			throw new NullPointerException();
		}
		Calendar beforeInUTC = ((Calendar) actualDateInUTC.clone());
		// subtract the time to actual date to consider only a range actualDateInUTC
		beforeInUTC.add(Calendar.SECOND, this.secondsFailedAttemptForSingleIp * -1);

		long count = this.failedLoginAttemptService.getCountOfAllFailedLoginAttemptOfIpInTimeRange(ip, beforeInUTC,
				actualDateInUTC);

		return count;
	}

	/**
	 * 
	 * @param username
	 * @param httpRequest
	 * @throws NullPointerException if <strong>username</strong> or
	 *                              <strong>httpRequest</strong> is null<br>
	 *                              The method will save the failed login attempt
	 */
	public void saveFailedLoginAttempt(String username, HttpServletRequest httpRequest) {
		if (username == null || httpRequest == null) {
			throw new NullPointerException();
		}
		try {
			this.saveFailedLoginAttemptPrivate(username, httpRequest);
		} catch (EntityCreationException | EntityAlreadyExistsException e) {
			LOGGER.error(
					"Error saving Failed Login attempt. This can happen when multiple login are made in the same second",
					e);
		}
	}

	private void saveFailedLoginAttemptPrivate(String username, HttpServletRequest httpRequest)
			throws EntityCreationException, EntityAlreadyExistsException {
		String ip = this.getIpFromRequest(httpRequest);
		String userAgent = this.getUserAgentFromRequest(httpRequest);
		// if ip or user-agent are null something is wrong but the program will not
		// stop, so we log the attempt
		if (ip == null || userAgent == null) {
			LOGGER.error("saveFailedLoginAttemptPrivate Not valid request ip:" + ip + " userAgent:" + userAgent + "\r\n"
					+ getHttpRequestInStringFormat(httpRequest));
			return;
		}

		Date currentDate = DateUtility.getCurrentDateInUTC();

		FailedLoginAttempt park = this.failedLoginAttemptService.findByKey(ip, userAgent, username, currentDate);
		if (park == null) {
			FailedLoginAttempt attempt = buildFailedLoginAttempt(ip, userAgent, username, currentDate);
			this.failedLoginAttemptService.create(attempt);
		}
	}

	/**
	 * 
	 * @param ip
	 * @param userAgent
	 * @param username
	 * @param date
	 * @return a FailedLoginAttempt object with the properties passed in argument
	 *         setted
	 */
	private FailedLoginAttempt buildFailedLoginAttempt(String ip, String userAgent, String username, Date date) {
		if (ip == null || userAgent == null || username == null || date == null) {
			throw new NullPointerException();
		}
		FailedLoginAttempt attempt = new FailedLoginAttempt();
		FailedLoginAttemptPK failedLoginAttemptPK = new FailedLoginAttemptPK();
		failedLoginAttemptPK.setUsername(username);
		failedLoginAttemptPK.setIp(ip);
		failedLoginAttemptPK.setUserAgent(userAgent);
		failedLoginAttemptPK.setLoginDate(date);
		attempt.setFailedLoginAttemptPK(failedLoginAttemptPK);

		return attempt;
	}

	/**
	 * 
	 * @param username
	 * @param httpRequest
	 * @throws NullPointerException if <strong>username</strong> or
	 *                              <strong>httpRequest</strong> is null<br>
	 *                              The method will save the successfull login
	 *                              attempt
	 */
	public void saveSuccessfullLogin(String username, HttpServletRequest httpRequest) {
		if (username == null || httpRequest == null) {
			throw new NullPointerException();
		}
		try {

			this.saveSuccessfullLoginPrivate(username, httpRequest);

		} catch (EntityCreationException | EntityAlreadyExistsException e) {
			LOGGER.error(
					"Error saving Successfull Login attempt. This can happen when multiple login attempt are made in the same second",
					e);
		}
	}

	private void saveSuccessfullLoginPrivate(String username, HttpServletRequest httpRequest)
			throws EntityCreationException, EntityAlreadyExistsException {
		String ip = this.getIpFromRequest(httpRequest);
		String userAgent = this.getUserAgentFromRequest(httpRequest);
		// if ip or user-agent are null something is wrong but the program will not
		// stop, so we log the attempt
		if (ip == null || userAgent == null) {
			LOGGER.error("saveSuccessfullLoginPrivate Not valid request  ip:" + ip + " userAgent:" + userAgent + "\r\n"
					+ getHttpRequestInStringFormat(httpRequest));
			return;
		}

		Date currentDate = DateUtility.getCurrentDateInUTC();

		SuccessfullyLoginLogs attempt = new SuccessfullyLoginLogs();
		SuccessfullyLoginLogsPK attemptPK = new SuccessfullyLoginLogsPK();
		attemptPK.setIp(ip);
		attemptPK.setUserAgent(userAgent);
		attemptPK.setLoginDate(currentDate);
		attempt.setSuccessfullyLoginLogsPK(attemptPK);

		UsersAuthDetails user = userAuthDetailsService.findByUsername(username);

		if (user == null) {
			LOGGER.error("Cannot be SuccessfullyLoginLogs if the username does not exist");
			return;
		}
		attempt.getSuccessfullyLoginLogsPK().setFkIdUsersAuthDetails(user.getId());

		if (!this.successfullyLoginLogsService.exists(attempt)) {
			this.successfullyLoginLogsService.create(attempt);
		}

	}

	/**
	 * 
	 * @param httpRequest
	 * @return ip from request using ClientIPAddressRetriever, null will be returned
	 *         if ip is not valid
	 */
	private String getIpFromRequest(HttpServletRequest httpRequest) {
		return InformationHttpRequestExtractor.getIpFromRequest(httpRequest, configSrv.getProxyType());
	}

	/**
	 * 
	 * @param httpRequest
	 * @return the user agent if exists in httpRequest else
	 *         <strong>NO_USER_AGENT_USER_AGENT</strong> string<br>
	 *         The method does not return null
	 * 
	 */
	private String getUserAgentFromRequest(HttpServletRequest httpRequest) {
		return InformationHttpRequestExtractor.getUserAgentFromRequest(httpRequest);
	}

	/**
	 * 
	 * @param httpRequest
	 * @return a string representation of http request, example: user-agent:
	 *         gecko_...
	 */
	private String getHttpRequestInStringFormat(HttpServletRequest httpRequest) {
		return InformationHttpRequestExtractor.getHttpRequestInStringFormat(httpRequest);
	}

}

