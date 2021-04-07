package it.palex.attendanceManagement.data.entities.core;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 7 lug 2020
 */
@Entity
@Table(name = "expense_report_element")
public class ExpenseReportElement extends AuditableEntity implements DatabaseCheckableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	public static final int DESCRIPTION_MAX_SIZE = 2147483647;
	
	@Size(max = DESCRIPTION_MAX_SIZE)
	@Column(name = "description")
	private String description;

	@Basic(optional = false)
	@NotNull
	@Column(name = "amount")
	private Double amount;
	
	@Basic(optional = false)
	@Column(name = "accepted")
	private Boolean accepted;

	@JoinColumn(name = "attachment", referencedColumnName = "id")
	@ManyToOne
	private Document attachment;

	@JoinColumn(name = "expense_report", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private ExpenseReport expenseReport;

	
	
	public ExpenseReportElement() {
	}

	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidDescription(this.description) && isValidAmount(this.amount)
				&& isValidExpenceReport(this.expenseReport) && isValidAttachment(this.attachment)
				&& isValidAccepted(this.accepted);
		
		return isValid;
	}
	

	public String whyCannotBeInsertedInDatabase() {
		String why = "description:"+this.description+", amount:"+this.amount+
					 ", expenseReport:"+this.expenseReport+", attachment:"+this.attachment+
					 ", accepted:"+this.accepted;
		
		return why;
	}
	
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

	public static boolean isValidDescription(String description) {
		if(description==null) {
			return true;
		}
		if(description.length()>DESCRIPTION_MAX_SIZE) {
			return false;
		}
		return true;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	public static boolean isValidAmount(Double amount) {
		return amount!=null;
	}
	

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public static boolean isValidAccepted(Boolean accepted) {
		return true;
	}
	
	public Document getAttachment() {
		return attachment;
	}

	public void setAttachment(Document attachment) {
		this.attachment = attachment;
	}

	public static boolean isValidAttachment(Document attachment) {
		return true;
	}
	
	
	public ExpenseReport getExpenseReport() {
		return expenseReport;
	}

	public void setExpenseReport(ExpenseReport expenseReport) {
		this.expenseReport = expenseReport;
	}

	public static boolean isValidExpenceReport(ExpenseReport expenseReport) {
		return expenseReport!=null;
	}
	
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ExpenseReportElement)) {
			return false;
		}
		ExpenseReportElement other = (ExpenseReportElement) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.palex.attendanceManagement.data.entities.ExpenseReportElement[ id=" + id + " ]";
	}

}
