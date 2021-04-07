package it.palex.attendanceManagement.commons.utils.tables;


public class ColumnLabel implements CellValue {

	private String label;
	private short foregroundColorIndex;
	private boolean bold;
	
	
	public ColumnLabel(String label, short foregroundColorIndex, boolean bold) {
		this(label);
		this.foregroundColorIndex = foregroundColorIndex;
		this.bold = bold;
	}

	public ColumnLabel(String label,  boolean bold) {
		this(label);
		this.label = label;
		this.bold = bold;
	}
	
	public ColumnLabel(String label, short foregroundColorIndex) {
		this(label);
		this.label = label;
		this.foregroundColorIndex = foregroundColorIndex;
	}
	
	public ColumnLabel(String label) {
		super();
		this.label = label;
		this.foregroundColorIndex = CellColor.WHITE.color;
		this.bold = false;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		if (label == null) {
			throw new NullPointerException(
					"The label cannot be null. Error at --> method LabelColonna.setLabel(String label)");
		}
		this.label = label;
	}

	public short getForegroundColorIndex() {
		return foregroundColorIndex;
	}

	/**
	 * Use IndexedColors.GREY_25_PERCENT.getIndex()
	 * @param foregroundColorIndex
	 */
	public void setForegroundColorIndex(short foregroundColorIndex) {
		this.foregroundColorIndex = foregroundColorIndex;
	}
	
	
	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	@Override
	public String getValue() {
		return this.label;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime * this.label.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ColumnLabel))
			return false;
		if (o == this)
			return true;
		ColumnLabel p = (ColumnLabel) o;
		return this.label.equals(p.getLabel());
	}

	@Override
	public String toString() {
		return "ColumnLabel [label=" + label + ", foregroundColorIndex=" + foregroundColorIndex + "]";
	}
}