package it.palex.attendanceManagement.data.exceptions;

/**
 * @author Alessandro Pagliaro
 *
 * Questa eccezione deve essere sollevata in caso l'entity non può essere inserita nel database.
 * Per restringere il campo di'uso dell'eccezione e di un facile debugging questa eccezione deve essere sollevata 
 * quando si sta cercando di inserire una entity che implementa la classe DatabaseCheckableInsert 
 */
public class EntityCreationException extends RuntimeException{
	
	private static final long serialVersionUID = -2807179279453212444L;

	public EntityCreationException(){
		super();
	}
	
	public EntityCreationException(String msg){
		super(msg);
	}
}
