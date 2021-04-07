package it.palex.attendanceManagement.data.entities.core;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedProvidersEnum;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "fcm_user_token")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FcmUserToken.findAll", query = "SELECT f FROM FcmUserToken f"),
    @NamedQuery(name = "FcmUserToken.findById", query = "SELECT f FROM FcmUserToken f WHERE f.id = :id"),
    @NamedQuery(name = "FcmUserToken.findByToken", query = "SELECT f FROM FcmUserToken f WHERE f.token = :token"),
    @NamedQuery(name = "FcmUserToken.findByDeviceId", query = "SELECT f FROM FcmUserToken f WHERE f.deviceId = :deviceId")})
public class FcmUserToken extends AuditableEntity implements Serializable, DatabaseCheckableEntity {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    public static final int TOKEN_MIN_SIZE = 1;
    public static final int TOKEN_MAX_SIZE  = 4096;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = TOKEN_MIN_SIZE, max = TOKEN_MAX_SIZE)
    @Column(name = "token")
    private String token;
    
    public static final int DEVICE_ID_MIN_SIZE = 1;
    public static final int DEVICE_ID_MAX_SIZE = 4096;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = DEVICE_ID_MIN_SIZE, max = DEVICE_ID_MAX_SIZE)
    @Column(name = "device_id")
    private String deviceId;
    
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UsersAuthDetails userId;

    public static final String DEFAULT_PROVIDER_NAME = "FIREBASE";
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "provider_name")
    private String providerName;
    
    
    public FcmUserToken() {
    }

	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidToken(this.token) && isValidProviderName(this.providerName)
				 && isValidUserId(this.userId) && isValidDeviceId(this.deviceId);
		
		return isValid;
	}
	
    @Override
	public String whyCannotBeInsertedInDatabase() {
		final String why = "token:"+this.token+", providerName:"+ this.providerName+", userId:"+ this.userId
				+", deviceId:"+ this.deviceId;
		
		return why;
	}
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
		if(token.length()<TOKEN_MIN_SIZE || token.length()>TOKEN_MAX_SIZE) {
			return false;
		}
		return true;
	}

    public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public static boolean isValidProviderName(String providerName) {
		if(providerName==null) {
			return false;
		}
		return SupportedProvidersEnum.isValid(providerName);
	}
	
	public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public static boolean isValidDeviceId(String deviceId) {
		if(deviceId==null) {
			return false;
		}
		if(deviceId.length()<DEVICE_ID_MIN_SIZE || deviceId.length()>DEVICE_ID_MAX_SIZE) {
			return false;
		}
		return true;
	}

    public UsersAuthDetails getUserId() {
        return userId;
    }

    public void setUserId(UsersAuthDetails userId) {
        this.userId = userId;
    }

    public static boolean isValidUserId(UsersAuthDetails userId) {
		return userId!=null;
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
        if (!(object instanceof FcmUserToken)) {
            return false;
        }
        FcmUserToken other = (FcmUserToken) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
    
}


