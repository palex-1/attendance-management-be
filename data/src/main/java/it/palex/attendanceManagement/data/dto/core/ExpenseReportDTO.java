package it.palex.attendanceManagement.data.dto.core;

import java.util.Date;

public class ExpenseReportDTO {

	private Long id;
	private String title;
	private Date dateOfExpence;
	private Double amount;
	private Double amountAccepted;
	private String notes;
	private String status;
	private String location;
	private UserProfileSmallDTO madeBy;
	private UserProfileSmallDTO processedBy;
	private UserProfileSmallDTO processingBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDateOfExpence() {
		return dateOfExpence;
	}

	public void setDateOfExpence(Date dateOfExpence) {
		this.dateOfExpence = dateOfExpence;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public UserProfileSmallDTO getMadeBy() {
		return madeBy;
	}

	public void setMadeBy(UserProfileSmallDTO madeBy) {
		this.madeBy = madeBy;
	}

	public UserProfileSmallDTO getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(UserProfileSmallDTO processedBy) {
		this.processedBy = processedBy;
	}

	public UserProfileSmallDTO getProcessingBy() {
		return processingBy;
	}

	public void setProcessingBy(UserProfileSmallDTO processingBy) {
		this.processingBy = processingBy;
	}

	public Double getAmountAccepted() {
		return amountAccepted;
	}

	public void setAmountAccepted(Double amountAccepted) {
		this.amountAccepted = amountAccepted;
	}

	

}
