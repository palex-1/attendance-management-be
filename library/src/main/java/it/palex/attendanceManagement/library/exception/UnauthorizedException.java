package it.palex.attendanceManagement.library.exception;

public class UnauthorizedException extends CustomHttpException {

	private static final long serialVersionUID = 6379965730765578414L;
	
	public UnauthorizedException() {
        super();
    }
    
    public UnauthorizedException(StandardReturnCodesEnum error) {
		super(error);
	}
    
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

    protected UnauthorizedException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}