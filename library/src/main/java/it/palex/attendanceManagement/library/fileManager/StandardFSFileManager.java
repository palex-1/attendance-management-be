package it.palex.attendanceManagement.library.fileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.library.utils.FileUtility;


/**
 * @author Alessandro Pagliaro
 *
 */
@Component("StandardFSFileManager")
public class StandardFSFileManager implements FileManager {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StandardFSFileManager.class);
	
	@Value("${file-manager.standard-fs.base-path}")
	private String basePath;
	
	@Override
	public String writeFile(InputStream in, String relativePath, String extension) throws IOException {
		if(in==null) {
			throw new NullPointerException();
		}
		String ext = "UNKNOWN";
		if(extension!=null) {
			ext = extension;
		}
		
		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();
		
		String filePath = this.basePath;
		if(relativePath!=null) {
			filePath = this.concatPath(filePath, relativePath);
		}
		filePath =  this.concatPath(filePath, this.buildCurrentDayPath());
		
		FileUtility.makeFolder(filePath);
		
		filePath = this.concatPath(filePath, randomUUIDString);
		
		filePath = this.addExtensionToName(filePath, ext);

		FileUtils.copyInputStreamToFile(in, new File(filePath));
		
		return filePath;
	}

	@Override
	public InputStream readFile(String filePath) throws Exception {
		if(filePath==null) {
			throw new NullPointerException();
		}
		FileInputStream inputStream = new FileInputStream(filePath);
		
		return inputStream;
	}

	/**
	 * @return false if the file not exists or is not a file. true if file is successfully removed
	 * @throws Exception if error occurs
	 */
	@Override
	public boolean deleteFile(String filePath) throws Exception {
		if(FileUtility.checkIfIsAFile(filePath)) {
			Path path = Paths.get(filePath);
			try {
				Files.delete(path);
			}catch(NoSuchFileException e) {
				LOGGER.error("File not found in path:"+filePath, e);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String concatToBasePath(String subpath) {
		if(subpath==null) {
			throw new NullPointerException();
		}
		return this.concatPath(this.basePath, subpath);
	}

	@Override
	public String getType() {
		return FileManagerType.StandardFSFileManager.name();
	}
	
	@Override
	public long getSizeOfFile(String filePath) {
		if(filePath==null) {
			throw new NullPointerException();
		}
		if(FileUtility.checkIfIsAFile(filePath)) {
			File f = new File(filePath);
			return f.length();
		}
		
		return -1;
	}

}

