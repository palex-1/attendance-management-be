package it.palex.attendanceManagement.library.utils.crypto.sym;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class BytesDeserializer extends StdDeserializer<Bytes> {

	private static final long serialVersionUID = -8384123915433025277L;

	public BytesDeserializer() {
        super(Bytes.class);
    }

    @Override
    public Bytes deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        String base64 = node.asText();
        return new Bytes(Base64.getDecoder().decode(base64));
    }
}
