//package it.palex.attendanceManagement.commons.utils;
//
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.InvalidPathException;
//import java.util.UUID;
//
//import javax.imageio.ImageIO;
//
//import org.apache.commons.io.FileUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import it.palex.attendanceManagement.library.utils.FileUtility;
//
//@Component
//@Deprecated
///**
// * @deprecated Use --> documentService.saveFileWithDefaultFM(fileName, fileStream,
// * 											DocumentService.TEMP_PATH, DocumentService.TEMP_FILE_DESCRIPTION
// */
//public class TemporaryFileManager {
//
//	@Value("${temp-file-directory}")
//	private String tempFileDirectory;
//
//
//	@Deprecated
//	public File saveFileToTempDir(InputStream inputStream, String fileName)
//			throws InvalidPathException, IOException {
//		UUID uuid = UUID.randomUUID();
//		String randomUUIDString = uuid.toString();
//
//		String filePath = this.tempFileDirectory;
//
//		filePath =  FileUtility.concatPath(filePath, FileUtility.getCurrentDaySubPath());
//
//		FileUtility.makeFolder(filePath);
//
//		filePath = FileUtility.concatPath(filePath, randomUUIDString);
//
//		String ext = FileUtility.getFileExtension(fileName);
//		if(ext!=null) {
//			filePath = FileUtility.addExtensionToName(filePath, ext);
//		}
//		File file = new File(filePath);
//
//		FileUtils.copyInputStreamToFile(inputStream, file);
//
//		return file;
//	}
//
//	/**
//	 *
//	 * @param filePath
//	 * @return true if file does not exists or is a directory
//	 * @throws IOException
//	 */
//	@Deprecated
//	public boolean deleteFileFromTempDir(File file) throws IOException {
//		return FileUtility.safeDeleteFile(file);
//	}
//
//	@Deprecated
//	public File saveBufferedImageInPng(BufferedImage image) throws IOException {
//
//		UUID uuid = UUID.randomUUID();
//		String randomUUIDString = uuid.toString();
//		String filePath = this.tempFileDirectory;
//
//		filePath =  FileUtility.concatPath(filePath, FileUtility.getCurrentDaySubPath());
//
//		FileUtility.makeFolder(filePath);
//
//		filePath = FileUtility.concatPath(filePath, randomUUIDString);
//		filePath = FileUtility.addExtensionToName(filePath, "png");
//
//		File imageFile = new File(filePath);
//
//		ImageIO.write(image, "png", imageFile);
//
//		return imageFile;
//	}
//
//
//}
