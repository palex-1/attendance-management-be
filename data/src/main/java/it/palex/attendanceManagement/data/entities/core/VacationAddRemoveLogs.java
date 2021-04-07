package it.palex.attendanceManagement.data.entities.core;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 12 lug 2020
 */
@Entity
@Table(name = "vacation_add_remove_logs")
public class VacationAddRemoveLogs extends AuditableEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "type")
    private String type;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount_of_hours")
    private double amountOfHours;
    
    @Column(name = "lock_id")
    private Integer lockId;
    
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;
    

    public VacationAddRemoveLogs() {
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

    public double getAmountOfHours() {
        return amountOfHours;
    }

    public void setAmountOfHours(double amountOfHours) {
        this.amountOfHours = amountOfHours;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    
    public Integer getLockId() {
		return lockId;
	}

	public void setLockId(Integer lockId) {
		this.lockId = lockId;
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
        if (!(object instanceof VacationAddRemoveLogs)) {
            return false;
        }
        VacationAddRemoveLogs other = (VacationAddRemoveLogs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.VacationAddRemoveLogs[ id=" + id + " ]";
    }
    
}

