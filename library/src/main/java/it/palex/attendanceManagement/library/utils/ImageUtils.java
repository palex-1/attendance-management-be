package it.palex.attendanceManagement.library.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageUtils {

		
	public static void resizeToHeight(String inputImagePath, String outputImagePath, int desiredSizeScaled)
			throws IOException {
		if (inputImagePath == null || outputImagePath == null) {
			throw new NullPointerException();
		}

		if (desiredSizeScaled <= 0) {
			throw new IllegalArgumentException("Invalid desiredSizeScaled");
		}

		File inputFile = new File(inputImagePath);
		BufferedImage inputImage = ImageIO.read(inputFile);

		float percentage = ((float) desiredSizeScaled / (float) inputImage.getHeight());

		int scaledWidth = (int) (inputImage.getWidth() * percentage);
		int scaledHeight = (int) (inputImage.getHeight() * percentage);

		resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
	}

	public static void resizeToWidth(String inputImagePath, String outputImagePath, int desiredSizeScaled)
			throws IOException {
		if (inputImagePath == null || outputImagePath == null) {
			throw new NullPointerException();
		}

		if (desiredSizeScaled <= 0) {
			throw new IllegalArgumentException("Invalid desiredSizeScaled");
		}

		File inputFile = new File(inputImagePath);
		BufferedImage inputImage = ImageIO.read(inputFile);

		float percentage = ((float) desiredSizeScaled / (float) inputImage.getWidth());

		int scaledWidth = (int) (inputImage.getWidth() * percentage);
		int scaledHeight = (int) (inputImage.getHeight() * percentage);

		resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
	}

	/**
	 * Resizes an image to a absolute width and height (the image may not be
	 * proportional)
	 * 
	 * @param inputImagePath  Path of the original image
	 * @param outputImagePath Path to save the resized image
	 * @param scaledWidth     absolute width in pixels
	 * @param scaledHeight    absolute height in pixels
	 * @throws IOException
	 */
	public static void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight)
			throws IOException {
		// reads input image
		File inputFile = new File(inputImagePath);
		
		resize(new FileInputStream(inputFile), outputImagePath, scaledWidth, scaledHeight);
	}

	/**
	 * 
	 * @param inputImage
	 * @param scaledWidth
	 * @param scaledHeight
	 * @return a BufferedImage created from inputImage
	 */
	public static BufferedImage createBufferedImageOutput(BufferedImage inputImage, int scaledWidth, int scaledHeight) {
		// creates output image
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

		// scales the input image to the output image
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();
		
		return outputImage;
	}
	
//	public static void main(String...args) throws IOException {
//		FileInputStream in = new FileInputStream("C:\\Users\\Alessandro Pagliaro\\Downloads\\0.jfif");
//		BufferedImage image = resizeToBufferedImage(in, 500);
//		
//		System.out.println("");
//	}
	
	public static BufferedImage resizeToBufferedImage(InputStream inputStreamImage, int desiredSizeScaled) throws IOException {
		BufferedImage inputImage = ImageIO.read(inputStreamImage);
        
		if(inputImage==null) {
			return null;
		}
				
		float percentage = ((float) desiredSizeScaled / (float) inputImage.getHeight());

		int scaledWidth = (int) (inputImage.getWidth() * percentage);
		int scaledHeight = (int) (inputImage.getHeight() * percentage);
		
		// scales the input image to the output image
		BufferedImage outputImage = createBufferedImageOutput(inputImage, scaledWidth, scaledHeight);

		return outputImage;
	}
	
	public static BufferedImage resizeToBufferedImage(InputStream inputStreamImage, int scaledWidth, int scaledHeight) throws IOException {
		BufferedImage inputImage = ImageIO.read(inputStreamImage);
		
		if(inputImage==null) {
			return null;
		}
		
		// scales the input image to the output image
		BufferedImage outputImage = createBufferedImageOutput(inputImage, scaledWidth, scaledHeight);

		return outputImage;
	}
	
	
	public static void resize(InputStream inputStreamImage, String outputImagePath, int scaledWidth, int scaledHeight)
			throws IOException {
		
		BufferedImage outputImage = resizeToBufferedImage(inputStreamImage, scaledWidth, scaledHeight);
		
		// extracts extension of output file
		String formatName = FileUtility.getFileExtension(outputImagePath);

		// writes to output file
		ImageIO.write(outputImage, formatName, new File(outputImagePath));
	}
	
}
