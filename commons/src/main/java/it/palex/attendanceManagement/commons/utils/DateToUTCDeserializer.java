package it.palex.attendanceManagement.commons.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import it.palex.attendanceManagement.library.utils.DateUtility;

/**
 * Use this class only if you cannot set virtual machine jvm local time to UTC
 * 
 * @author Alessandro Pagliaro
 *
 */
public class DateToUTCDeserializer extends StdDeserializer<Date>{

	private static final long serialVersionUID = 2764688824304637650L;

	public DateToUTCDeserializer() {
        this(null);
    }
	
	public DateToUTCDeserializer(Class<?> vc) {
        super(vc);
    }
 
    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context)
      throws IOException, JsonProcessingException {
        String date = jsonparser.getText();
        try {
        	
        	SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
    		Date deserializedDate = dateFormatGmt.parse(date);
    		
    		Date utcDeserializedDate = DateUtility.dateToUTC(deserializedDate);
    		
            return utcDeserializedDate;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    

}
