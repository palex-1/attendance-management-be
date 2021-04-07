package it.palex.attendanceManagement.commons.utils.tables;

import java.util.Iterator;

public interface TableRow extends Iterable<CellValue>{
	
	public void setColumnValue(int columnIndex, CellValue value);
	public Iterator<CellValue> iterator();
	public int getColumnNumber();
	public CellValue getValueAt(int indiceColonna);
	
}
