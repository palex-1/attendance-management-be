package it.palex.attendanceManagement.commons.exeptions;

public class ExtensionUnknownException extends RuntimeException {

	private static final long serialVersionUID = 6846127705588854678L;

	public ExtensionUnknownException(String mess){
		super(mess);
	}
    
	public ExtensionUnknownException(){
		super();
	}
	
}
