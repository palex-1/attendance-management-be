package it.palex.attendanceManagement.data.entities.core;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.enumTypes.TaskCompletionLocksStatusEnum;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 10 giu 2020
 */
@Entity
@Table(name = "task_completions_locks")
public class TaskCompletionsLocks extends AuditableEntity implements DatabaseCheckableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;

	@Basic(optional = false)
	@NotNull
	@Column(name = "year")
	private Integer year;

	@Basic(optional = false)
	@NotNull
	@Column(name = "month")
	private Integer month;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "hours_calculation_execution_requested")
	private Boolean hoursCalculationExecutionRequested;
	
	public static final int STATUS_MAX_SIZE = 30;
	
	@Basic(optional = false)
	@NotNull
	@Size(max = STATUS_MAX_SIZE)
	@Column(name = "status")
	private String status;

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "processed_on_date")
    private Date processedOnDate;
	
	
	
	public TaskCompletionsLocks() {
	}

	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidYear(this.year) && isValidMonth(this.month) 
				&& isValidHoursCalculationExecutionRequested(this.hoursCalculationExecutionRequested)
				&& isValidStatus(this.status);
		
		return isValid;
	}
	
	

	public String whyCannotBeInsertedInDatabase() {
		String why = "year:"+this.year+", month:"+this.month
				+", hoursCalculationExecutionRequested:"+this.hoursCalculationExecutionRequested
				+", status:"+this.status;
		
		return why;
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	
	public static boolean isValidYear(Integer year) {
		return year!=null;
	}
	
	
	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}
	
	public static boolean isValidMonth(Integer month) {
		return month!=null;
	}
	
	public Boolean getHoursCalculationExecutionRequested() {
		return hoursCalculationExecutionRequested;
	}

	public void setHoursCalculationExecutionRequested(Boolean hoursCalculationExecutionRequested) {
		this.hoursCalculationExecutionRequested = hoursCalculationExecutionRequested;
	}
	
	public static boolean isValidHoursCalculationExecutionRequested(Boolean hoursCalculationExecutionRequested) {
		return hoursCalculationExecutionRequested!=null;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public static boolean isValidStatus(String status) {
		if(status==null) {
			return false;
		}
		if(status.length()>STATUS_MAX_SIZE) {
			return false;
		}
		return TaskCompletionLocksStatusEnum.isValid(status);
	}
	
	

	public Date getProcessedOnDate() {
		return processedOnDate;
	}

	public void setProcessedOnDate(Date processedOnDate) {
		this.processedOnDate = processedOnDate;
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
		if (!(object instanceof TaskCompletionsLocks)) {
			return false;
		}
		TaskCompletionsLocks other = (TaskCompletionsLocks) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.palex.attendanceManagement.data.entities.TaskCompletionsLocks[ id=" + id + " ]";
	}

}
