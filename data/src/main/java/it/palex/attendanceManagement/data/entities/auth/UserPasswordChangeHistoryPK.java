package it.palex.attendanceManagement.data.entities.auth;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author palex
 */
@Embeddable
public class UserPasswordChangeHistoryPK implements Serializable, DatabaseCheckableEntity {
	
	private static final long serialVersionUID = 7454403960262134029L;

	@Basic(optional = false)
    @NotNull
    @Column(name = "fk_id_users_auth_details")
    private Integer fkIdUsersAuthDetails;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "password_change_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordChangeDate;
    
    public static final int MIN_HASHED_PASSWORD_SIZE = 64;
    public static final int MAX_HASHED_PASSWORD_SIZE = 64;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_HASHED_PASSWORD_SIZE, max = MAX_HASHED_PASSWORD_SIZE)
    @Column(name = "hashed_password")
    private String hashedPassword;

    public UserPasswordChangeHistoryPK() {
    }

    public UserPasswordChangeHistoryPK(int fkIdUsersAuthDetails, Date passwordChangeDate, String hashedPassword) {
        this.fkIdUsersAuthDetails = fkIdUsersAuthDetails;
        this.passwordChangeDate = passwordChangeDate;
        this.hashedPassword = hashedPassword;
    }

    /* (non-Javadoc)
	 * @see it.palex.gestionePresenze.data.entities.DatabaseCheckableInsert#canBeInsertedInDatabase()
	 */
	@Override
	public boolean canBeInsertedInDatabase() {
		if(this.fkIdUsersAuthDetails==null || this.hashedPassword==null || this.passwordChangeDate==null){
			return false;
		}
		return this.isValidHashedPassword();
	}
	
    /**
	 * @return
	 */
	private boolean isValidHashedPassword() {
		if(hashedPassword==null){
			return false;
		}
	    if(hashedPassword.length() < MIN_HASHED_PASSWORD_SIZE || hashedPassword.length() > MAX_HASHED_PASSWORD_SIZE ){
	    	return false;
	    }
	    return true;
	}

	public Integer getFkIdUsersAuthDetails() {
        return fkIdUsersAuthDetails;
    }

    public void setFkIdUsersAuthDetails(Integer fkIdUsersAuthDetails) {
        this.fkIdUsersAuthDetails = fkIdUsersAuthDetails;
    }

    public Date getPasswordChangeDate() {
        return passwordChangeDate;
    }

    public void setPasswordChangeDate(Date passwordChangeDate) {
        this.passwordChangeDate = passwordChangeDate;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) fkIdUsersAuthDetails;
        hash += (passwordChangeDate != null ? passwordChangeDate.hashCode() : 0);
        hash += (hashedPassword != null ? hashedPassword.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserPasswordChangeHistoryPK)) {
            return false;
        }
        UserPasswordChangeHistoryPK other = (UserPasswordChangeHistoryPK) object;
        if (this.fkIdUsersAuthDetails != other.fkIdUsersAuthDetails) {
            return false;
        }
        if ((this.passwordChangeDate == null && other.passwordChangeDate != null) || (this.passwordChangeDate != null && !this.passwordChangeDate.equals(other.passwordChangeDate))) {
            return false;
        }
        if ((this.hashedPassword == null && other.hashedPassword != null) || (this.hashedPassword != null && !this.hashedPassword.equals(other.hashedPassword))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.UserPasswordChangeHistoryPK[ fkIdUsersAuthDetails=" + fkIdUsersAuthDetails + ", passwordChangeDate=" + passwordChangeDate + ", hashedPassword=" + hashedPassword + " ]";
    }

	
    
}
