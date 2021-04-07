package it.palex.attendanceManagement.commons.messaging;

import java.util.List;
import java.util.Map;

import javax.activation.DataSource;



public class Email {
	
	private String fromAddress;
	private List<String> toAddress;
	private String subject;
	private String text;
	private List<Attachment>  attachment;
	private List<String> toCC;
	private List<String> toBcc;
	private Map<String, DataSource> imageResourcesToShow;
	

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public List<String> getToAddress() {
		return toAddress;
	}

	public void setToAddress(List<String> toAddress) {
		this.toAddress = toAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Attachment> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<Attachment> attachment) {
		this.attachment = attachment;
	}

	public List<String> getToCC() {
		return toCC;
	}

	public void setToCC(List<String> toCC) {
		this.toCC = toCC;
	}

	public List<String> getToBcc() {
		return toBcc;
	}

	public void setToBcc(List<String> toBcc) {
		this.toBcc = toBcc;
	}

	public Map<String, DataSource> getImageResourcesToShow() {
		return imageResourcesToShow;
	}

	public void setImageResourcesToShow(Map<String, DataSource> imageResourcesToShow) {
		this.imageResourcesToShow = imageResourcesToShow;
	}
	
	

}
