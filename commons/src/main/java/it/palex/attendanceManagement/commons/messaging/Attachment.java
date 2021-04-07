package it.palex.attendanceManagement.commons.messaging;

import java.io.InputStream;

public class Attachment {
	
	private String fullName;
	private String mimeType;
	private InputStream  inputStream;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	

	
}