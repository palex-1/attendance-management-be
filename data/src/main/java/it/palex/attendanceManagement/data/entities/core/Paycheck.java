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

import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 *         16 giu 2020
 */
@Entity
@Table(name = "paycheck")
public class Paycheck extends AuditableEntity implements DatabaseCheckableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	@Column(name = "send_email_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendEmailDate;

	@Basic(optional = false)
	@NotNull
	@Column(name = "year")
	private Integer year;

	public static final int TITLE_MAX_SIZE = 200;
		
	@Basic(optional = true)
	@Size(max = TITLE_MAX_SIZE)
	@Column(name = "title")
	private String title;

	@Basic(optional = false)
	@NotNull
	@Column(name = "month")
	private Integer month;

	@JoinColumn(name = "document", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private Document document;

	@JoinColumn(name = "user_profile", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private UserProfile userProfile;
	
	

	public Paycheck() {
	}

	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidDocument(this.document) && isValidUserProfile(this.userProfile)
				&& isValidMonth(this.month) && isValidYear(this.year) && isValidTitle(this.title)
				&& isValidSendEmailDate(this.sendEmailDate);
		
		return isValid;
	}
	

	public String whyCannotBeInsertedInDatabase() {
		String why = "document:"+this.document+", userProfile:"+this.userProfile
				+", month:"+this.month+", year:"+this.year+", title:"+this.title
				+", sendEmailDate:"+this.sendEmailDate;
		
		return why;
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSendEmailDate() {
		return sendEmailDate;
	}

	public void setSendEmailDate(Date sendEmailDate) {
		this.sendEmailDate = sendEmailDate;
	}

	public static boolean isValidSendEmailDate(Date sendEmailDate) {
		return true;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public static boolean isValidTitle(String title) {
		if(title==null) {
			return true;
		}
		if(title.length()>TITLE_MAX_SIZE) {
			return false;
		}
		return true;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public static boolean isValidDocument(Document document) {
		return document!=null;
	}
	
	
	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	
	public static boolean isValidUserProfile(UserProfile userProfile) {
		return userProfile!=null;
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
		if (!(object instanceof Paycheck)) {
			return false;
		}
		Paycheck other = (Paycheck) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.palex.attendanceManagement.data.entities.Paycheck[ id=" + id + " ]";
	}	

}
