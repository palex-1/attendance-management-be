package it.palex.attendanceManagement.data.entities.auth;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "users_access_token")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsersAccessToken.findAll", query = "SELECT u FROM UsersAccessToken u"),
    @NamedQuery(name = "UsersAccessToken.findByToken", query = "SELECT u FROM UsersAccessToken u WHERE u.token = :token"),
    @NamedQuery(name = "UsersAccessToken.findByRefreshToken", query = "SELECT u FROM UsersAccessToken u WHERE u.refreshToken = :refreshToken"),
    @NamedQuery(name = "UsersAccessToken.findByDeviceIdentifier", query = "SELECT u FROM UsersAccessToken u WHERE u.deviceIdentifier = :deviceIdentifier"),
    @NamedQuery(name = "UsersAccessToken.findByExpirationDate", query = "SELECT u FROM UsersAccessToken u WHERE u.expirationDate = :expirationDate"),
   
    @NamedQuery(name = "UsersAccessToken.deleteAllTokenOfUserId", 
	query = "DELETE FROM UsersAccessToken uac WHERE uac.fkIdUsersAuthDetails.id = :id"),
    
    @NamedQuery(name = "UsersAccessToken.deleteAllTokenExpiredBefore", 
   	query = "DELETE FROM UsersAccessToken uac WHERE uac.expirationDate <= :date")

    
    
})


public class UsersAccessToken implements Serializable, DatabaseCheckableEntity {
	
	private static final long serialVersionUID = -3914218861137198390L;

	public static final int MIN_TOKEN_SIZE = 16;
	public static final int MAX_TOKEN_SIZE = 4096;
	@Id
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_TOKEN_SIZE, max = MAX_TOKEN_SIZE)
    @Column(name = "token")
    private String token;
    
	public static final int MIN_REFRESH_TOKEN_SIZE = 32;
	public static final int MAX_REFRESH_TOKEN_SIZE = 128;
	
    @Basic(optional = true)
    @Size(max = MAX_REFRESH_TOKEN_SIZE)
    @Column(name = "refresh_token")
    private String refreshToken;
    
    public static final int MIN_DEVICE_IDENTIFIER_SIZE = 1;
    public static final int MAX_DEVICE_IDENTIFIER_SIZE = 64;
    
    @Size(min = MIN_DEVICE_IDENTIFIER_SIZE, max = MAX_DEVICE_IDENTIFIER_SIZE)
    @Column(name = "device_identifier")
    private String deviceIdentifier;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "issued_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issuedDate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "TWO_FACTOR_AUTHENTICATION_IN_PROGRESS")
    private Boolean twoFaInProgress;
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "must_reset_password")
    private Boolean mustResetPassword;
    
    
    public static final int MIN_ONE_TIME_PASSWORD_SIZE = 6;
    public static final int MAX_ONE_TIME_PASSWORD_SIZE = 16;
    
    //used for 2FA
    @Size(max = MAX_ONE_TIME_PASSWORD_SIZE)
    @Column(name = "ONE_TIME_PASSWORD")
    private String oneTimePassword;
    
    public static final int INITIAL_OTP_NUMBER_REQ = 0;
    
    //used for 2FA
    @Column(name = "ONE_TIME_PASSWORD_REQ_NUMBER")
    private Integer oneTimePasswordReqNumber = INITIAL_OTP_NUMBER_REQ;
    
    //used for 2FA
    @Column(name = "ONE_TIME_PASSWORD_EXPIRES")
    @Temporal(TemporalType.DATE)
    private Date oneTimePasswordExpiration;
    
	@JoinColumn(name = "fk_id_users_auth_details", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UsersAuthDetails fkIdUsersAuthDetails;
    

    public UsersAccessToken() {
    }

    
	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidToken(this.token) && isValidRefreshToken(this.refreshToken)
				&& isValidDeviceIdentifier(this.deviceIdentifier)
				&& isValidIssueDate(this.issuedDate) && isValidExpirationDate(this.expirationDate)
				&& isValidFkIdUsersAuthDetails(this.fkIdUsersAuthDetails)
				&& isValidTwoFaInProgress(this.twoFaInProgress) 
				&& isValidOneTimePassword(this.oneTimePassword)
				&& isValidOneTimePasswordExpiration(this.oneTimePasswordExpiration)
				&& isValidMustResetPassword(this.mustResetPassword)
				&& isValidOneTimePasswordReqNumber(this.oneTimePasswordReqNumber);
		
		return isValid;
	}


	@Override
	public String whyCannotBeInsertedInDatabase() {
		final String why = "token:"+this.token+", refreshToken:"+ this.refreshToken+", deviceIdentifier:"+ this.deviceIdentifier
				+", issuedDate:"+ this.issuedDate+", expirationDate:"+ this.expirationDate+", fkIdUsersAuthDetails:"+ this.fkIdUsersAuthDetails
				+", twoFaInProgress:"+ this.twoFaInProgress+", oneTimePassword:"+ this.oneTimePassword
				+", oneTimePasswordExpiration:"+ this.oneTimePasswordExpiration
				+", mustResetPassword:"+this.mustResetPassword
				+", oneTimePasswordReqNumber:"+this.oneTimePasswordReqNumber;
		
		return why;
	}


	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public static boolean isValidToken(String token) {
    	if(token==null) {
    		return false;
    	}
    	if( token.length() < MIN_TOKEN_SIZE || token.length() > MAX_TOKEN_SIZE) {
    		return false;
    	}
    	return true;
    }

    
    public Integer getOneTimePasswordReqNumber() {
		return oneTimePasswordReqNumber;
	}

	public void setOneTimePasswordReqNumber(Integer oneTimePasswordReqNumber) {
		this.oneTimePasswordReqNumber = oneTimePasswordReqNumber;
	}

	public static boolean isValidOneTimePasswordReqNumber(Integer oneTimePasswordReqNumber) {
		return oneTimePasswordReqNumber!=null;
	}

    
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public static boolean isValidRefreshToken(String refreshToken) {
    	if(refreshToken==null) {
    		return true; //can be null
    	}
    	if( refreshToken.length() < MIN_REFRESH_TOKEN_SIZE || refreshToken.length() > MAX_REFRESH_TOKEN_SIZE) {
    		return false;
    	}
    	return true;
    }

    public Date getOneTimePasswordExpiration() {
		return oneTimePasswordExpiration;
	}


	public void setOneTimePasswordExpiration(Date oneTimePasswordExpiration) {
		this.oneTimePasswordExpiration = oneTimePasswordExpiration;
	}


	public Boolean getMustResetPassword() {
		return mustResetPassword;
	}
	
	public void setMustResetPassword(Boolean mustResetPassword) {
		this.mustResetPassword = mustResetPassword;
	}

	private boolean isValidMustResetPassword(Boolean mustResetPassword) {
		return mustResetPassword!=null;
	}
	
	public Boolean getTwoFaInProgress() {
		return twoFaInProgress;
	}


	public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }
    
    public static boolean isValidDeviceIdentifier(String deviceIdentifier) {
    	if(deviceIdentifier==null) {
    		return true;
    	}
    	if( deviceIdentifier.length() < MIN_DEVICE_IDENTIFIER_SIZE || deviceIdentifier.length() > MAX_DEVICE_IDENTIFIER_SIZE) {
    		return false;
    	}
    	return true;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }
    
    public static boolean isValidIssueDate(Date issuedDate) {
    	return issuedDate!=null;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
    
	public static boolean isValidExpirationDate(Date expirationDate) {
    	return expirationDate!=null;
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

    public Boolean isTwoFaInProgress() {
		return twoFaInProgress;
	}
    
	public void setTwoFaInProgress(Boolean twoFaInProgress) {
		this.twoFaInProgress = twoFaInProgress;
	}
	
	public static boolean isValidTwoFaInProgress(Boolean twoFaInProgress) {
		return twoFaInProgress!=null;
	}
	
	 private static boolean isValidOneTimePasswordExpiration(Date oneTimePasswordExpiration) {
			return true;
	}
		
    
	public String getOneTimePassword() {
		return oneTimePassword;
	}
	
	public void setOneTimePassword(String oneTimePassword) {
		this.oneTimePassword = oneTimePassword;
	}
	
	public static boolean isValidOneTimePassword(String oneTimePassword) {
    	if(oneTimePassword==null) {
    		return true;
    	}
    	if( oneTimePassword.length() < MIN_ONE_TIME_PASSWORD_SIZE || oneTimePassword.length() > MAX_ONE_TIME_PASSWORD_SIZE) {
    		return false;
    	}
    	return true;
    }
	
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (token != null ? token.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersAccessToken)) {
            return false;
        }
        UsersAccessToken other = (UsersAccessToken) object;
        if ((this.token == null && other.token != null) || (this.token != null && !this.token.equals(other.token))) {
            return false;
        }
        return true;
    }

     
}
