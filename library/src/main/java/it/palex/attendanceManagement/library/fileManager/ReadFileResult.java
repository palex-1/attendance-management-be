package it.palex.attendanceManagement.library.fileManager;

import java.io.InputStream;

public class ReadFileResult {

	private InputStream stream;
	private int fileLenght;
	
	public ReadFileResult(InputStream stream, int fileLenght) {
		super();
		this.stream = stream;
		this.fileLenght = fileLenght;
	}
	
	public InputStream getStream() {
		return stream;
	}
	public void setStream(InputStream stream) {
		this.stream = stream;
	}
	
	public int getFileLenght() {
		return fileLenght;
	}
	public void setFileLenght(int fileLenght) {
		this.fileLenght = fileLenght;
	}
	
}

