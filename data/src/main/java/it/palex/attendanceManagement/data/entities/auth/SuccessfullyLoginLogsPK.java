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
import it.palex.attendanceManagement.library.utils.InetAddressValidator;

/**
 *
 * @author palex
 */
@Embeddable
public class SuccessfullyLoginLogsPK implements Serializable, DatabaseCheckableEntity {
	
	private static final long serialVersionUID = -3358982217914660607L;
	
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "fk_id_users_auth_details")
    private int fkIdUsersAuthDetails;
	
	public static final int IP_MIN_SIZE_V4 = 7;//5.5.5.5
    public static final int IP_MAX_SIZE_V6 = 39;//IPV6
    
    @Basic(optional = false)
    @NotNull
    @Size(min = IP_MIN_SIZE_V4, max = IP_MAX_SIZE_V6)
    @Column(name = "ip")
    private String ip;
    
    public static final int USER_AGENT_MIN_SIZE = 0;
    public static final int USER_AGENT_MAX_SIZE = 512;
    @Basic(optional = false)
    @NotNull
    @Size(min = USER_AGENT_MIN_SIZE, max = USER_AGENT_MAX_SIZE)
    @Column(name = "user_agent")
    private String userAgent;
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "login_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date loginDate;

    public SuccessfullyLoginLogsPK() {
    }

    public SuccessfullyLoginLogsPK(int fkIdUsersAuthDetails, String ip, String userAgent, Date loginDate) {
        this.fkIdUsersAuthDetails = fkIdUsersAuthDetails;
        this.ip = ip;
        this.userAgent = userAgent;
        this.loginDate = loginDate;
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = this.isValidFkIdUsersAuthDetails(fkIdUsersAuthDetails) && this.isValidIp(ip) 
				&& this.isValidLoginDate(loginDate) && this.isValidUserAgent(userAgent);
		return isValid;
	}
    
    public int getFkIdUsersAuthDetails() {
        return fkIdUsersAuthDetails;
    }

    public void setFkIdUsersAuthDetails(int fkIdUsersAuthDetails) {
        this.fkIdUsersAuthDetails = fkIdUsersAuthDetails;
    }
    
    private boolean isValidFkIdUsersAuthDetails(int fkIdUsersAuthDetails){
    	if(fkIdUsersAuthDetails<0){
    		return false;
    	}
    	return true;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    private boolean isValidIp(String ip){
    	if(ip==null){
    		return false;
    	}
    	return InetAddressValidator.getInstance().isValid(ip);
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    private boolean isValidUserAgent(String userAgent) {
		if(userAgent==null){
			return false;
		}
		if(userAgent.length() < USER_AGENT_MIN_SIZE || userAgent.length() > USER_AGENT_MAX_SIZE){
			return false;
		}
		return true;
	}

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
    
    private boolean isValidLoginDate(Date loginDate){
    	if(loginDate==null){
    		return false;
    	}
    	return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) fkIdUsersAuthDetails;
        hash += (ip != null ? ip.hashCode() : 0);
        hash += (userAgent != null ? userAgent.hashCode() : 0);
        hash += (loginDate != null ? loginDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SuccessfullyLoginLogsPK)) {
            return false;
        }
        SuccessfullyLoginLogsPK other = (SuccessfullyLoginLogsPK) object;
        if (this.fkIdUsersAuthDetails != other.fkIdUsersAuthDetails) {
            return false;
        }
        if ((this.ip == null && other.ip != null) || (this.ip != null && !this.ip.equals(other.ip))) {
            return false;
        }
        if ((this.userAgent == null && other.userAgent != null) || (this.userAgent != null && !this.userAgent.equals(other.userAgent))) {
            return false;
        }
        if ((this.loginDate == null && other.loginDate != null) || (this.loginDate != null && !this.loginDate.equals(other.loginDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.SuccessfullyLoginLogsPK[ fkIdUsersAuthDetails=" + fkIdUsersAuthDetails + ", ip=" + ip + ", userAgent=" + userAgent + ", loginDate=" + loginDate + " ]";
    }
    
}
