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
@Table(name = "successfully_login_logs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SuccessfullyLoginLogs.findAll", query = "SELECT s FROM SuccessfullyLoginLogs s"),
    @NamedQuery(name = "SuccessfullyLoginLogs.findByFkIdUsersAuthDetails", query = "SELECT s FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.fkIdUsersAuthDetails = :fkIdUsersAuthDetails"),
    @NamedQuery(name = "SuccessfullyLoginLogs.findByIp", query = "SELECT s FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.ip = :ip"),
    @NamedQuery(name = "SuccessfullyLoginLogs.findByUserAgent", query = "SELECT s FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.userAgent = :userAgent"),
    @NamedQuery(name = "SuccessfullyLoginLogs.findByLoginDate", query = "SELECT s FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.loginDate = :loginDate")
    
    , @NamedQuery(name = "SuccessfullyLoginLogs.findByKey", 
	query = "SELECT s FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.ip = :ip AND "
			+ "s.successfullyLoginLogsPK.fkIdUsersAuthDetails = :fkIdUsersAuthDetails AND "
			+ "s.successfullyLoginLogsPK.userAgent = :userAgent AND s.successfullyLoginLogsPK.loginDate = :loginDate")
	
	, @NamedQuery(name = "SuccessfullyLoginLogs.countByFkIdUsersAuthDetails", 
					query = "SELECT COUNT(s) FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.fkIdUsersAuthDetails = :fkIdUsersAuthDetails")
	
	, @NamedQuery(name = "SuccessfullyLoginLogs.findSuccessfullyLoginLogsOfUserAuthDetailsInTimeRange", 
			query = "SELECT s FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.fkIdUsersAuthDetails = :fkIdUsersAuthDetails AND "
					+ "s.successfullyLoginLogsPK.loginDate <= :from AND s.successfullyLoginLogsPK.loginDate >= :to ORDER BY s.successfullyLoginLogsPK.loginDate DESC")
	
	, @NamedQuery(name = "SuccessfullyLoginLogs.countSuccessfullyLoginLogsOfUserAuthDetailsInTimeRange", 
			query = "SELECT COUNT(s) FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.fkIdUsersAuthDetails = :fkIdUsersAuthDetails AND "
					+ "s.successfullyLoginLogsPK.loginDate <= :from AND s.successfullyLoginLogsPK.loginDate >= :to")
	
	, @NamedQuery(name = "SuccessfullyLoginLogs.countAllSuccessfullyLoginLogs", 
			query = "SELECT COUNT(s) FROM SuccessfullyLoginLogs s")
	
	, @NamedQuery(name = "SuccessfullyLoginLogs.findAllSuccessfullyLoginLogsOfUser", 
					query = "SELECT s FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.fkIdUsersAuthDetails = :fkIdUsersAuthDetails ORDER BY s.successfullyLoginLogsPK.loginDate DESC")
	
	, @NamedQuery(name = "SuccessfullyLoginLogs.deleteLoginHistoryOfAllAccountBeforeDate", 
			query = "DELETE FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.loginDate <= :beforeDate")
	
	, @NamedQuery(name = "SuccessfullyLoginLogs.deleteLoginHistoryOfUserAuthDetails", 
			query = "DELETE FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.fkIdUsersAuthDetails = :fkIdUsersAuthDetails")
	
	, @NamedQuery(name = "SuccessfullyLoginLogs.deleteLoginHistoryOfUserAuthDetailsBeforeDate", 
			query = "DELETE FROM SuccessfullyLoginLogs s WHERE s.successfullyLoginLogsPK.fkIdUsersAuthDetails = :fkIdUsersAuthDetails AND "
					+ "s.successfullyLoginLogsPK.loginDate <= :beforeDate")
})
public class SuccessfullyLoginLogs implements Serializable, DatabaseCheckableEntity {

	private static final long serialVersionUID = 87561512232667241L;
	
	@EmbeddedId
    protected SuccessfullyLoginLogsPK successfullyLoginLogsPK;
	
    @JoinColumn(name = "fk_id_users_auth_details", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UsersAuthDetails usersAuthDetails;

    public SuccessfullyLoginLogs() {
    }

    public SuccessfullyLoginLogs(SuccessfullyLoginLogsPK successfullyLoginLogsPK) {
        this.successfullyLoginLogsPK = successfullyLoginLogsPK;
    }

    public SuccessfullyLoginLogs(int fkIdUsersAuthDetails, String ip, String userAgent, Date loginDate) {
        this.successfullyLoginLogsPK = new SuccessfullyLoginLogsPK(fkIdUsersAuthDetails, ip, userAgent, loginDate);
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		if(this.successfullyLoginLogsPK==null){
			return false;
		}
		return this.successfullyLoginLogsPK.canBeInsertedInDatabase();
	}
    
    
    public SuccessfullyLoginLogsPK getSuccessfullyLoginLogsPK() {
        return successfullyLoginLogsPK;
    }

    public void setSuccessfullyLoginLogsPK(SuccessfullyLoginLogsPK successfullyLoginLogsPK) {
        this.successfullyLoginLogsPK = successfullyLoginLogsPK;
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
        hash += (successfullyLoginLogsPK != null ? successfullyLoginLogsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SuccessfullyLoginLogs)) {
            return false;
        }
        SuccessfullyLoginLogs other = (SuccessfullyLoginLogs) object;
        if ((this.successfullyLoginLogsPK == null && other.successfullyLoginLogsPK != null) || (this.successfullyLoginLogsPK != null && !this.successfullyLoginLogsPK.equals(other.successfullyLoginLogsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.SuccessfullyLoginLogs[ successfullyLoginLogsPK=" + successfullyLoginLogsPK + " ]";
    }

	
    
}
