package it.palex.attendanceManagement.data.dto.core;

public class ExpenseReportElementDTO {

	private Long id;
	private String description;
	private Double amount;
	private Boolean accepted;
	private DocumentDTO attachment;
	private Long expenseReportId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public DocumentDTO getAttachment() {
		return attachment;
	}

	public void setAttachment(DocumentDTO attachment) {
		this.attachment = attachment;
	}

	public Long getExpenseReportId() {
		return expenseReportId;
	}

	public void setExpenseReportId(Long expenseReportId) {
		this.expenseReportId = expenseReportId;
	}

}
