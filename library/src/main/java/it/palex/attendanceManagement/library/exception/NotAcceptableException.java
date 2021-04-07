package it.palex.attendanceManagement.library.exception;

public class NotAcceptableException extends CustomHttpException {

	private static final long serialVersionUID = 6379965730765578414L;
	
	public NotAcceptableException() {
        super();
    }
    
    public NotAcceptableException(StandardReturnCodesEnum error) {
		super(error);
	}
    
    public NotAcceptableException(String message) {
        super(message);
    }

    public NotAcceptableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAcceptableException(Throwable cause) {
        super(cause);
    }

    protected NotAcceptableException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}