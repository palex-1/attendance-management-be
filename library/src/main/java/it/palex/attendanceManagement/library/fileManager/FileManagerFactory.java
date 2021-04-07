package it.palex.attendanceManagement.library.fileManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Alessandro Pagliaro
 *
 */
@Component()
public class FileManagerFactory {

	@Autowired
	private ApplicationContext appContext;
	
	public FileManager buildFileManager(String fileManager) {
		if(fileManager==null) {
			throw new NullPointerException();
		}
		return this.buildFileManager(FileManagerType.valueOf(fileManager));
	}
	
	
	public FileManager buildFileManager(FileManagerType fileManagerType) {
		if(fileManagerType==null) {
			throw new NullPointerException();
		}
		switch(fileManagerType){
//			case EncryptedFSFileManager:{
//				return (FileManager) appContext.getBean(FileManagerType.EncryptedFSFileManager.name());
//			}
			case StandardFSFileManager:{
				return (FileManager) appContext.getBean(FileManagerType.StandardFSFileManager.name());
			}
		default:
			throw new IllegalArgumentException("Unknown file manager");
		}
		
	}
}
