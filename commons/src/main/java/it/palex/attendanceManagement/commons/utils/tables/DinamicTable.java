package it.palex.attendanceManagement.commons.utils.tables;

import java.util.ArrayList;
import java.util.Iterator;

public class DinamicTable implements Table {

	private ArrayList<TableRow> tableRows;
	private String tableName = "";

	public DinamicTable(String tableName) {
		if (tableName == null) {
			throw new NullPointerException();
		}
		this.tableName = tableName;
		tableRows = new ArrayList<>();
	}

	@Override
	public String getTableName() {
		return this.tableName;
	}

	@Override
	public void setTableRow(TableRow row, int rowIndex) {
		if (row == null) {
			throw new NullPointerException();
		}

		if (rowIndex < 0 || rowIndex > this.tableRows.size()) {
			throw new IndexOutOfBoundsException("index = " + rowIndex + "  size=" + (this.tableRows.size()));
		}
		this.tableRows.add(rowIndex, row);
	}

	public TableRow getRowAt(int index) {
		if (index < 0 || index >= this.tableRows.size()) {
			throw new IndexOutOfBoundsException("index= " + index + " size=" + this.tableRows.size());
		}
		return this.tableRows.get(index);
	}

	public void addTableRow(TableRow row) {
		if (row == null) {
			throw new NullPointerException("The row is null");
		}
		this.tableRows.add(row);
	}
	
	public void addBlankRow() {
		this.tableRows.add(new TableRowDinamic());
	}

	public void addBlankRows(int num) {
		if(num<=0) {
			throw new IllegalArgumentException("num must be greater than zero");
		}
		for(int i=0; i<num; i++) {
			this.tableRows.add(new TableRowDinamic());
		}
	}
	
	@Override
	public int getRowNumber() {
		return this.tableRows.size();
	}

	@Override
	public Iterator<TableRow> iterator() {
		return this.tableRows.iterator();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.tableRows.size());
		sb.append("DinamicTable [tableRows=");
		for (TableRow riga : this.tableRows) {
			sb.append(riga.toString());
			sb.append("\n");
		}

		sb.append(" numero di righe " + this.tableRows.size());

		return sb.toString();
	}

	@Override
	public int compareTo(Table tab) {
		if (tab == null) {
			throw new NullPointerException();
		}
		return this.tableName.compareToIgnoreCase(tab.getTableName());
	}

}