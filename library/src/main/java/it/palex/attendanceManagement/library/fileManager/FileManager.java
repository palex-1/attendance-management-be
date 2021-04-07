package it.palex.attendanceManagement.library.fileManager;

import java.io.IOException;  
import java.io.InputStream;

import it.palex.attendanceManagement.library.utils.FileUtility;

public interface FileManager {

	/**
	 * 
	 * @param in
	 * @param outFileName
	 * @return the final name of file created
	 * @throws IOException 
	 */
	public String writeFile(InputStream in, String relativePath, String extension) throws Exception;
	
	/**
	 * 
	 * @param path
	 * @return the dimension of file in bytes
	 */
	public long getSizeOfFile(String path) throws Exception;
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public InputStream readFile(String filePath) throws Exception;
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public boolean deleteFile(String filePath) throws Exception;
	
	
	default String buildCurrentDayPath(){
		String subPath = FileUtility.getCurrentDaySubPath();
		
		return subPath;
	}
	
	default String concatPath(String path, String subpath) {
		return FileUtility.concatPath(path, subpath);
	}
	
	default String addExtensionToName(String path, String ext) {
		return FileUtility.addExtensionToName(path, ext);
	}
	
	public String concatToBasePath(String subpath);
	
	public String getType();
	
}

