package it.palex.attendanceManagement.data.exceptions;

/**
*
* @author Alessandro Pagliaro
*/
public class EntityAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -3196767629154217212L;

	public EntityAlreadyExistsException(){
		super();
	}
	
	public EntityAlreadyExistsException(String msg){
		super(msg);
	}
	
   
}
