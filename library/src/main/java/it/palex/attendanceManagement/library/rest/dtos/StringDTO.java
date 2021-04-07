package it.palex.attendanceManagement.library.rest.dtos;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public class StringDTO implements DTO{

	private static final long serialVersionUID = 8200297266360553964L;

	private String value = null;

	public StringDTO() {
		
	}
	
	public StringDTO(String value) {
		super();
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "StringDTO [value=" + value + "]";
	}
	
}

