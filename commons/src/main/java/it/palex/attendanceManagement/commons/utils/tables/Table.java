package it.palex.attendanceManagement.commons.utils.tables;

import java.util.Iterator;

public interface Table extends Iterable<TableRow>, Comparable<Table>{
	
	public int getRowNumber();
	public TableRow getRowAt(int index);
	public void setTableRow(TableRow riga,int indiceRiga);
	public Iterator<TableRow> iterator();
	public String getTableName();
	
	@Override
	public int compareTo(Table tab);
}
