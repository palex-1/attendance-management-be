package it.palex.attendanceManagement.commons.utils;

import java.awt.image.BufferedImage; 
import java.io.File;
import java.io.IOException;
import org.jcodec.api.JCodecException;
import org.jcodec.api.awt.AWTFrameGrab;

public class VideoPreviewImageExtractor {

	
	/**
	 * Use ImageIO.write(frame, "png", new File(""))
	 * @param video
	 * @param frameNumber
	 * @return BufferedImage of the frame
	 * @throws IOException
	 * @throws JCodecException
	 */
	public static BufferedImage extractPreviewImageFrame(File video,
			int frameNumber) throws IOException, JCodecException {
		return AWTFrameGrab.getFrame(video, frameNumber);
	}
	
}

