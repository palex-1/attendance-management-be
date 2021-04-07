package it.palex.attendanceManagement.commons.exeptions;

public class TokenCannotBeRefreshed extends RuntimeException {

	private static final long serialVersionUID = 687632876478634678L;

	public TokenCannotBeRefreshed(String mess){
		super(mess);
	}
    
	public TokenCannotBeRefreshed(){
		super();
	}
	
}