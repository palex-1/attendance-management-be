package it.palex.attendanceManagement.library.rest;

import java.io.Serializable;
import java.util.UUID; 

public class GenericResponse<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String version = null;
	private T data = null;
	private int code = 0;
	private String message = null;
	private int subcode = 0;
	private String operationIdentifier;
	
	public GenericResponse(){
		super();
		this.operationIdentifier = UUID.randomUUID().toString();
	}
	/**
	 * 
	 * @param data
	 * @param code
	 * @param message
	 */
	public GenericResponse(T data, int code, String message) {
		this();
		this.data = data;
		this.code = code;
		this.message = message;
	}
	
	public GenericResponse(T data, int code, String message, int subcode) {
		this();
		this.data = data;
		this.code = code;
		this.message = message;
		this.subcode = subcode;
	}

	/**
	 * 
	 * @param version
	 * @param data
	 * @param code
	 * @param message
	 */
	public GenericResponse(String version, T data, int code, String message) {
		this();
		this.version = version;
		this.data = data;
		this.code = code;
		this.message = message;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getSubcode() {
		return subcode;
	}
	
	public void setSubcode(int subcode) {
		this.subcode = subcode;
	}
	
	/**
	 * @return the operationIdentifier
	 */
	public String getOperationIdentifier() {
		return operationIdentifier;
	}
	
	public void setOperationIdentifier(String uuid) {
		this.operationIdentifier = uuid;
	}
	
	
	@Override
	public String toString() {
		return "GenericResponse [version=" + version + ", data=" + data + ", code=" + code + ", message=" + message
				+ ", subcode=" + subcode + ", operationIdentifier=" + operationIdentifier + "]";
	}
	
}

