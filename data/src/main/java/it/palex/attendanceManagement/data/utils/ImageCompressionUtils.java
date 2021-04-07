package it.palex.attendanceManagement.data.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.palex.attendanceManagement.data.entities.enumTypes.SupportedImageCompression;

public class ImageCompressionUtils {
	public static SupportedImageCompression findBestMatch(List<String> supportedExt, 
			SupportedImageCompression compression) {
		if(supportedExt==null || supportedExt.isEmpty() || compression==null) {
			return null;
		}
		
		if(SupportedImageCompression.NORMAL.equals(compression)) {
			return findBestMatchForNormal(supportedExt);
		}
		
		if(SupportedImageCompression.MEDIUM.equals(compression)) {
			return findBestMatchForMedium(supportedExt);
		}
		
		if(SupportedImageCompression.SMALL.equals(compression)) {
			return findBestMatchForSmall(supportedExt);
		}
		
		if(SupportedImageCompression.TINY.equals(compression)) {
			return findBestMatchForTiny(supportedExt);
		}
		
		return null;
	}

	private static SupportedImageCompression findBestMatchForTiny(List<String> supportedCompression) {
		if(existsInList(supportedCompression, SupportedImageCompression.TINY)) {
			return SupportedImageCompression.TINY;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.SMALL)) {
			return SupportedImageCompression.SMALL;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.MEDIUM)) {
			return SupportedImageCompression.MEDIUM;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.NORMAL)) {
			return SupportedImageCompression.NORMAL;
		}
		return null;
	}

	private static SupportedImageCompression findBestMatchForSmall(List<String> supportedCompression) {
		if(existsInList(supportedCompression, SupportedImageCompression.SMALL)) {
			return SupportedImageCompression.SMALL;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.MEDIUM)) {
			return SupportedImageCompression.MEDIUM;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.TINY)) {
			return SupportedImageCompression.TINY;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.NORMAL)) {
			return SupportedImageCompression.NORMAL;
		}
		return null;
	}

	private static SupportedImageCompression findBestMatchForMedium(List<String> supportedCompression) {
		if(existsInList(supportedCompression, SupportedImageCompression.MEDIUM)) {
			return SupportedImageCompression.MEDIUM;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.NORMAL)) {
			return SupportedImageCompression.NORMAL;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.SMALL)) {
			return SupportedImageCompression.SMALL;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.TINY)) {
			return SupportedImageCompression.TINY;
		}
		return null;
	}

	private static SupportedImageCompression findBestMatchForNormal(List<String> supportedCompression) {
		if(existsInList(supportedCompression, SupportedImageCompression.NORMAL)) {
			return SupportedImageCompression.NORMAL;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.MEDIUM)) {
			return SupportedImageCompression.MEDIUM;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.SMALL)) {
			return SupportedImageCompression.SMALL;
		}
		if(existsInList(supportedCompression, SupportedImageCompression.TINY)) {
			return SupportedImageCompression.TINY;
		}
		return null;
	}
	
	
	
	private static boolean existsInList(List<String> supportedCompression,
			SupportedImageCompression compression) {
		for(String comp: supportedCompression) {
			if(StringUtils.equalsIgnoreCase(comp, compression.name())) {
				return true;
			}
		}
		return false;
	}
}
