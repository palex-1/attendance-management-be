package it.palex.attendanceManagement.data.entities.auth;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author palex
 */
@Entity
@Table(name = "user_password_change_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserPasswordChangeHistory.findAll", query = "SELECT u FROM UserPasswordChangeHistory u"),
    @NamedQuery(name = "UserPasswordChangeHistory.findByFkIdUsersAuthDetails", query = "SELECT u FROM UserPasswordChangeHistory u WHERE u.userPasswordChangeHistoryPK.fkIdUsersAuthDetails = :fkIdUsersAuthDetails"),
    @NamedQuery(name = "UserPasswordChangeHistory.findByPasswordChangeDate", query = "SELECT u FROM UserPasswordChangeHistory u WHERE u.userPasswordChangeHistoryPK.passwordChangeDate = :passwordChangeDate"),
    @NamedQuery(name = "UserPasswordChangeHistory.findByHashedPassword", query = "SELECT u FROM UserPasswordChangeHistory u WHERE u.userPasswordChangeHistoryPK.hashedPassword = :hashedPassword"),
    
    @NamedQuery(name = "UserPasswordChangeHistory.deleteAllChangePasswordOfUserBeforeByUserId", 
    	query = "DELETE FROM UserPasswordChangeHistory u WHERE u.userPasswordChangeHistoryPK.fkIdUsersAuthDetails = :fkIdUsersAuthDetails AND u.userPasswordChangeHistoryPK.passwordChangeDate <= :beforeDate"),
    @NamedQuery(name = "UserPasswordChangeHistory.deleteAllChangePasswordBefore", 
    	query = "DELETE FROM UserPasswordChangeHistory u WHERE u.userPasswordChangeHistoryPK.passwordChangeDate <= :beforeDate")


})
public class UserPasswordChangeHistory implements Serializable, DatabaseCheckableEntity {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected UserPasswordChangeHistoryPK userPasswordChangeHistoryPK;
    
    @JoinColumn(name = "fk_id_users_auth_details", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UsersAuthDetails usersAuthDetails;

    public UserPasswordChangeHistory() {
    }

    public UserPasswordChangeHistory(UserPasswordChangeHistoryPK userPasswordChangeHistoryPK) {
        this.userPasswordChangeHistoryPK = userPasswordChangeHistoryPK;
    }

    public UserPasswordChangeHistory(int fkIdUsersAuthDetails, Date passwordChangeDate, String hashedPassword) {
        this.userPasswordChangeHistoryPK = new UserPasswordChangeHistoryPK(fkIdUsersAuthDetails, passwordChangeDate, hashedPassword);
    }

    public UserPasswordChangeHistoryPK getUserPasswordChangeHistoryPK() {
        return userPasswordChangeHistoryPK;
    }

	@Override
	public boolean canBeInsertedInDatabase() {
		if(this.userPasswordChangeHistoryPK==null || this.usersAuthDetails==null){
			return false;
		}
		return this.userPasswordChangeHistoryPK.canBeInsertedInDatabase();
	}
	
    public void setUserPasswordChangeHistoryPK(UserPasswordChangeHistoryPK userPasswordChangeHistoryPK) {
        this.userPasswordChangeHistoryPK = userPasswordChangeHistoryPK;
    }

    public UsersAuthDetails getUsersAuthDetails() {
        return usersAuthDetails;
    }

    public void setUsersAuthDetails(UsersAuthDetails usersAuthDetails) {
        this.usersAuthDetails = usersAuthDetails;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userPasswordChangeHistoryPK != null ? userPasswordChangeHistoryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserPasswordChangeHistory)) {
            return false;
        }
        UserPasswordChangeHistory other = (UserPasswordChangeHistory) object;
        if ((this.userPasswordChangeHistoryPK == null && other.userPasswordChangeHistoryPK != null) || (this.userPasswordChangeHistoryPK != null && !this.userPasswordChangeHistoryPK.equals(other.userPasswordChangeHistoryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.UserPasswordChangeHistory[ userPasswordChangeHistoryPK=" + userPasswordChangeHistoryPK + " ]";
    }

	
    
}
