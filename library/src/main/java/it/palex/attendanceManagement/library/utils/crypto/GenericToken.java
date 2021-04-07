package it.palex.attendanceManagement.library.utils.crypto;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * @author Alessandro Pagliaro
 *
 */
public class GenericToken implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GenericToken.class);

	public String token = "";

	public GenericToken(String token) {
		if (token == null) {
			throw new NullPointerException();
		}

	}

	public String getToken() {
		return token;
	}

	/**
	 * 
	 * @param info
	 * @return BooleanWithInfoResponse Marshalled in a string json
	 */
	public String toJson() {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json;
		try {
			json = ow.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error during unmarshalling of string", e);
			return "";
		}
		return json;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericToken other = (GenericToken) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GenericToken [token=" + token + "]";
	}

}
