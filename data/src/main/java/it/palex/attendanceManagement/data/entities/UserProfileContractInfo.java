package it.palex.attendanceManagement.data.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import it.palex.attendanceManagement.data.entities.core.UserLevel;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

@Entity
@Table(name = "user_profile_contract_info")
public class UserProfileContractInfo extends AuditableEntity implements DatabaseCheckableEntity {
	
	private static final long serialVersionUID = 1L;
    
	@Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
	
    @Basic(optional = false)
    @NotNull
    @Column(name = "work_day_hours")
    private Integer workDayHours;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "residual_vacation_days")
    private Double residualVacationDays;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "residual_leave_hours")
    private Double residualLeaveHours;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "hourly_cost")
    private Double hourlyCost;
    
    
    @JoinColumn(name = "level", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserLevel level;
        
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @OneToOne(optional = false)
    private UserProfile userProfile;

    @JoinColumn(name = "employment_office", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Office employmentOffice;
    
    
    public UserProfileContractInfo() {
    }


	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidWorkDayHours(this.workDayHours) 
				&& isValidUserLevel(this.level) 
				&& isValidResidualVacationDays(this.residualVacationDays)
				&& isValidResidualLeaveHours(this.residualLeaveHours)
				&& isValidUserProfile(this.userProfile)
				&& isValidHourlyCost(this.hourlyCost);
		
		return isValid;
	}	

	
	public String whyCannotBeInsertedInDatabase() {
		String why = "workDayHours:"+this.workDayHours
					 +", level:"+this.level
					 +", residualVacationDays:"+this.residualVacationDays
					 +", residualLeaveHours:"+this.residualLeaveHours
					 +", userProfile:"+this.userProfile
					 +", hourlyCost:"+this.hourlyCost;
		
		return why;
	}
	
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkDayHours() {
        return workDayHours;
    }

    public void setWorkDayHours(Integer workDayHours) {
        this.workDayHours = workDayHours;
    }
    
    public static boolean isValidWorkDayHours(Integer workDayHours) {
		return workDayHours!=null;
	}
    

    public UserLevel getLevel() {
        return level;
    }

    public void setLevel(UserLevel level) {
        this.level = level;
    }
    
    public static boolean isValidUserLevel(UserLevel level) {
		return level!=null;
	}
    

    public Double getResidualVacationDays() {
		return residualVacationDays;
	}

	public void setResidualVacationDays(Double residualVacationDays) {
		this.residualVacationDays = residualVacationDays;
	}

	public static boolean isValidResidualVacationDays(Double residualVacationDays) {
		return residualVacationDays!=null;
	}

	

	public Office getEmploymentOffice() {
		return employmentOffice;
	}


	public void setEmploymentOffice(Office employmentOffice) {
		this.employmentOffice = employmentOffice;
	}


	public Double getResidualLeaveHours() {
		return residualLeaveHours;
	}


	public void setResidualLeaveHours(Double residualLeaveHours) {
		this.residualLeaveHours = residualLeaveHours;
	}

	public static boolean isValidResidualLeaveHours(Double residualLeaveHours) {
		return residualLeaveHours!=null;
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
    
    public Double getHourlyCost() {
		return hourlyCost;
	}

	public void setHourlyCost(Double hourlyCost) {
		this.hourlyCost = hourlyCost;
	}

	public static boolean isValidHourlyCost(Double hourlyCost) {
		if(hourlyCost==null) {
			return false;
		}
		if(hourlyCost.doubleValue() < 0d) {
			return false;
		}
		return true;
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
        if (!(object instanceof UserProfileContractInfo)) {
            return false;
        }
        UserProfileContractInfo other = (UserProfileContractInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.UserProfileContractInfo[ id=" + id + " ]";
    }

    
}
