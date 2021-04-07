package it.palex.attendanceManagement.library.exception;

public class UnprocessableEntityException extends CustomHttpException {

	private static final long serialVersionUID = 6379965730765578414L;
	
	public UnprocessableEntityException() {
        super();
    }
    
    public UnprocessableEntityException(StandardReturnCodesEnum error) {
		super(error);
	}
    
    public UnprocessableEntityException(String message) {
        super(message);
    }

    public UnprocessableEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnprocessableEntityException(Throwable cause) {
        super(cause);
    }

    protected UnprocessableEntityException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}