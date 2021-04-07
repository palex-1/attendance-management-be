package it.palex.attendanceManagement.library.exception;

public class InvalidImageException extends CustomHttpException {

	private static final long serialVersionUID = 6379965730765578414L;
	
	public InvalidImageException() {
        super();
    }
    
    public InvalidImageException(StandardReturnCodesEnum error) {
		super(error);
	}
    
    public InvalidImageException(String message) {
        super(message);
    }

    public InvalidImageException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidImageException(Throwable cause) {
        super(cause);
    }

    protected InvalidImageException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}