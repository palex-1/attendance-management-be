package it.palex.attendanceManagement.library.fileManager;

/**
 * @author Alessandro Pagliaro
 *
 */
public enum FileManagerType {
//	EncryptedFSFileManager, 
	StandardFSFileManager;

	public static boolean isValid(String type) {
		if(type==null) {
			return false;
		}
		try {
			FileManagerType.valueOf(type);
			//if no IllegalArgumentException exception is thrown
			return true;
		}catch(IllegalArgumentException e) {
			return false;
		}
	}
	
}
