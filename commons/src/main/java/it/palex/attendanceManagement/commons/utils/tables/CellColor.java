package it.palex.attendanceManagement.commons.utils.tables;

public enum CellColor {

	WHITE(ColorCodes.WHITE_CODE),
	LIGHT_BLUE(ColorCodes.LIGHT_BLUE_CODE),
	AQUA(ColorCodes.AQUA_CODE),
	GREY_25_PERCENT(ColorCodes.GREY_25_PERCENT),
	YELLOW(ColorCodes.YELLOW),
	RED(ColorCodes.RED);
	
	public final short color;
	
	CellColor(short color){
		this.color = color;
	}
	
	
	public static class ColorCodes {
		public static final short WHITE_CODE = 0;
		public static final short LIGHT_BLUE_CODE = 1;
		public static final short AQUA_CODE = 2;
		public static final short GREY_25_PERCENT = 3;
		public static final short YELLOW = 4;
		public static final short RED = 5;
	}
}
