package it.palex.attendanceManagement.library.exception;

public class InternalServerErrorException extends CustomHttpException {

	private static final long serialVersionUID = 6379965730765578414L;
	
	public InternalServerErrorException() {
        super();
    }
    
    public InternalServerErrorException(StandardReturnCodesEnum error) {
		super(error);
	}
    
    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }

    protected InternalServerErrorException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}