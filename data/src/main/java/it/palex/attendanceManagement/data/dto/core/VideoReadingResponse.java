package it.palex.attendanceManagement.data.dto.core;

import java.io.InputStream;

public class VideoReadingResponse {
	private InputStream inputStream;
	private long contentLenght;
	private String fileNameWithExt;
	
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public long getContentLenght() {
		return contentLenght;
	}
	public void setContentLenght(long contentLenght) {
		this.contentLenght = contentLenght;
	}
	
	public String getFileNameWithExt() {
		return fileNameWithExt;
	}
	public void setFileNameWithExt(String fileNameWithExt) {
		this.fileNameWithExt = fileNameWithExt;
	}
}
