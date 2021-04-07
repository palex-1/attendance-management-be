package it.palex.attendanceManagement.commons.utils.tables;

public class CellStringValue implements CellValue {

	private String cellValue;

	public CellStringValue(String cellValue) {
		this.cellValue = cellValue;
	}

	public CellStringValue(int cellValue) {
		this.cellValue = cellValue + "";
	}

	public CellStringValue(float cellValue) {
		this.cellValue = cellValue + "";
	}

	public CellStringValue(double cellValue) {
		this.cellValue = cellValue + "";
	}

	public CellStringValue(short cellValue) {
		this.cellValue = cellValue + "";
	}

	public CellStringValue(char cellValue) {
		this.cellValue = cellValue + "";
	}

	public CellStringValue(long cellValue) {
		this.cellValue = cellValue + "";
	}

	public CellStringValue(boolean cellValue) {
		if (cellValue) {
			this.cellValue = "true";
		} else {
			this.cellValue = "false";
		}

	}

	public String getStringValue() {
		return cellValue;
	}

	public void setStringValue(String cellValue) {
		if (cellValue == null) {
			throw new NullPointerException(
					"The cellValue cannot be null. Error at --> method CellStringValue.setStringValue(String cellValue)");
		}
		this.cellValue = cellValue;
	}

	@Override
	public String getValue() {
		if(cellValue==null) {
			return "";
		}
		return cellValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellValue == null) ? 0 : cellValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CellStringValue other = (CellStringValue) obj;
		if (cellValue == null) {
			if (other.cellValue != null)
				return false;
		} else if (!cellValue.equals(other.cellValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CellStringValue [cellValue=" + cellValue + "]";
	}

}
