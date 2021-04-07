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
public class ResetPasswordRequestPK implements Serializable, DatabaseCheckableEntity {
	
	private static final long serialVersionUID = 6566578130691868764L;

	public static final int IP_MIN_SIZE_V4 = 7;//5.5.5.5
    public static final int IP_MAX_SIZE_V6 = 39;//IPV6
    
	@Basic(optional = false)
    @NotNull
    @Size(min = IP_MIN_SIZE_V4, max = IP_MAX_SIZE_V6)
    @Column(name = "ip")
    private String ip;
    
	public static final int USER_AGENT_MIN_SIZE = 1;
    public static final int USER_AGENT_MAX_SIZE = 512;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = USER_AGENT_MIN_SIZE, max = USER_AGENT_MAX_SIZE)
    @Column(name = "user_agent")
    private String userAgent;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    public static final int MIN_USERNAME_SIZE = UsersAuthDetails.MIN_USERNAME_SIZE;
    public static final int MAX_USERNAME_SIZE = UsersAuthDetails.MAX_USERNAME_SIZE;
    public static final String DEFAULT_USERNAME_FOR_USERNAME_NOT_FOUND="0000000000000000000000000000000000000000000000000000000000000000";
    
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_USERNAME_SIZE, max = MAX_USERNAME_SIZE)
    @Column(name = "username")
    private String username;

    public ResetPasswordRequestPK() {
    }

    public ResetPasswordRequestPK(String ip, String userAgent, Date creationDate, String username) {
        this.ip = ip;
        this.userAgent = userAgent;
        this.creationDate = creationDate;
        this.username = username;
    }

    @Override
   	public boolean canBeInsertedInDatabase() {
   		boolean isValid = this.isValidIp(this.ip) && this.isValidCreationDate(this.creationDate)
   				&& this.isValidUsername(this.username) 
   				&& this.isValidUserAgent(this.userAgent);
   		
   		return isValid;
   	}
    
    @Override
  	public String whyCannotBeInsertedInDatabase() {
  		final String why = "ip:"+this.ip+", creationDate:"+ this.creationDate
  				+", username:"+ this.username+", userAgent:"+ this.userAgent;
  		
  		return why;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    private boolean isValidCreationDate(Date creationDate) {
		if(creationDate==null){
			return false;
		}
		return true;
	}
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private boolean isValidUsername(String username) {
		if(username==null){
			return false;
		}
		if(username.length() < MIN_USERNAME_SIZE || username.length() > MAX_USERNAME_SIZE){
			return false;
		}
		return true;
	}
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ip != null ? ip.hashCode() : 0);
        hash += (userAgent != null ? userAgent.hashCode() : 0);
        hash += (creationDate != null ? creationDate.hashCode() : 0);
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResetPasswordRequestPK)) {
            return false;
        }
        ResetPasswordRequestPK other = (ResetPasswordRequestPK) object;
        if ((this.ip == null && other.ip != null) || (this.ip != null && !this.ip.equals(other.ip))) {
            return false;
        }
        if ((this.userAgent == null && other.userAgent != null) || (this.userAgent != null && !this.userAgent.equals(other.userAgent))) {
            return false;
        }
        if ((this.creationDate == null && other.creationDate != null) || (this.creationDate != null && !this.creationDate.equals(other.creationDate))) {
            return false;
        }
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "ResetPasswordRequestPK [ip=" + ip + ", userAgent=" + userAgent + ", creationDate=" + creationDate
				+ ", username=" + username + "]";
	}
    
}
