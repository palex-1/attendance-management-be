package it.palex.attendanceManagement.library.rest.dtos;

public class LongDTO implements DTO {

	private static final long serialVersionUID = 9018018022700561283L;
	
	private Long value;

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "LongDTO [value=" + value + "]";
	}

}
