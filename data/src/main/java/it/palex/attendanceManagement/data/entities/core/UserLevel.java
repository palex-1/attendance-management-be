package it.palex.attendanceManagement.data.entities.core;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 5 giu 2020
 */
@Entity
@Table(name = "user_level")
public class UserLevel extends AuditableEntity implements DatabaseCheckableEntity {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    
    public static final int MIN_LEVEL_SIZE = 1;
    public static final int MAX_LEVEL_SIZE = 50;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_LEVEL_SIZE, max = MAX_LEVEL_SIZE)
    @Column(name = "level")
    private String level;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "monthly_vacation_days")
    private Double monthlyVacationDays;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "monthly_leave_hours")
    private Double monthlyLeaveHours;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "bank_hour_enabled")
    private Boolean bankHourEnabled;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "extra_work_paid")
    private Boolean extraWorkPaid;
    
    
    public UserLevel() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidLevel(this.level) && isValidMonthlyVacationDays(this.monthlyVacationDays)
				&& isValidMonthlyLeaveHours(this.monthlyLeaveHours) && isValidBankHourEnabled(this.bankHourEnabled)
				&& isValidExtraWorkPaid(this.extraWorkPaid);
		
		return isValid;
	}
    	

	public String whyCannotBeInsertedInDatabase() {
		String why = "level:"+this.level+", monthlyVacationDays:"+this.monthlyVacationDays
					+", monthlyLeaveHours:"+this.monthlyLeaveHours+", bankHourEnabled:"+this.bankHourEnabled
					+", extraWorkPaid:"+this.extraWorkPaid;
		
		return why;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    
    public static boolean isValidLevel(String level) {
		if(level==null) {
			return false;
		}
		if(level.length()<MIN_LEVEL_SIZE || level.length()>MAX_LEVEL_SIZE) {
			return false;
		}
		return true;
	}
    
    
    public Double getMonthlyVacationDays() {
		return monthlyVacationDays;
	}

	public void setMonthlyVacationDays(Double monthlyVacationDays) {
		this.monthlyVacationDays = monthlyVacationDays;
	}
	
	public static boolean isValidMonthlyVacationDays(Double monthlyVacationDays) {
		return monthlyVacationDays!=null;
	}
	
	

	public Double getMonthlyLeaveHours() {
		return monthlyLeaveHours;
	}

	public void setMonthlyLeaveHours(Double monthlyLeaveHours) {
		this.monthlyLeaveHours = monthlyLeaveHours;
	}
	
	public static boolean isValidMonthlyLeaveHours(Double monthlyLeaveHours) {
		return monthlyLeaveHours!=null;
	}
	
	public Boolean getBankHourEnabled() {
		return bankHourEnabled;
	}

	public void setBankHourEnabled(Boolean bankHourEnabled) {
		this.bankHourEnabled = bankHourEnabled;
	}

	public static boolean isValidBankHourEnabled(Boolean bankHourEnabled) {
		return bankHourEnabled!=null;
	}
	
	
	public Boolean getExtraWorkPaid() {
		return extraWorkPaid;
	}

	public void setExtraWorkPaid(Boolean extraWorkPaid) {
		this.extraWorkPaid = extraWorkPaid;
	}
	
	public static boolean isValidExtraWorkPaid(Boolean extraWorkPaid) {
		return extraWorkPaid!=null;
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
        if (!(object instanceof UserLevel)) {
            return false;
        }
        UserLevel other = (UserLevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.UserLevel[ id=" + id + " ]";
    }



	
    
}

