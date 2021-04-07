package it.palex.attendanceManagement.data.entities.core;


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

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.enumTypes.UserAttendanceTypeEnum;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 26 giu 2020
 */
@Entity
@Table(name = "user_attendance")
public class UserAttendance implements Serializable, DatabaseCheckableEntity {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    public static final int TYPE_MAX_SIZE = 10;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = TYPE_MAX_SIZE)
    @Column(name = "type")
    private String type;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    
    @JoinColumn(name = "turnstile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Turnstile turnstile;
    
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;
    
    @NotNull
    @Column(name = "deleted")
    @Basic(optional = false)
    private Boolean deleted = false;
    
    

    public UserAttendance() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidType(this.type) && isValidTimestamp(this.timestamp) 
				&& isValidTurnstile(this.turnstile) && isValidUserProfile(this.userProfile)
				&& isValidDeleted(this.deleted);
		
		return isValid;
	}
    
	public String whyCannotBeInsertedInDatabase() {
		String why = "type:"+this.type+", timestamp:"+this.timestamp
					+", turnstile:"+this.turnstile+", userProfile:"+this.userProfile
					+", deleted:"+this.deleted;
		
		return why;
	}
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public static boolean isValidType(String type) {
		if(type==null) {
			return false;
		}
		if(type.length()>TYPE_MAX_SIZE) {
			return false;
		}
		return UserAttendanceTypeEnum.isValid(type);
	}
    
    
    public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public static boolean isValidDeleted(Boolean deleted) {
		return deleted!=null;
	}

	public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public static boolean isValidTimestamp(Date timestamp) {
		return timestamp!=null;
	}
    
    
    
    public Turnstile getTurnstile() {
        return turnstile;
    }

    public void setTurnstile(Turnstile turnstile) {
        this.turnstile = turnstile;
    }

    public static  boolean isValidTurnstile(Turnstile turnstile) {
    	return turnstile!=null;
	}
    
    
    
    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public static boolean isValidUserProfile(UserProfile userProfile) {
    	return userProfile!=null;
	}
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserAttendance)) {
            return false;
        }
        UserAttendance other = (UserAttendance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.UserAttendance[ id=" + id + " ]";
    }

	
    
}

