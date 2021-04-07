package it.palex.attendanceManagement.commons.utils.document;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import it.palex.attendanceManagement.commons.exeptions.ExtensionUnknownException;
import it.palex.attendanceManagement.commons.utils.tables.Table;

public class MultipleDocumentBuilderFactory {

	public static final String XLSXBuilderID="xlsx";
	public static final String XLSBuilderID="xls";
    
	public static InputStream build(String extension, ArrayList<Table> tabelle) throws IOException {
		if(extension==null || tabelle==null) {
			throw new NullPointerException();
		}
		
		MultipleDocumentBuilderDirector director=null;
		
	    switch(extension){
		    case XLSXBuilderID: {
		    	 MultipleSheetExcelDocumentBuilder excel=new MultipleSheetExcelDocumentBuilder();
			     director=new MultipleDocumentBuilderDirector(excel);
			     director.makeDocument(tabelle);
			     
			     return excel.makeDocument();
		    }
		    case XLSBuilderID: {		    	
		    	MultipleSheetXLSDocumentBuilder xlsBuilder =new MultipleSheetXLSDocumentBuilder();
			    director= new MultipleDocumentBuilderDirector(xlsBuilder);
			    director.makeDocument(tabelle);
			     
			     return xlsBuilder.makeDocument();
		    }
		    default: {
		    	throw new ExtensionUnknownException("This is not a supported extension for MultipleDocumentBuilderFactory.build"+extension);
		    }
	    	
	    }
	    
	}
	
	
}
