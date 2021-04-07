package it.palex.attendanceManagement.library.utils.crypto.sym;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class BytesSerializer extends StdSerializer<Bytes>{

	private static final long serialVersionUID = 8050796548122910375L;

	public BytesSerializer() {
        super(Bytes.class);
    }

    @Override
    public void serialize(Bytes value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(Base64.getEncoder().encodeToString(value.getBytes()));
    }
}
