package it.palex.attendanceManagement.library.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.library.exception.InvalidImageException;

@Component
public class ImageResizer {

	private static final String ORACLE_JDK_RUNTIME_NAME = "Java(TM) SE Runtime Environment";
	private static final String OPEN_JDK_RUNTIME_NAME = "OpenJDK Runtime Environment";
	
	private static final String JPG_IMAGE_EXTENSION_CONVERSION = "jpg";
	
	public static boolean isCurrentVMOracle() {
		return StringUtils.equalsIgnoreCase(ORACLE_JDK_RUNTIME_NAME, System.getProperty("java.runtime.name"));
	}
	
	public static boolean isCurrentVMOpenJDK() {
		return StringUtils.equalsIgnoreCase(OPEN_JDK_RUNTIME_NAME, System.getProperty("java.runtime.name"));
	}
	
	
//	public static void main(String[]args) throws Exception {
//		java.io.FileInputStream input = new java.io.FileInputStream(new File("C:\\Users\\Alessandro Pagliaro\\Desktop\\1226f7fb-a870-4065-8004-fc9194ad38eb.jpg"));
//		
//		InputStream rotatedImage = ImageRotationFix.rotateImage(input);
//		
//		java.io.FileOutputStream fileOut = new java.io.FileOutputStream(new java.io.File("C:\\Users\\Alessandro Pagliaro\\Desktop\\medium.jpg"));
//
//        ImageResizer resizer = new ImageResizer();
//		ByteArrayOutputStream out = resizer.scaleImage(rotatedImage, "jpg", 0.2f);
//		
//		out.writeTo(fileOut);
//		
//		out.close();
//	}
	
	
	
	private ByteArrayOutputStream convertToJpg(InputStream imageInputStream) throws IOException {
		if(imageInputStream==null) {
			throw new NullPointerException();
		}
		ByteArrayOutputStream convertedImageOutput = new ByteArrayOutputStream();
		
		BufferedImage image = ImageIO.read(imageInputStream);
		if(image==null) {
			throw new InvalidImageException();
		}
		
        BufferedImage result = new BufferedImage( image.getWidth(),  image.getHeight(), 
        		BufferedImage.TYPE_INT_RGB);
        
        result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        ImageIO.write(result, "jpg", convertedImageOutput);
        
        return convertedImageOutput;
	}
	
	
	
	/**
	 * If call this method remember to close the ByteArrayOutputStream returned
	 * @param inputStream
	 * @param imageExtension support only jpg format
	 * @param scalingFactor
	 * @return an unclosed ByteArrayOutputStream with the compressed image
	 * @throws IOException
	 */
	public ByteArrayOutputStream scaleImage(InputStream inputStream, String imageExtension, 
			float scalingFactor) throws IOException {
		if(inputStream==null || imageExtension==null) {
			throw new NullPointerException();
		}
		if(!StringUtils.equalsIgnoreCase(imageExtension, JPG_IMAGE_EXTENSION_CONVERSION)) {
			throw new IllegalArgumentException("Only <"+JPG_IMAGE_EXTENSION_CONVERSION+"> compression output is supported");
		}
		if(scalingFactor<=0 || scalingFactor>1) {
			throw new IllegalArgumentException("Invalid scaling factor. scalingFactor:"+scalingFactor);
		}
		
		//convert to jpg for openjdk issues with other formats
		ByteArrayOutputStream convertedImageOutputStream = this.convertToJpg(inputStream);
		ByteArrayInputStream convertedImageInputStream = new ByteArrayInputStream( convertedImageOutputStream.toByteArray() );
		convertedImageOutputStream.close();
		convertedImageOutputStream = null; //for garbage collection
		
		ByteArrayOutputStream scaledImageOutputStream = new ByteArrayOutputStream();
				
		BufferedImage image = ImageIO.read(convertedImageInputStream);
		convertedImageInputStream.close();
		convertedImageInputStream= null; //for garbage collection
		
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(imageExtension);
		ImageWriter writer = (ImageWriter) writers.next();
		
		ImageOutputStream ios = ImageIO.createImageOutputStream(scaledImageOutputStream);
		writer.setOutput(ios);
		
		ImageWriteParam param = writer.getDefaultWriteParam();
		
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		
	    param.setCompressionQuality(scalingFactor);
	    writer.write(null, new IIOImage(image, null, null), param);

	    ios.close();
	    writer.dispose();
	    
	    return scaledImageOutputStream;
	}
	
	/**
	 * 
	 * @param inputStreamImage
	 * @param inputFileExt
	 * @param fileManager
	 * @param desiredScaledWidth
	 * @return an InputStream of the scaled image
	 * @throws IOException
	 */
	public InputStream resizeToWidth(InputStream inputStreamImage, String inputFileExt,
				int desiredScaledWidth) throws IOException {
		if(inputStreamImage==null ) {
			throw new NullPointerException("inputStreamImage is null at resizeToWidth");
		}
		if(inputFileExt==null) {
			throw new NullPointerException("inputFileExt is null at resizeToWidth");
		}
		if(desiredScaledWidth<=0) {
			throw new IllegalArgumentException("Invalid scaled width");
		}
		
		BufferedImage resizedImage = ImageUtils.resizeToBufferedImage(
				inputStreamImage, desiredScaledWidth);
		
		if(resizedImage==null) { //cannot resize
			return null;
		}
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(resizedImage, inputFileExt, os);
		InputStream scaledImage = new ByteArrayInputStream(os.toByteArray());
		
		return scaledImage;
	}

	
}

