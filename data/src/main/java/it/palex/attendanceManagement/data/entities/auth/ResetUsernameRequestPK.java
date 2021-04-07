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

/**
 *
 * @author palex
 */
@Embeddable
public class ResetUsernameRequestPK implements Serializable {
	
	private static final long serialVersionUID = 7286097657287425156L;

	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 39)
    @Column(name = "ip")
    private String ip;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "user_agent")
    private String userAgent;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "hashed_username")
    private String hashedUsername;

    public ResetUsernameRequestPK() {
    }

    public ResetUsernameRequestPK(String ip, String userAgent, Date creationDate, String hashedUsername) {
        this.ip = ip;
        this.userAgent = userAgent;
        this.creationDate = creationDate;
        this.hashedUsername = hashedUsername;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getHashedUsername() {
        return hashedUsername;
    }

    public void setHashedUsername(String hashedUsername) {
        this.hashedUsername = hashedUsername;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ip != null ? ip.hashCode() : 0);
        hash += (userAgent != null ? userAgent.hashCode() : 0);
        hash += (creationDate != null ? creationDate.hashCode() : 0);
        hash += (hashedUsername != null ? hashedUsername.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResetUsernameRequestPK)) {
            return false;
        }
        ResetUsernameRequestPK other = (ResetUsernameRequestPK) object;
        if ((this.ip == null && other.ip != null) || (this.ip != null && !this.ip.equals(other.ip))) {
            return false;
        }
        if ((this.userAgent == null && other.userAgent != null) || (this.userAgent != null && !this.userAgent.equals(other.userAgent))) {
            return false;
        }
        if ((this.creationDate == null && other.creationDate != null) || (this.creationDate != null && !this.creationDate.equals(other.creationDate))) {
            return false;
        }
        if ((this.hashedUsername == null && other.hashedUsername != null) || (this.hashedUsername != null && !this.hashedUsername.equals(other.hashedUsername))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.ResetUsernameRequestPK[ ip=" + ip + ", userAgent=" + userAgent + ", creationDate=" + creationDate + ", hashedUsername=" + hashedUsername + " ]";
    }
    
}
