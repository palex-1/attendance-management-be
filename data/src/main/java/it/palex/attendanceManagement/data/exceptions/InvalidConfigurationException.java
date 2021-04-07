package it.palex.attendanceManagement.data.exceptions;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public class InvalidConfigurationException  extends RuntimeException {

	
	private static final long serialVersionUID = 1717184747384975856L;

	public InvalidConfigurationException() {
		super();
	}

	public InvalidConfigurationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidConfigurationException(String message) {
		super(message);
	}
	
	public InvalidConfigurationException(String areaName, String key) {
		super("InvalidConfiguration --> areaName: "+areaName+" --  key: "+key);		
	}

	public InvalidConfigurationException(Throwable cause) {
		super(cause);
	}
	
}
