package it.palex.attendanceManagement.commons.utils.document;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.IndexedColors;

import it.palex.attendanceManagement.commons.utils.tables.CellColor;
import it.palex.attendanceManagement.commons.utils.tables.Table;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public interface MultipleTableToDocumentBuilder {

	public void buildTables(ArrayList<Table> tabelle);

	default short colorToApachePoiColor(int index) {
		switch (index) {
		case CellColor.ColorCodes.WHITE_CODE:
			return IndexedColors.WHITE.index;
		case CellColor.ColorCodes.LIGHT_BLUE_CODE:
			return IndexedColors.LIGHT_BLUE.index;
		case CellColor.ColorCodes.AQUA_CODE:
			return IndexedColors.AQUA.index;
		case CellColor.ColorCodes.GREY_25_PERCENT:
			return IndexedColors.GREY_25_PERCENT.index;
		case CellColor.ColorCodes.YELLOW:
			return IndexedColors.YELLOW.index;
		case CellColor.ColorCodes.RED:
			return IndexedColors.RED.index;	
		default:
			return IndexedColors.WHITE.index;
		}

	}

}
