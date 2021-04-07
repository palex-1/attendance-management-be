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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 16 giu 2020
 */
@Entity
@Table(name = "personal_document")
public class PersonalDocument extends AuditableEntity implements DatabaseCheckableEntity {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
        
    @JoinColumn(name = "personal_document_type", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PersonalDocumentType personalDocumentType;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "upload_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "editable")
    private Boolean editable = true;
    
    @JoinColumn(name = "document", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Document document;
    
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;

    
    public PersonalDocument() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidDocumentType(this.personalDocumentType) && isValidUploadDate(this.uploadDate)
				&& isValidDocument(this.document) && isValidUserProfile(this.userProfile) 
				&& isValidEditable(this.editable);
		
		return isValid;
	}
    
	public String whyCannotBeInsertedInDatabase() {
		String why = "personalDocumentType:"+this.personalDocumentType+", uploadDate:"+this.uploadDate
					 +", document:"+this.document+", userProfile:"+this.userProfile+", editable:"+this.editable;
		
		return why;
	}
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public PersonalDocumentType getPersonalDocumentType() {
		return personalDocumentType;
	}

	public void setPersonalDocumentType(PersonalDocumentType personalDocumentType) {
		this.personalDocumentType = personalDocumentType;
	}

	public static boolean isValidDocumentType(PersonalDocumentType documentType) {
		return documentType!=null;
	}
    
    
    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    private boolean isValidUploadDate(Date uploadDate) {
		return uploadDate!=null;
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

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public static boolean isValidEditable(Boolean editable) {
		return editable!=null;
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
        if (!(object instanceof PersonalDocument)) {
            return false;
        }
        PersonalDocument other = (PersonalDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.PersonalDocument[ id=" + id + " ]";
    }

}
