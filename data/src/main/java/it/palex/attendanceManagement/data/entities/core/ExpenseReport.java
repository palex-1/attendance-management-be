package it.palex.attendanceManagement.data.entities.core;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.enumTypes.ExpenseReportStatusEnum;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 6 lug 2020
 */
@Entity
@Table(name = "expense_report")
public class ExpenseReport extends AuditableEntity implements DatabaseCheckableEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	public static final int TITLE_MAX_SIZE = 2000;
	
	@Size(max = TITLE_MAX_SIZE)
	@NotNull
	@Column(name = "title")
	private String title;
		
	@Basic(optional = false)
	@NotNull
	@Column(name = "date_of_expence")
	@Temporal(TemporalType.DATE)
	private Date dateOfExpence;

	@Basic(optional = false)
	@NotNull
	@Column(name = "amount")
	private Double amount;

	@Basic(optional = false)
	@NotNull
	@Column(name = "amount_accepted")
	private Double amountAccepted;
	
	public static final int NOTES_MAX_SIZE = 2147483647;
	
	@Size(max = NOTES_MAX_SIZE)
	@Column(name = "notes")
	private String notes;

	public static final int STATUS_MAX_SIZE = 30;
	
	@Basic(optional = false)
	@NotNull
	@Size(max = STATUS_MAX_SIZE)
	@Column(name = "status")
	private String status;

	public static final int LOCATION_MAX_SIZE = 2000;

	@Size(max = LOCATION_MAX_SIZE)
	@Column(name = "location")
	private String location;

	@JoinColumn(name = "made_by", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private UserProfile madeBy;

	@JoinColumn(name = "processed_by", referencedColumnName = "id")
	@ManyToOne
	private UserProfile processedBy;

	@JoinColumn(name = "processing_by", referencedColumnName = "id")
	@ManyToOne
	private UserProfile processingBy;
	
	
	@JoinColumn(name = "task_id", referencedColumnName = "id")
	@ManyToOne(optional = true)
	private WorkTask workTask;
	
	
	public ExpenseReport() {
	}

	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidTitle(this.title) && isValidDateOfExpence(this.dateOfExpence)
				&& isValidAmount(this.amount) && isValidNotes(this.notes)
				&& isValidStatus(this.status) 
				&& isValidMadeBy(this.madeBy) && isValidProcessedBy(this.processedBy)
				&& isValidLocation(this.location) && isValidProcessingBy(this.processingBy)
				&& isValidAmountAccepted(this.amountAccepted)
				&& isValidWorkTask(this.workTask);
		
		return isValid;
	}

	

	public String whyCannotBeInsertedInDatabase() {
		String why  = "title:"+this.title+", dateOfExpence:"+this.dateOfExpence
		+", amount:"+this.amount+", notes:"+this.notes
		+", status:"+this.status
		+", madeBy:"+this.madeBy +", processedBy:"+this.processedBy
		+", location:"+this.location+", processingBy:"+this.processingBy
		+", amountAccepted:"+this.amountAccepted;
		
		return why;
	}
	
	
	
	
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

	public static boolean isValidTitle(String title) {
		if(title==null) {
			return false;
		}
		if(title.length()>TITLE_MAX_SIZE) {
			return false;
		}
		return true;
	}
	
	public Date getDateOfExpence() {
		return dateOfExpence;
	}

	public void setDateOfExpence(Date dateOfExpence) {
		this.dateOfExpence = dateOfExpence;
	}
	
	public static boolean isValidDateOfExpence(Date dateOfExpence) {
		return dateOfExpence!=null;
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
	
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public static boolean isValidNotes(String notes) {
		if(notes==null) {
			return true;
		}
		if(notes.length()>NOTES_MAX_SIZE) {
			return false;
		}
		return true;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static boolean isValidStatus(String status) {
		if(status==null) {
			return true;
		}
		if(status.length()>STATUS_MAX_SIZE) {
			return false;
		}
		return ExpenseReportStatusEnum.isValid(status);
	}
	
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public static boolean isValidLocation(String location) {
		if(location==null) {
			return true;
		}
		if(location.length()>LOCATION_MAX_SIZE) {
			return false;
		}
		return true;
	}
	
	

	public Double getAmountAccepted() {
		return amountAccepted;
	}

	public void setAmountAccepted(Double amountAccepted) {
		this.amountAccepted = amountAccepted;
	}

	private boolean isValidAmountAccepted(Double amountAccepted) {
		return amountAccepted!=null;
	}
	
	
	
	public UserProfile getMadeBy() {
		return madeBy;
	}

	public void setMadeBy(UserProfile madeBy) {
		this.madeBy = madeBy;
	}
	
	public static boolean isValidMadeBy(UserProfile madeBy) {
		return madeBy!=null;
	}

	public UserProfile getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(UserProfile processedBy) {
		this.processedBy = processedBy;
	}

	public static boolean isValidProcessedBy(UserProfile processedBy) {
		return true;
	}
	
	
	public UserProfile getProcessingBy() {
		return processingBy;
	}

	public void setProcessingBy(UserProfile processingBy) {
		this.processingBy = processingBy;
	}

	public static boolean isValidProcessingBy(UserProfile processingBy) {
		return true;
	}
	
	
	
	public WorkTask getWorkTask() {
		return workTask;
	}

	public void setWorkTask(WorkTask workTask) {
		this.workTask = workTask;
	}

	public static boolean isValidWorkTask(WorkTask workTask) {
		return true;
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
		if (!(object instanceof ExpenseReport)) {
			return false;
		}
		ExpenseReport other = (ExpenseReport) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.palex.attendanceManagement.data.entities.ExpenseReport[ id=" + id + " ]";
	}

	

}
