package it.palex.attendanceManagement.data.entities.auth;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author palex
 */
@Entity
@Table(name = "failed_login_attempt")
@NamedQueries({
    @NamedQuery(name = "FailedLoginAttempt.findAll", query = "SELECT f FROM FailedLoginAttempt f"),
    @NamedQuery(name = "FailedLoginAttempt.findByUsername", query = "SELECT f FROM FailedLoginAttempt f WHERE f.failedLoginAttemptPK.username = :username"),
    @NamedQuery(name = "FailedLoginAttempt.findByIp", query = "SELECT f FROM FailedLoginAttempt f WHERE f.failedLoginAttemptPK.ip = :ip"),
    @NamedQuery(name = "FailedLoginAttempt.findByUserAgent", query = "SELECT f FROM FailedLoginAttempt f WHERE f.failedLoginAttemptPK.userAgent = :userAgent"),
    @NamedQuery(name = "FailedLoginAttempt.findByLoginDate", query = "SELECT f FROM FailedLoginAttempt f WHERE f.failedLoginAttemptPK.loginDate = :loginDate"),

    @NamedQuery(name = "FailedLoginAttempt.findByKey", query = "SELECT f FROM FailedLoginAttempt f WHERE f.failedLoginAttemptPK.loginDate = :loginDate AND "
			+ "f.failedLoginAttemptPK.userAgent = :userAgent AND f.failedLoginAttemptPK.ip = :ip AND "
			+ "f.failedLoginAttemptPK.username = :username")

	, @NamedQuery(name = "FailedLoginAttempt.findByIpUsernameAndDate", query = "SELECT f FROM FailedLoginAttempt f WHERE f.failedLoginAttemptPK.loginDate = :loginDate AND "
				+ "f.failedLoginAttemptPK.ip = :ip AND "
				+ "f.failedLoginAttemptPK.username = :username")
	
	, @NamedQuery(name = "FailedLoginAttempt.findAllLoginAttemptOfIpInTimeRange", 
			query = "SELECT f FROM FailedLoginAttempt f WHERE f.failedLoginAttemptPK.loginDate <= :to AND "
				+ "f.failedLoginAttemptPK.loginDate >= :from AND f.failedLoginAttemptPK.ip = :ip")
	
	, @NamedQuery(name = "FailedLoginAttempt.countAllLoginAttemptOfIpInTimeRange", 
			query = "SELECT COUNT(f) FROM FailedLoginAttempt f WHERE f.failedLoginAttemptPK.loginDate <= :to AND "
					+ "f.failedLoginAttemptPK.loginDate >= :from AND f.failedLoginAttemptPK.ip = :ip")
	
	, @NamedQuery(name = "FailedLoginAttempt.findAllLoginAttemptOfIpAndUsernameInTimeRange", 
			query = "SELECT COUNT(f) FROM FailedLoginAttempt f WHERE f.failedLoginAttemptPK.loginDate <= :to AND "
					+ "f.failedLoginAttemptPK.loginDate >= :from AND f.failedLoginAttemptPK.ip = :ip AND "
					+ "f.failedLoginAttemptPK.username = :username")
	
	, @NamedQuery(name = "FailedLoginAttempt.countAllLoginAttemptOfIpAndUsernameInTimeRange", 
			query = "SELECT COUNT(f) FROM FailedLoginAttempt f WHERE f.failedLoginAttemptPK.loginDate <= :to AND "
				+ "f.failedLoginAttemptPK.loginDate >= :from AND f.failedLoginAttemptPK.ip = :ip AND "
				+ "f.failedLoginAttemptPK.username = :username")
	
	, @NamedQuery(name = "FailedLoginAttempt.deleteAllFailedLoginAttemptBefore", 
			query = "DELETE FROM FailedLoginAttempt f WHERE f.failedLoginAttemptPK.loginDate <= :beforeDate")


})
public class FailedLoginAttempt implements Serializable, DatabaseCheckableEntity {
    private static final long serialVersionUID = 1L;
    
    
    @EmbeddedId
    protected FailedLoginAttemptPK failedLoginAttemptPK;

    public FailedLoginAttempt() {
    }

    public FailedLoginAttempt(FailedLoginAttemptPK failedLoginAttemptPK) {
        this.failedLoginAttemptPK = failedLoginAttemptPK;
    }

    public FailedLoginAttempt(String hashedUsername, String ip, String userAgent, Date loginDate) {
        this.failedLoginAttemptPK = new FailedLoginAttemptPK(hashedUsername, ip, userAgent, loginDate);
    }
    
    
    /* (non-Javadoc)
	 * @see it.palex.gestionePresenze.data.entities.DatabaseCheckableInsert#canBeInsertedInDatabase()
	 */
	@Override
	public boolean canBeInsertedInDatabase() {
		return this.isValidFailedLoginAttemptPK();
	}
    
    private boolean isValidFailedLoginAttemptPK(){
    	if(this.failedLoginAttemptPK==null){
    		return false;
    	}
    	return failedLoginAttemptPK.canBeInsertedInDatabase();
    }

    public FailedLoginAttemptPK getFailedLoginAttemptPK() {
        return failedLoginAttemptPK;
    }

    public void setFailedLoginAttemptPK(FailedLoginAttemptPK failedLoginAttemptPK) {
        this.failedLoginAttemptPK = failedLoginAttemptPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (failedLoginAttemptPK != null ? failedLoginAttemptPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FailedLoginAttempt)) {
            return false;
        }
        FailedLoginAttempt other = (FailedLoginAttempt) object;
        if ((this.failedLoginAttemptPK == null && other.failedLoginAttemptPK != null) || (this.failedLoginAttemptPK != null && !this.failedLoginAttemptPK.equals(other.failedLoginAttemptPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.FailedLoginAttempt[ failedLoginAttemptPK=" + failedLoginAttemptPK + " ]";
    }

	
    
}
