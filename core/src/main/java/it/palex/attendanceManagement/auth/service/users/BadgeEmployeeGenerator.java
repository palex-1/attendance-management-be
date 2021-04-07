package it.palex.attendanceManagement.auth.service.users;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import it.palex.attendanceManagement.data.dto.documents.DocumentoReadInternalResponse;
import it.palex.attendanceManagement.data.entities.Company;
import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.GlobalConfigurations;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.UserProfileImage;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedImageCompression;
import it.palex.attendanceManagement.data.exceptions.InvalidConfigurationException;
import it.palex.attendanceManagement.data.service.core.CompanyService;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.service.core.UserProfileImageService;
import it.palex.attendanceManagement.data.service.documento.DocumentService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.library.exception.BadDataException;
import it.palex.attendanceManagement.library.exception.NotFoundException;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;

@Service
public class BadgeEmployeeGenerator implements GenericService {

	private static final int BUFFER_SIZE = 1024;

	@Value("${app.company-id}")
    private String companyId;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private UserProfileImageService userProfileImageService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private GlobalConfigurationsService globalConfigurationsService;
	
	@Autowired
	private CompanyService companyService;
	
	
	public GenericResponse<StringDTO> findCompanyId(){
		return this.buildStringOkResponse(companyId);
	}
	
	
	public DocumentoReadInternalResponse generateBadgeForUser(Integer userId) throws Exception {
		if(userId==null) {
			throw new BadDataException();
		}
		
		UserProfile profile = this.userProfileService.findById(userId);
		
		if(profile==null) {
			throw new NotFoundException("User not found");
		}
		
		String name = profile.getName();
		String surname = profile.getSurname();
		
		InputStream profileImage = null;
		UserProfileImage image = this.userProfileImageService.findBestMatchUserProfileImage(profile, SupportedImageCompression.MEDIUM);
		if(image!=null) {
			profileImage = this.documentService.openInputStreamOnFile(image.getProfileImageId());			
		}
		
		
		Company company = this.companyService.findRootCompany();
		
		String companyName = company==null ? "":company.getName();
		
		
		GlobalConfigurations companyLogoDocumentId = this.globalConfigurationsService.findByAreaAndKey(
				GlobalConfigurationSettingsTuple.BADGE.AREA_NAME, 
				GlobalConfigurationSettingsTuple.BADGE.COMPANY_LOGO_DOCUMENT_ID);
		
		InputStream companyLogoInputStream = null;
		
		if(companyLogoDocumentId!=null) {
			try {
				Long documentId = Long.parseLong( companyLogoDocumentId.getSettingValue());
				Document doc = this.documentService.findDocumentById(documentId);
				
				if(doc!=null) {
					companyLogoInputStream = this.documentService.openInputStreamOnFile(doc);
				}
				
			}catch(NumberFormatException e) {
				throw new InvalidConfigurationException("area:"+GlobalConfigurationSettingsTuple.BADGE.AREA_NAME
						+", key:"+GlobalConfigurationSettingsTuple.BADGE.COMPANY_LOGO_DOCUMENT_ID);
			}
			
		}
			
		
		ByteArrayInputStream badge = this.generateBadge(companyName, name, surname, 
				userId, companyLogoInputStream, profileImage);
		
		
		StreamingResponseBody responseBody =  new StreamingResponseBody() {
			BufferedInputStream buffStr = new BufferedInputStream(badge, 1024);
			
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
        
        return new DocumentoReadInternalResponse(responseBody, "badge_user"+userId+".pdf");
	}
	
	
	
	
	
	private ByteArrayInputStream generateBadge(String companyName, String name, String surname, Integer userId,
			InputStream companyIcon, InputStream profileImage) throws Exception {
		if(userId==null) {
			throw new NullPointerException();
		}
		
		PDDocument doc = new PDDocument();   
		PDPage page = new PDPage();
        doc.addPage(page);
        
        //add U as start and add \n to submit forms automatically
		String user = companyId+""+userId+"\n";
		
		BufferedImage bufferedImage = generateEAN13BarcodeImage(user);
		bufferedImage= rotateClockwise90(bufferedImage);
		
		PDImageXObject ximage = JPEGFactory.createFromImage(doc, bufferedImage);
		
		PDPageContentStream contentStream = new PDPageContentStream(doc, page);
		
		contentStream.drawImage(ximage, 10f, -20f);
		
		
		if(companyIcon!=null) {
			BufferedImage logoBufferedImage = ImageIO.read(companyIcon);
			logoBufferedImage = rotateClockwise90(logoBufferedImage);
			
			PDImageXObject logoImage = JPEGFactory.createFromImage(doc, logoBufferedImage);
			
			contentStream.drawImage(logoImage, 370f, 660f, 100, 100);
			
			companyIcon.close();
		}
		
		if(profileImage!=null) {
			BufferedImage userBufferedImage = ImageIO.read(profileImage);
			userBufferedImage = rotateClockwise90(userBufferedImage);
			
			PDImageXObject userImage = JPEGFactory.createFromImage(doc, userBufferedImage);
			
			contentStream.drawImage(userImage, 170f, 600f, 180, 160);
			
			profileImage.close();
		}
		
		float height = page.getMediaBox().getHeight();
		float width = page.getMediaBox().getWidth();
		contentStream.transform(Matrix.getRotateInstance(-Math.PI / 2, (height - width) / 2, (height - width) / 2));

		
		contentStream.moveTo(0, 0);
		contentStream.beginText();
		
		if(companyName!=null) {
			int offset = -570;
			if(companyName.length()<18) {
				offset = -510;
			}
			contentStream.newLineAtOffset(offset, 320);
			contentStream.setFont(PDType1Font.HELVETICA, 48);
			//contentStream.setNonStrokingColor(15, 83, 158);
			contentStream.showText(companyName);
			contentStream.endText();
			
		}
		
		
		if(name!=null) {
			contentStream.beginText();
			contentStream.newLineAtOffset(-460, 220);
			contentStream.setFont(PDType1Font.HELVETICA, 36);
			contentStream.showText(name.toUpperCase());
			contentStream.endText();
		}
		
		if(surname!=null) {
			contentStream.beginText();
			contentStream.newLineAtOffset(-460, 160);
			contentStream.setFont(PDType1Font.HELVETICA, 36);
			contentStream.showText(surname.toUpperCase());
			contentStream.endText();
		}
		
		
		
		
		contentStream.close();
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		doc.save(outputStream);
		doc.close();
		
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		outputStream.close();
		outputStream = null;
		
		return inputStream;
	}
	
	
	
	private static BufferedImage rotateClockwise90(BufferedImage src) {
	    int width = src.getWidth();
	    int height = src.getHeight();

	    BufferedImage dest = new BufferedImage(height, width, src.getType());

	    Graphics2D graphics2D = dest.createGraphics();
	    graphics2D.translate((height - width) / 2, (height - width) / 2);
	    graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
	    graphics2D.drawRenderedImage(src, null);

	    return dest;
	}
	
	
	private static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
	    Code128Writer barcodeWriter = new Code128Writer();
	    BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, 840, 130);
	 
	    return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
	
	
}
