package it.palex.attendanceManagement.data.entities;

import java.io.Serializable;
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

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.enumTypes.ContactTypeEnum;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "user_contacts")
public class UserContacts implements Serializable, DatabaseCheckableEntity {
	
	private static final long serialVersionUID = 3143157807208082493L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
	
	public static final int MIN_CVALUE_SIZE = 1;
	public static final int MAX_CVALUE_SIZE = 255;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_CVALUE_SIZE, max = MAX_CVALUE_SIZE)
    @Column(name = "c_value")
    private String cValue;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "verified")
    private Boolean verified;
    
    public static final int MIN_VERIFICATION_TOKEN_SIZE = 6; //otp case
	public static final int MAX_VERIFICATION_TOKEN_SIZE = 1024;
	
	//do not set unique here, problems with one time password
	@Size(max = MAX_VERIFICATION_TOKEN_SIZE)
    @Column(name = "verification_token")
    private String verificationToken;
    
    @Column(name = "verification_token_creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verificationTokenCreationDate;
    
    @Column(name = "verification_token_expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verificationTokenExpirationDate;
    
    public static final int MIN_C_TYPE_SIZE = 1;
	public static final int MAX_C_TYPE_SIZE = 30;
	
    @NotNull
    @Size(min = MIN_C_TYPE_SIZE, max = MAX_C_TYPE_SIZE)
    @Column(name = "user_contact_type")
    private String userContactType;
    
    @JoinColumn(name = "fk_id_users_auth_details", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UsersAuthDetails fkIdUsersAuthDetails;

    public UserContacts() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidCValue(this.cValue) && isValidVerified(this.verified)
				&& isValidUserContactType(this.userContactType)
				&& isValidFkIdUsersAuthDetails(this.fkIdUsersAuthDetails)
				&& isValidVerificationToken(this.verificationToken)
				&& isValidVerificationTokenCreationDate(this.verificationTokenCreationDate)
				&& isValidVerificationTokenExpirationDate(this.verificationTokenExpirationDate);
		
		return isValid;
	}
    
    @Override
 	public String whyCannotBeInsertedInDatabase() {
 		final String why = "cValue:"+this.cValue+", verified:"+ this.verified+", userContactType:"+ this.userContactType
 				+", fkIdUsersAuthDetails:"+ this.fkIdUsersAuthDetails+", verificationToken:"+ this.verificationToken
 				+", verificationTokenCreationDate:"+ this.verificationTokenCreationDate+", verificationTokenExpirationDate:"+ this.verificationTokenExpirationDate;
 		
 		return why;
 	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCValue() {
        return cValue;
    }

    public void setCValue(String cValue) {
        this.cValue = cValue;
    }
    
    public static boolean isValidCValue(String cValue) {
    	if(cValue==null) {
    		return false;
    	}
    	if( cValue.length() < MIN_CVALUE_SIZE ||  cValue.length() > MAX_CVALUE_SIZE){
			return false;
		}
    	return true;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
    
    public static boolean isValidVerified(Boolean verified) {
    	return verified!=null;
    }
    
    public String getUserContactType() {
        return userContactType;
    }

    public void setUserContactType(String userContactType) {
        this.userContactType = userContactType;
    }
    
    public static boolean isValidUserContactType(String userContactType) {
    	return userContactType!=null && ContactTypeEnum.isValid(userContactType);
    }

    public UsersAuthDetails getFkIdUsersAuthDetails() {
        return fkIdUsersAuthDetails;
    }

    public void setFkIdUsersAuthDetails(UsersAuthDetails fkIdUsersAuthDetails) {
        this.fkIdUsersAuthDetails = fkIdUsersAuthDetails;
    }
    
    public static boolean isValidFkIdUsersAuthDetails(UsersAuthDetails fkIdUsersAuthDetails) {
    	return fkIdUsersAuthDetails!=null;
    }

    public String getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}
	
	public static boolean isValidVerificationToken(String verificationToken) {
		if(verificationToken==null) {
			return true;
		}
		if( verificationToken.length() < MIN_VERIFICATION_TOKEN_SIZE ||  
				verificationToken.length() > MAX_VERIFICATION_TOKEN_SIZE){
			return false;
		}
		
		return true;
	}

	public Date getVerificationTokenCreationDate() {
		return verificationTokenCreationDate;
	}

	public void setVerificationTokenCreationDate(Date verificationTokenCreationDate) {
		this.verificationTokenCreationDate = verificationTokenCreationDate;
	}
	
	public static boolean isValidVerificationTokenCreationDate(Date verificationTokenCreationDate) {
		return true;
	}
	
	
	public Date getVerificationTokenExpirationDate() {
		return verificationTokenExpirationDate;
	}

	public void setVerificationTokenExpirationDate(Date verificationTokenExpirationDate) {
		this.verificationTokenExpirationDate = verificationTokenExpirationDate;
	}
	
	public static boolean isValidVerificationTokenExpirationDate(Date verificationTokenExpirationDate) {
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
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserContacts)) {
            return false;
        }
        UserContacts other = (UserContacts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "UserContacts [id=" + id + "]";
	}

}
