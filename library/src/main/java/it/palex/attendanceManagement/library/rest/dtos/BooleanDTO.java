package it.palex.attendanceManagement.library.rest.dtos;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public class BooleanDTO implements DTO {

	private static final long serialVersionUID = -6400709096130320173L;
	
	
	private Boolean value = null;

	public BooleanDTO() {
	}
	
	public BooleanDTO(Boolean value) {
		this.value = value;
	}
	
	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "BooleanDTO [value=" + value + "]";
	}
	
}

