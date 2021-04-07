package it.palex.attendanceManagement.library.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.apache.commons.io.FilenameUtils;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
public class FileUtility {
	
	public static final String EXTENSION_SEPARATOR = ".";
	
	
	public static String getFileSeparator(){
		return "/";
	}

	/**
	 * 
	 * @param folder
	 * @param extension
	 * @return name of files contained in the folder with the specified extension.
	 */
	public static LinkedList<String> getFilteredStringNameFileInFolder(String folder, String extension){
		if(folder==null){
			throw new NullPointerException();
		}
		String extPark="";
		if(extension!=null){
			extPark=extension;
		}
    	LinkedList<String> textFiles = new LinkedList<>();
		  File dir = new File(folder);
		  for (File file : dir.listFiles()) {
		    if (file.getName().endsWith((extPark))) {
		      textFiles.add(file.getName());
		    }
		  }
		  return textFiles;
    }
	
	/**
	 * 
	 * @param folder
	 * @return a linked list with name of file conteined in the folder including subfolder
	 */
	public static LinkedList<String> getStringNameFileInFolder(String folder){
		if(folder==null){
			throw new NullPointerException();
		}
    	LinkedList<String> textFiles = new LinkedList<String>();
		  File dir = new File(folder);
		  for (File file : dir.listFiles()) {
		      textFiles.add(file.getName());
		  }
		  return textFiles;
		  
    }
	
	
	
	/**
	 * check if exist the folder and is a folder
	 * @param folderPath
	 * @return
	 */
	public static boolean checkEsistenzaCartella(String folderPath){
		if(folderPath==null){
			throw new NullPointerException();
		}
		Path file = new File(folderPath).toPath();
		return Files.isDirectory(file); 
	}
	
	
	/**
	 * delete all file contained in the specified folder
	 * @param folder
	 */
	public static void deleteAllFileInFolderExceptSubfolder(String folder){
		if(folder==null){
			throw new NullPointerException();
		}
		LinkedList<String> lista=getStringNameFileInFolder(folder);
		for(String fileName:lista){
			String pathComplete=folder+getFileSeparator()+fileName;
			boolean isAFile=checkIfIsAFile(pathComplete);
			if(isAFile){
				File file = new File(pathComplete);
				file.delete();
			}
		}
	}
	
	/**
     * 
     * @param path
     * @return true if the path on local file system is accessible and is a directory
     */
	public static boolean checkIfIsADirectory(String path){
		if(path==null){
			throw new NullPointerException();
		}
		File file = new File(path);
		return file.isDirectory(); 
	}
	
	
    /**
     * 
     * @param path
     * @return true if the path on local file system is accessible and exist
     */
	public static boolean checkIfPathExist(String path){
		if(path==null){
			throw new NullPointerException();
		}
		File file = new File(path);
		return file.exists();
	}
	
	
	/**
	 * 
	 * @param path
	 * @return true if is a file
	 */
	public static boolean checkIfIsAFile(String path){
		if(path==null){
			throw new NullPointerException();
		}
		File file = new File(path);
        return file.isFile();
	}
	
	/**
	 * 
	 * @param path
	 * @param ext
	 * @return true if the name of file ends with extension
	 */
	public static boolean checkIfIsAFileWithExtension(String path, String ext){
		if(path==null || ext==null){
			return false;
		}
		boolean isAFile=checkIfIsAFile(path);
		if(isAFile){
		      return path.toUpperCase().endsWith(ext.toUpperCase());
		}
		return false;
	}

	/**
	 * 
	 * @param path
	 * @param ext
	 * @return true if the name of file ends with extension
	 */
	public static boolean checkIfIsAFileWithExtension(File file, String ext){
		if(file==null || ext==null){
			return false;
		}
		boolean isAFile=checkIfIsAFile(file);
		if(isAFile){
		      return file.getName().toUpperCase().endsWith(ext.toUpperCase());
		}
		return false;
	}
	
	
	
	
	/**
	 * 
	 * @param path
	 * @return true if is a file
	 */
	public static boolean checkIfIsAFile(File file){
		if(file==null){
			return false;
		}

		return file.isFile();
	}
	
	
	
	/**
	 * This method returns the textual part of the filename after the last dot.There must be no directory separator after the dot. 
	 * foo.txt      --> "txt"
	 * a/b/c.jpg    --> "jpg"
	 * a/b.txt/c    --> ""
	 * a/b/c        --> ""
	 * null --> null
	 * @param file
	 * @return the extension based only on the file name, null if file is null or filename is null
	 */
	public static String getFileExtension(File file){
		if(file==null) {
			return null;
		}
		return getFileExtension(file.getName());
	    
	}
	
	
	/**
	 * The method is only based on last dot
	 * foo.txt      --> "txt"
	 * a/b/c.jpg    --> "jpg"
	 * a/b.txt/c    --> ""
	 * a/b/c        --> ""
	 * null --> null
	 * @param completePath
	 * @return the extension based only on the file name,  null if completePath is null
	 */
	public static String getFileExtension(String completePath){
		if(completePath==null) {
			return null;
		}
		return FilenameUtils.getExtension(completePath);
	}
	
	
	
	
	/**
	 *  Delete the folder. If the folderPath is not a valid directory the method return without throwing exception
	 * @param folderPath
	 * @throws IOException
	 */
	public static void deleteFolder(String folderPath) throws IOException{
		if(folderPath==null){
			throw new NullPointerException();
		}
		if(!FileUtility.checkIfIsADirectory(folderPath)){
			return;
		}
		LinkedList<String> lista=FileUtility.getStringNameFileInFolder(folderPath);
		for(String nomeFile: lista){
			String currentPath=folderPath+FileUtility.getFileSeparator()+nomeFile;
			File currentFile = new File(currentPath);
			
			if(FileUtility.checkIfIsADirectory(folderPath)){
				//delete subfolder recursively
				deleteFolder(currentPath);
			}
			
		    currentFile.delete();
		}
		
		
	}
	
	
	/**
	 * 
	 * @param folder
	 * @throws InvalidPathException - if the path string cannot be converted to a Path
	 * @throws IOException 
	 */
	public static void makeFolder(String folder)throws InvalidPathException, IOException{
		Path path = Paths.get(folder);
        //if directory exists?
        if (!Files.exists(path)) {
                Files.createDirectories(path);
        }
	}
	
	
	/**
	 * 
	 * @param path1
	 * @param path2
	 * @return true if, and only if, the two paths locate the same
	 * @throws IOException - if an I/O error occurs
	 */
	public static boolean arePointingTheSameFile(String path1, String path2) throws IOException{
		if(path1==null || path2==null){
			throw new NullPointerException();
		}
		Path p1 = Paths.get(path1);
		Path p2 = Paths.get(path2);
		return Files.isSameFile(p1,p2);
	}
	
	
	/**
	 * 
	 * @param path
	 * @return true if the file is deleted false otherwise
	 * Note: if the <strong>path</strong> is a directory the directory will not removed<br>
	 * The method works only with files<br>
	 * If the file is not found false will be returned
	 * @throws IOException 
	 */
	public static boolean safeDeleteFile(String path) throws IOException{
		if(path==null){
			throw new NullPointerException();
		}
		boolean isADir = FileUtility.checkIfIsADirectory(path);
		if(isADir){
			return false;
		}
		boolean isAFile = FileUtility.checkIfIsAFile(path);
		if(!isAFile){
			return false;
		}
		Path p = Paths.get(path);
		
		Files.delete(p);
		
		return true;
	}
	
	public static boolean safeDeleteFile(File file) throws IOException {
		return safeDeleteFile(file.getAbsolutePath());
	}
	
	/**
	 * 
	 * @param inputFileName
	 * @return the file name without extension
	 * @throws NullPointerException if <strong>fileName</strong> is null
	 * The method only use the last point and delete the extension
	 */
	public static String removeFileNameExtension(String fileName){
		if(fileName==null){
			throw new NullPointerException();
		}
		int index = fileName.lastIndexOf(".");
		if(index>=0){
			return fileName.substring(0, index);
		}
		return fileName;
	}
	
	
	/**
	 * 
	 * @param path
	 * @return file name with extension
	 */
	public static String getFileName(String path){
		if(path==null){
			throw new NullPointerException();
		}
		String park = path.replaceAll("\\\\", "/");
		
		int idx = park.lastIndexOf("/");
		
		
		return idx >= 0 ? park.substring(idx + 1) : path;
	}
	
	/**
	 * 
	 * @param path
	 * @return the directory where file is saved
	 */
	public static String getFileDirectory(String path){
		if(path==null){
			throw new NullPointerException();
		}
		String park = path.replaceAll("\\\\", "/");
		
		int idx = park.lastIndexOf("/");
		
		
		return idx > 0 ? park.substring(0, idx) : "";
	}
	
	/**
	 * 
	 * @param path
	 * @return file name without extension
	 */
	public static String getFileNameWithoutExt(String path) {
		String fileName = getFileName(path);
		
		return removeFileNameExtension(fileName);
	}
	
	 /**
	  * 
	  * @return a string for path like 2008/11/02
	  */
	 public static String getCurrentDaySubPath(){
			String date = DateUtility.getFormattedDataDDMMYYYY(DateUtility.getCurrentDateInUTC());
			String subPath = date.substring(6, 10)+FileUtility.getFileSeparator()
							  +date.substring(3, 5)+FileUtility.getFileSeparator()
							  +date.substring(0, 2);
			
			return subPath;
	}
	 
	 /**
	  * 
	  * @param path
	  * @param subpath
	  * @return the path concatenated
	  * @throws NullPointerException 
	  */
	public static String concatPath(String path, String subpath) {
		if(path==null || subpath==null) {
			throw new NullPointerException();
		}
		String p1 = path.replaceAll("\\\\", "/");
		String p2 = subpath.replaceAll("\\\\", "/");
		
		if(p1.endsWith("/") && p2.startsWith("/")) {
			return p1.substring(0, p1.length() - 1) + p2;
		}
		
		if(p1.endsWith("/") || p2.startsWith("/")) {
			return p1 + p2;
		}
		
		return p1+"/"+p2;
	}

	public static String addExtensionToName(String path, String ext) {
		if(path==null || ext==null) {
			throw new NullPointerException();
		}
		if(path.endsWith(ext)) {
			return path;
		}
		if(path.endsWith(".") && ext.startsWith(".")) {
			return path.substring(0, path.length() - 1) + ext;
		}
		
		if(path.endsWith(".") || ext.startsWith(".")) {
			return path + ext;
		}
		
		return path+"."+ext;
	}

	
	
}


