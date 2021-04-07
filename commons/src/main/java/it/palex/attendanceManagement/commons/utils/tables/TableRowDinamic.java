package it.palex.attendanceManagement.commons.utils.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class TableRowDinamic implements TableRow {

	private ArrayList<CellValue> tableRow;
	private int columnNumber = -1;

	public TableRowDinamic() {
		tableRow = new ArrayList<CellValue>();
	}

	public void setColumnValue(int columnIndex, CellValue value) {
		if(tableRow.size()<=columnIndex) {
			int toAdd = Math.abs(tableRow.size() - columnIndex) + 1;
			while(toAdd>0) {
				tableRow.add(new CellStringValue(""));
				toAdd--;
			}
		}
		
		tableRow.set(columnIndex, value);
	}

	
	@Override
	public Iterator<CellValue> iterator() {
		return Collections.unmodifiableList(tableRow).iterator();
	}

	public int getColumnNumber() {
		return this.columnNumber;
	}
	
	@Override
	public CellValue getValueAt(int index) {
		return this.tableRow.get(index);
	}

	@Override
	public String toString() {
		return "TableRowDinamic [tableRow=" + tableRow + ", columnNumber=" + columnNumber + "]";
	}

}
