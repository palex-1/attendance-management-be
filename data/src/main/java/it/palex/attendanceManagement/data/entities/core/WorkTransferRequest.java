package it.palex.attendanceManagement.data.entities.core;


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
import it.palex.attendanceManagement.data.entities.enumTypes.WorkTransferTypeEnum;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 4 lug 2020
 */
@Entity
@Table(name = "work_transfer_request")
public class WorkTransferRequest extends AuditableEntity implements DatabaseCheckableEntity {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "day")
    @Temporal(TemporalType.DATE)
    private Date day;
    
    public static final int TYPE_MIN_SIZE = 1;
    public static final int TYPE_MAX_SIZE = 20;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = TYPE_MIN_SIZE, max = TYPE_MAX_SIZE)
    @Column(name = "type")
    private String type;
    
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;

    public WorkTransferRequest() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidDay(this.day) && isValidType(this.type) 
				&& isValidUserProfile(this.userProfile);
		
		return isValid;
	}
    
	public String whyCannotBeInsertedInDatabase() {
		String why = "day:"+this.day+", type:"+this.type
					 +", userProfile:"+this.userProfile;
		
		return why;
	}
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public static boolean isValidDay(Date day) {
		return day!=null;
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
    	if(type.length() < TYPE_MIN_SIZE || type.length() > TYPE_MAX_SIZE){
			return false;
		}
		return WorkTransferTypeEnum.isValid(type);
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
        if (!(object instanceof WorkTransferRequest)) {
            return false;
        }
        WorkTransferRequest other = (WorkTransferRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.WorkTransferRequest[ id=" + id + " ]";
    }
    
}

