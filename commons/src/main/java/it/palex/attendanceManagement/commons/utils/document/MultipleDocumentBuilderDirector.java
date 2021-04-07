package it.palex.attendanceManagement.commons.utils.document;

import java.util.ArrayList;

import it.palex.attendanceManagement.commons.utils.tables.Table;

public class MultipleDocumentBuilderDirector {

	private MultipleTableToDocumentBuilder builder = null;
	
	public MultipleDocumentBuilderDirector(MultipleTableToDocumentBuilder builder){
		if(builder==null){
			throw new NullPointerException();
		}
		this.builder = builder;
	}
	
	public void makeDocument(ArrayList<Table> tabella){
          builder.buildTables(tabella);	
	}
	
}
