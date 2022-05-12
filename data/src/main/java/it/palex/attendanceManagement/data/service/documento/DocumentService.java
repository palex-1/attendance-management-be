package it.palex.attendanceManagement.data.service.documento;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import javax.annotation.PostConstruct;

import com.querydsl.core.BooleanBuilder;
import it.palex.attendanceManagement.data.entities.QDocument;
import it.palex.attendanceManagement.data.entities.core.QUserAttendance;
import it.palex.attendanceManagement.library.utils.IterableUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import it.palex.attendanceManagement.data.dto.core.VideoReadingResponse;
import it.palex.attendanceManagement.data.dto.documents.DocumentoReadInternalResponse;
import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.GlobalConfigurations;
import it.palex.attendanceManagement.data.entities.TicketDownload;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.exceptions.InvalidConfigurationException;
import it.palex.attendanceManagement.data.repository.documento.DocumentRepository;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.library.exception.InvalidImageException;
import it.palex.attendanceManagement.library.fileManager.FileManager;
import it.palex.attendanceManagement.library.fileManager.FileManagerFactory;
import it.palex.attendanceManagement.library.fileManager.FileManagerType;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.FileUtility;
import it.palex.attendanceManagement.library.utils.ImageResizer;

@Service
public class DocumentService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DocumentService.class);

	private final QDocument QDoc = QDocument.document;

	/** This is a temp path where file can be stored for temp use */
	public static final String TEMP_PATH = "TEMP";
	public static final String TEMP_FILE_DESCRIPTION = "THIS_IS_A_TEMPORARY_FILE";

	private static final int BUFFER_SIZE = 1024;

	@Autowired
	private DocumentRepository documentRepo;
		
	@Autowired
	private FileManagerFactory fileManagerFactory;
	
	@Autowired
	private TicketDownloadService ticketDownloadService;
	
	@Autowired
	private GlobalConfigurationsService globalConfigurationsService;
	
	@Autowired
	private ImageResizer imageResizer;
	
	private FileManagerType defaultFileManager;
	
	
	@PostConstruct
	private void initializeDefaultFM() {
		this.initializeDefaultNotCryptedFM();
	}
	
	private void initializeDefaultNotCryptedFM() {
		GlobalConfigurations config = this.globalConfigurationsService.findByAreaAndKeyAndAssertNotNull(
				GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.AREA_NAME,
				GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.DEFAULT_FILE_MANAGER);
		
		if(!FileManagerType.isValid(config.getSettingValue())){
			throw new InvalidConfigurationException(
					"INVALID config --> area="+GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.AREA_NAME+","
					+ "key="+GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.DEFAULT_FILE_MANAGER);
		}
		
		this.defaultFileManager =  FileManagerType.valueOf(config.getSettingValue());
	}
	
	
	private FileManagerType getDefaultFileManager() {
		return this.defaultFileManager;
	}
	
	/**
	 * 
	 * @param id
	 * @return null if id is null or no document has the specified id otherwise the document with that <b>id</b>
	 */
	public Document getById(Long id) {
		if(id==null) {
			return null;
		}
		Optional<Document> doc = this.documentRepo.findById(id);
		
		if(doc.isPresent()) {
			return doc.get();
		}
		
		return null;
	}
	
	public Document save(Document doc) {
		if(doc==null) {
			throw new NullPointerException();
		}
		if(!doc.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(doc);
		}
		
		return this.documentRepo.save(doc);
	}
	
	
	
	public Document saveFileWithDefaultFM(String fileNameWithExt, String base64FileContent, 
			Integer userId, String description) throws Exception {
		if(base64FileContent==null) {
			throw new NullPointerException();
		}
		int mimeTypeEnd = base64FileContent.indexOf(',');
		String base64Final = base64FileContent;
		
		if(mimeTypeEnd >= 0) {
			base64Final = base64Final.substring(mimeTypeEnd+1);
		}
		
		byte[] data = Base64.getDecoder().decode(base64Final);
		InputStream arrayInputStream = new ByteArrayInputStream(data);
		
		return this.saveFileWithDefaultFM(fileNameWithExt, arrayInputStream, userId+"", description);
	}
	
	public Document saveFileWithDefaultFM(String fileNameWithExt, File file, 
			Integer userId, String description) throws Exception {
		if(file==null) {
			throw new NullPointerException();
		}
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		
		return this.saveFileWithDefaultFM(fileNameWithExt, inputStream, userId+"", description);
	}

	/**
	 *
	 * @param fileExt
	 * @param inputStream
	 * @return
	 * @throws Exception if file saving error occurred
	 * @throws NullPointerException if fileExt or inputStream are null
	 * @throws IllegalArgumentException if fileExt start with a dot '.'
	 */
	public Document saveTempFileWithDefaultFM(String fileExt, InputStream inputStream) throws Exception{
		if(fileExt==null){
			throw new NullPointerException();
		}
		if(fileExt.trim().startsWith(".")){
			throw new IllegalArgumentException("File extension cannot start with '.'");
		}
		UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString()+"."+fileExt.trim();

		return this.saveFileWithDefaultFM(fileName, inputStream,
				DocumentService.TEMP_PATH, DocumentService.TEMP_FILE_DESCRIPTION);
	}

	public Document saveFileWithDefaultFM(String fileNameWithExt, InputStream inputStream, 
			String subfolder, String description) throws Exception {
		if(fileNameWithExt==null || inputStream==null) {
			throw new NullPointerException();
		}
		if(!Document.isValidDescription(description)) {
			throw new DataCannotBeInsertedInDatabase();
		}
		
		FileManager fileManager = this.fileManagerFactory.buildFileManager(
				getDefaultFileManager());
		
		return completeFileSave(fileNameWithExt, inputStream, subfolder, description, fileManager);
	}
	

	public Document saveFileWithDefaultCryptedFM(String fileNameWithExt, InputStream inputStream, 
			Integer userId, String description) throws Exception {
		if(inputStream==null) {
			throw new NullPointerException();
		}
		return this.saveFileWithDefaultCryptedFM(fileNameWithExt, inputStream, userId+"", description);
	}
	
	public Document saveFileWithDefaultCryptedFM(String fileNameWithExt, InputStream inputStream, 
			String subfolder, String description) throws Exception {
		if(fileNameWithExt==null || inputStream==null) {
			throw new NullPointerException();
		}
		if(!Document.isValidDescription(description)) {
			throw new DataCannotBeInsertedInDatabase();
		}
		
		FileManager fileManager = this.fileManagerFactory.buildFileManager(
				    //TODO develop a new crypted file manager
					getDefaultFileManager()
				);
		
		return completeFileSave(fileNameWithExt, inputStream, subfolder, description, fileManager);
	}
	

	private Document completeFileSave(String fileNameWithExt, InputStream inputStream, String subfolder,
			String description, FileManager fileManager) throws Exception {
		String ext = FileUtility.getFileExtension(fileNameWithExt);
		String subfolderStr = subfolder!=null ? subfolder+"" : null;

		String fileFinalPath = fileManager.writeFile(inputStream, subfolderStr, ext);
				
		long fileSize = fileManager.getSizeOfFile(fileFinalPath);
		
		Document doc = new Document();
		doc.setFileManager(fileManager.getType());
		doc.setFullFileName(fileNameWithExt);
		doc.setFilePath(fileFinalPath);
		doc.setDescription(description);
		doc.setFileSize(fileSize);
		
		if(!doc.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase("Internal Error!!! persisting document");
		}
		
		
		return this.documentRepo.save(doc);
	}
	
	
	
	
	
	public InputStream openInputStreamOnFile(Document doc) throws Exception {
		if(doc==null) {
			return null;
		}
		
		String fileManagerUsed = doc.getFileManager();
		
		FileManager fileManager = this.fileManagerFactory.buildFileManager(
				fileManagerUsed);
		
		return fileManager.readFile(doc.getFilePath());
	}

	/**
	 * This method can be used to download data from browser
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public DocumentoReadInternalResponse openStreamOnFile(Document doc) throws Exception {
		if(doc==null) {
			return null;
		}
		StreamingResponseBody responseBody;
		String fileName = doc.getFullFileName();
		
		String fileManagerUsed = doc.getFileManager();
		
		FileManager fileManager = this.fileManagerFactory.buildFileManager(
				fileManagerUsed);

		InputStream inputStream = fileManager.readFile(doc.getFilePath());
		
		responseBody =  new StreamingResponseBody() {
				BufferedInputStream buffStr = new BufferedInputStream(inputStream, 1024);
				
	            @Override
	            public void writeTo (OutputStream out) throws IOException {
	            	try {
	            		byte []arr = new byte [BUFFER_SIZE];
	                	int available  = -1;
	                	while((available = buffStr.read(arr)) > 0) {   
	                		out.write(arr, 0, available); 
	                	}      
	                	out.flush();
	                	out.close();
	            	}finally {
	            		buffStr.close();
	            	}
	            		
	            }
	        };
	        
	    return new DocumentoReadInternalResponse(responseBody, fileName);
	}

	public byte[] retrieveBase64(Document doc) throws Exception {
		String fileManagerUsed = doc.getFileManager();

		FileManager fileManager = this.fileManagerFactory.buildFileManager(
				fileManagerUsed);

		byte[] documentEncoded;
		try(InputStream inputStream = fileManager.readFile(doc.getFilePath())){
			documentEncoded = IOUtils.toByteArray(inputStream);
		}
		return documentEncoded;
	}

	public void deleteDocumentAndFile(Document document) throws Exception {
		if(document==null) {
			throw new NullPointerException();
		}
		FileManager fileManager = this.fileManagerFactory.buildFileManager(
				document.getFileManager());
		fileManager.deleteFile(document.getFilePath());
		
		this.documentRepo.delete(document);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteDocumentAndFileWithTransact(Document document) throws Exception {
		this.deleteDocumentAndFile(document);
	}
	
	public void deleteOnlyDocument(Document document) throws Exception {
		if(document==null) {
			throw new NullPointerException();
		}
		this.documentRepo.delete(document);
	}
	
	public void deleteOnlyFile(Document document) throws Exception {
		if(document==null) {
			throw new NullPointerException();
		}
		FileManager fileManager = this.fileManagerFactory.buildFileManager(
				document.getFileManager());
		
		fileManager.deleteFile(document.getFilePath());
	}
	
	public byte[] retrieveBase64(String fileManagerUsed, String filePath) throws Exception {

		FileManager fileManager = this.fileManagerFactory.buildFileManager(
				fileManagerUsed);

		byte[] documentEncoded;
		try(InputStream inputStream = fileManager.readFile(filePath)){
			documentEncoded = IOUtils.toByteArray(inputStream);
		}
		return documentEncoded;
	}
	
	public Document saveFileWithDefaultFM(File file, String fileName,
			Integer userId, String description) throws IOException, Exception {
		if(file==null) {
			throw new NullPointerException();
		}
		return this.saveFileWithDefaultFM(new FileInputStream(file), fileName, userId, description);
	}
	
	public Document saveFileWithDefaultFM(InputStream stream, String fileName,
			Integer userId, String description) throws IOException, Exception {
		if(stream==null || fileName==null) {
			throw new NullPointerException();
		}
		if(!Document.isValidDescription(description)) {
			throw new DataCannotBeInsertedInDatabase();
		}
		
		FileManager fileManager = this.fileManagerFactory.buildFileManager(
				getDefaultFileManager());
		
		String ext = FileUtility.getFileExtension(fileName);
		String userIdStr = userId!=null ? userId+"" : null;

		
		String fileFinalPath = fileManager.writeFile(stream,
				userIdStr, ext);
		
		long fileSize = fileManager.getSizeOfFile(fileFinalPath);
		
		Document doc = new Document();
		doc.setFileManager(getDefaultFileManager().name());
		doc.setFullFileName(fileName);
		doc.setFilePath(fileFinalPath);
		doc.setDescription(description);
		doc.setFileSize(fileSize);
		
		if(!doc.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase("Internal Error!!! persisting document");
		}
		
		//stream.close();
		
		return this.documentRepo.save(doc);		
	}

	public Document saveFileWithDefaultFM(MultipartFile file, 
			Integer userId, String description) throws IOException, Exception {
		if(file==null) {
			throw new NullPointerException();
		}
		
		return this.saveFileWithDefaultFM(file.getInputStream(), file.getOriginalFilename(), 
				userId, description);
	}

	/**
     * 
	 * @param downloadToken
	 * @return
	 * @throws Exception
	*/
	public DocumentoReadInternalResponse openStreamOnFile(String downloadToken) throws Exception {
		Document doc = findDocumentByNonExpiredToken(downloadToken);
		 
		if(doc==null) {
			return null;
		}
		
		return openStreamOnFile(doc);
	}

	private Document findDocumentByNonExpiredToken(String downloadToken) {
		TicketDownload ticket = this.ticketDownloadService.findByToken(downloadToken);
		if(ticket==null) {
			return null;
		}
		if(ticket.getExpirationDate().before(DateUtility.getCurrentDateInUTC())) {
			this.ticketDownloadService.delete(ticket);
			return null;
		}
		
		Document doc = ticket.getDocument();
		
		if(ticket.getIsOneShot()) {
			this.ticketDownloadService.delete(ticket); //if is a one shot ticket remove after use
		}
		
		return doc;
	}
	
	
	public String getImageDefaultExtension() {
		GlobalConfigurations config = this.globalConfigurationsService.findByAreaAndKey(
				GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.AREA_NAME,
				GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.DEFAULT_IMAGE_CONVERSION_EXTENSION);
		
		if(config==null || StringUtils.isEmpty(config.getSettingValue())) {
			throw new InvalidConfigurationException(
					"Not found config --> area="+GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.AREA_NAME+","
					+ "key="+GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.DEFAULT_IMAGE_CONVERSION_EXTENSION);
		}
		
		return config.getSettingValue();
	}
	
	public float getDefaultCompressionLevel() {
		GlobalConfigurations config = this.globalConfigurationsService.findByAreaAndKey(
				GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.AREA_NAME,
				GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.DEFAULT_COMPRESSION_LEVEL);
		
		if(config==null || StringUtils.isEmpty(config.getSettingValue())) {
			throw new InvalidConfigurationException(
					"Not found config --> area="+GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.AREA_NAME+","
					+ "key="+GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.DEFAULT_COMPRESSION_LEVEL);
		}
		
		try {
			float park = Float.parseFloat(config.getSettingValue());
			
			if(park<=0 || park>1) {
				throw new InvalidConfigurationException("Value of compression must be less or equal to 1 and bigger than 0");
			}
			
			return park;
		}catch(Exception e) {
			throw new InvalidConfigurationException(
					"Not found config --> area="+GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.AREA_NAME+","
					+ "key="+GlobalConfigurationSettingsTuple.FILE_MANAGER_CONFIG.DEFAULT_COMPRESSION_LEVEL);
		}
	}
	
	
	/**
	 * 
	 * @param fileNameWithExt
	 * @param inputStream
	 * @param userId
	 * @param description
	 * @return
	 * @throws Exception if file saving exception occurs
	 * @throws InvalidImageException if the image is not a valid image
	 */
	public Document saveImageWithDefaultFM(String fileNameWithExt, 
			InputStream inputStream, String subpath, String description) throws Exception {
		
		String fileName = FileUtility.getFileNameWithoutExt(fileNameWithExt);
		
		String ext = getImageDefaultExtension();
		
		String fileNameWithNewExt = fileName+"."+ext;
				
		try {
			ByteArrayOutputStream os = compressImage(inputStream, ext);
			InputStream sanitizedImageStream = new ByteArrayInputStream(os.toByteArray());
			os.flush();
			//os.close();
			
			
			return this.saveFileWithDefaultFM
					(fileNameWithNewExt, sanitizedImageStream, subpath, description);
			
		}catch(javax.imageio.IIOException e) {
			LOGGER.error("Unsupported image extension", e);
			throw new InvalidImageException("Unsupported image extension");
		}
	}
	
	/**
	 * If call this method remember to close the OutputStream returned
	 * @param inputStream
	 * @throws IOException
	 */
	private ByteArrayOutputStream compressImage(InputStream inputStream, String imageExtension) throws IOException {
		float compressionLevel = getDefaultCompressionLevel();

	    return imageResizer.scaleImage(inputStream, imageExtension, compressionLevel);
	}
	

	/**
	 * 
	 * @param file
	 * @param userId
	 * @param description
	 * @return
	 * @throws IOException if file saving exception occurs
	 * @throws Exception if file saving exception occurs
	 * @throws InvalidImageException if the image is not a valid image
	 */
	public Document saveImageWithDefaultFM(MultipartFile file, 
			Integer userId, String description) throws IOException, Exception {
		return this.saveImageWithDefaultFM(file.getOriginalFilename(), 
				file.getInputStream(), userId+"", description);
	}


	public VideoReadingResponse openStreamOnVideoFile(String downloadToken) 
			throws Exception {
		if(downloadToken==null) {
			return null;
		}
		Document doc = this.findDocumentByNonExpiredToken(downloadToken);
		
		if(doc==null) {
			return null;
		}
		
		VideoReadingResponse videoResponse = new VideoReadingResponse();
				
		FileManager fileManager = this.fileManagerFactory.buildFileManager(
				doc.getFileManager());
		
		InputStream inputStream = fileManager.readFile(doc.getFilePath());
		
		videoResponse.setInputStream(inputStream);
		videoResponse.setContentLenght(doc.getFileSize());
		videoResponse.setFileNameWithExt(doc.getFullFileName());
		
		return videoResponse;
	}

	public Document findDocumentById(Long documentId) {
		if(documentId==null) {
			return null;
		}
		Optional<Document> opt = this.documentRepo.findById(documentId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		
		return null;
	}


	public List<Document> findTempFilesCreatedBefore(Date createdBefore, PageRequest pageable) {
		if(pageable==null || createdBefore==null){
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QDoc.description.eq(TEMP_FILE_DESCRIPTION));

		cond.and(QDoc.createdDate.loe(createdBefore));

		List<Document> documents = IterableUtils.iterableToList(
				this.documentRepo.findAll(cond, pageable)
		);

		return documents;
	}


}
