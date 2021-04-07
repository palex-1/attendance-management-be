package it.palex.attendanceManagement.data.entities.enumTypes;

public enum SupportedImageCompression {
	NORMAL,
	MEDIUM,
	SMALL,
	TINY;
	
	public static final String NORMAL_STR = "NORMAL";
	public static final String MEDIUM_STR = "MEDIUM";
	public static final String SMALL_STR = "SMALL";
	public static final String TINY_STR = "TINY";
	
	
	public static boolean isValid(String compression) {
		if(compression==null) {
			return false;
		}
		try {
			SupportedImageCompression.valueOf(compression);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
	
}
