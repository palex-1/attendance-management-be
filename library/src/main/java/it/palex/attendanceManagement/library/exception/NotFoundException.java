package it.palex.attendanceManagement.library.exception;

public class NotFoundException extends CustomHttpException {

	private static final long serialVersionUID = 6379965730765578414L;
	
	public NotFoundException() {
        super();
    }
    
    public NotFoundException(StandardReturnCodesEnum error) {
		super(error);
	}
    
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    protected NotFoundException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}