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
import it.palex.attendanceManagement.data.entities.enumTypes.ReportStatusEnum;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 3 lug 2020
 * 
 */
@Entity
@Table(name = "report_user_task")
public class ReportUserTask extends AuditableEntity implements DatabaseCheckableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	public static final int MIN_STATUS_SIZE = 1;
	public static final int MAX_STATUS_SIZE = 30;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = MIN_STATUS_SIZE, max = MAX_STATUS_SIZE)
	@Column(name = "status")
	private String status;

	public static final int MAX_LOGS_SIZE = 2147483647;
	
	@Size(max = MAX_LOGS_SIZE)
	@Column(name = "logs")
	private String logs;

	@Basic(optional = false)
	@NotNull
	@Column(name = "month")
	private Integer month;

	@Basic(optional = false)
	@NotNull
	@Column(name = "year")
	private Integer year;

	@Basic(optional = false)
	@NotNull
	@Column(name = "deleted")
	private Boolean deleted;

	@JoinColumn(name = "report", referencedColumnName = "id")
	@ManyToOne(optional = true)
	private Document report;

	
	public ReportUserTask() {
	}

	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidStatus(this.status) && isValidLogs(this.logs)
				&& isValidMonth(this.month) && isValidYear(this.year)
				&& isValidDeleted(this.deleted) && isValidReport(this.report);
		
		return isValid;
	}
	
	public String whyCannotBeInsertedInDatabase() {
		String why = "status:"+this.status+", logs:"+this.logs
					+ ", month:"+this.month+", year:"+this.year
					+ ", deleted:"+this.deleted+", report:"+this.report;
		
		return why;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		if(status.length()<MIN_STATUS_SIZE || status.length()> MAX_STATUS_SIZE) {
			return false;
		}
		return ReportStatusEnum.isValid(status);
	}
	
	

	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}
	public static boolean isValidLogs(String logs) {
		if(logs==null) {
			return true;
		}
		if(logs.length()> MAX_LOGS_SIZE) {
			return false;
		}
		return true;
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
	
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public static boolean isValidYear(Integer year) {
		return year!=null;
	}
	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public static boolean isValidDeleted(Boolean deleted) {
		return deleted!=null;
	}
	
	public Document getReport() {
		return report;
	}

	public void setReport(Document report) {
		this.report = report;
	}

	public static boolean isValidReport(Document report) {
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
		if (!(object instanceof ReportUserTask)) {
			return false;
		}
		ReportUserTask other = (ReportUserTask) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.palex.attendanceManagement.data.entities.ReportUserTask[ id=" + id + " ]";
	}


}
