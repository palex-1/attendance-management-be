package it.palex.attendanceManagement.library.utils.crypto.sym;

import java.util.Arrays;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = BytesSerializer.class)
@JsonDeserialize(using = BytesDeserializer.class)
public class Bytes {

	private byte[] bytes;

    public Bytes(byte[] bytes) {
    	if(bytes==null) {
    		throw new NullPointerException();
    	}
        this.bytes = Arrays.copyOf(bytes, bytes.length);
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }
}
