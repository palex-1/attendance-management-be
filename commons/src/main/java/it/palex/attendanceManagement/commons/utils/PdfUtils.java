package it.palex.attendanceManagement.commons.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfUtils {

	public static boolean checExistanceOfStringOccurrencies(String str, InputStream stream, boolean ignoreCase) throws IOException {
		if(str==null || stream==null) {
			throw new NullPointerException();
		}
		PDDocument document = PDDocument.load(stream);
		PDFTextStripper pdfStripper = new PDFTextStripper();
		
		String text = pdfStripper.getText(document);

		int index = -1;
		
		if(ignoreCase) {
			index = text.indexOf(str.toUpperCase());
			if(index<0) {
				index = text.indexOf(str.toLowerCase());
			}
		}else {
			index = text.indexOf(str);
		}
		
		document.close();
		
		return index>=0;
	}

}

