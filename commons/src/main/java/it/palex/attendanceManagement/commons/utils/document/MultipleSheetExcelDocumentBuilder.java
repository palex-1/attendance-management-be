package it.palex.attendanceManagement.commons.utils.document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.palex.attendanceManagement.commons.utils.tables.CellValue;
import it.palex.attendanceManagement.commons.utils.tables.ColumnLabel;
import it.palex.attendanceManagement.commons.utils.tables.Table;
import it.palex.attendanceManagement.commons.utils.tables.TableRow;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public class MultipleSheetExcelDocumentBuilder implements MultipleTableToDocumentBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultipleSheetExcelDocumentBuilder.class);
	
	private ArrayList<Table> tables=null;
	
	
	public MultipleSheetExcelDocumentBuilder(){
	}

	
	@Override
	public void buildTables(ArrayList<Table> tables) {
		if(tables==null){
			throw new NullPointerException();
		}
		this.tables = tables;
	}
		
	private void buildCellValue(XSSFWorkbook workbook, Row row, CellValue value, int colNumber) {
		Cell cell = row.createCell(colNumber);
		if(value instanceof ColumnLabel){
			decorateLabelCell(workbook, (ColumnLabel) value, cell);
		}
		if(value==null) {
			cell.setCellValue("");
		}else {
			cell.setCellValue(value.getValue());	
		}
	}
	
	
	private void decorateLabelCell(XSSFWorkbook workbook, ColumnLabel label, Cell cell) {
		CellStyle labelStyle = workbook.createCellStyle();
		labelStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		labelStyle.setFillForegroundColor(this.colorToApachePoiColor(label.getForegroundColorIndex()));
		
		labelStyle.setBorderBottom(BorderStyle.THIN);
		labelStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        labelStyle.setBorderLeft(BorderStyle.THIN);
        labelStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        labelStyle.setBorderRight(BorderStyle.THIN);
        labelStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        labelStyle.setBorderTop(BorderStyle.THIN);
        labelStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
		
		XSSFFont font = workbook.createFont();
		font.setBold(label.isBold());
		labelStyle.setFont(font);
		
		cell.setCellStyle(labelStyle);
	}
	
	
	private Row buildRow(int rowIndex, XSSFSheet sheet) {
		return sheet.createRow(rowIndex);
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private InputStream buildInputStreamFile(XSSFWorkbook workbook) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        workbook.write(byteArrayOutputStream);
        
        byte[] data = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        
        return new ByteArrayInputStream(data); 
	}
	
	
	
	/**
	 * The file will be overwrite if already exist.
	 * @throws IOException 
	 */
	public InputStream makeDocument() throws IOException{ 
        
		XSSFWorkbook workbook = new XSSFWorkbook();
				
        for(Table tabella: this.tables){
        	buildSingleTable(tabella, workbook);
        }
        return buildInputStreamFile(workbook);
	}


	private void buildSingleTable(Table tabella, XSSFWorkbook workbook){
		XSSFSheet sheet = workbook.createSheet(tabella.getTableName());

        int rowNum = 0;
        
        for(TableRow riga: tabella){
        	//System.out.println(rowNum+""+riga);
        	Row row = buildRow(rowNum, sheet);
        	int colNum=0;
        	if(riga!=null){
        		for(CellValue valore: riga){
        			if(valore!=null){
	        		   buildCellValue(workbook, row,valore,colNum);
        			}
	        		colNum++;
	        	}
        	}else{
        		LOGGER.info("The row is empty" + rowNum);
        	}
        	
        	rowNum++;
        }
        
        int numberOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();
        for (int x = 0; x < numberOfColumns; x++) {
        	sheet.autoSizeColumn(x);
        }
	}
	
	
}
