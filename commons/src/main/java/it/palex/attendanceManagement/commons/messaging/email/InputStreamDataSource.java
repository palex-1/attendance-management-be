package it.palex.attendanceManagement.commons.messaging.email;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class InputStreamDataSource implements DataSource {
	
    private InputStream inputStream;
    private String mimeType = "*/*";
    
    public InputStreamDataSource(InputStream inputStream, String mimeType) {
        this.inputStream = inputStream;
        if(mimeType!=null) {
        	this.mimeType = mimeType;
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String getContentType() {
        return this.mimeType;
    }

    @Override
    public String getName() {
        return "InputStreamDataSource";
    }
    
}