package it.palex.attendanceManagement.data.dto.documents;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public class DocumentoReadInternalResponse {

	private StreamingResponseBody responseBody;
	private String fileName;
	
	public DocumentoReadInternalResponse(StreamingResponseBody responseBody, String fileName) {
		super();
		this.responseBody = responseBody;
		this.fileName = fileName;
	}
	
	public StreamingResponseBody getResponseBody() {
		return responseBody;
	}
	
	public String getFileName() {
		return fileName;
	}
	
}
