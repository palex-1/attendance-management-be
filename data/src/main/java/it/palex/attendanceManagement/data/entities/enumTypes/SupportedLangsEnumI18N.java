package it.palex.attendanceManagement.data.entities.enumTypes;

/**
 * 
 * @author Alessandro Pagliaro
 *
 * NOTE: MAX SIZE OF THE ENUM IS 10 CHARS
 */
public enum SupportedLangsEnumI18N {
	IT;
	
	
	public static SupportedLangsEnumI18N DEFAULT_USER_LANG() {
		return SupportedLangsEnumI18N.IT;
	}
	
	public static boolean isValid(String lang) {
		if(lang==null) {
			return false;
		}
		
		try {
			SupportedLangsEnumI18N.valueOf(lang);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}

}
