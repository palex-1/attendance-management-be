package it.palex.attendanceManagement.data.entities;

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

import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "completed_task")
public class CompletedTask extends AuditableEntity implements DatabaseCheckableEntity {
    
	private static final long serialVersionUID = 1L;
    
	public static short HOURS_OF_DAY = 24;
		
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    public static short MIN_TOTAL_HOURS = 1;
    public static short MAX_TOTAL_HOURS = 24;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "worked_hours")
    private Short workedHours;

	public static final int ACTIVITY_DESCRIPTION_MAX_SIZE = 255;

	@Size(max = ACTIVITY_DESCRIPTION_MAX_SIZE)
	@Column(name = "ACTIVITY_DESCRIPTION")
	private String activityDescription;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "day")
    @Temporal(TemporalType.DATE)
    private Date day;
    
    public static final boolean DEFAULT_SMARTWORKED = false;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "smartworked")
    private Boolean smartworked = DEFAULT_SMARTWORKED;
    
    public static final boolean DEFAULT_EDITABLE = true;
    
    @NotNull
    @Basic(optional = false)
    @Column(name = "editable")
    private Boolean editable = DEFAULT_EDITABLE;
    
    @NotNull
    @Basic(optional = false)
    @Column(name = "total_cost")
    private Double totalCost;
    
        
    @JoinColumn(name = "task_code", referencedColumnName = "task_code")
    @ManyToOne(optional = false)
    private WorkTask taskCode;
    
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;

    public CompletedTask() {
    }
    
    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidWorkedHours(this.workedHours)&& 
					isValidUserProfile(this.userProfile)&& isValidSmartworked(this.smartworked)
					&& isValidDay(this.day) && isValidWorkTask(this.taskCode)
					&& isValidEditable(this.editable) && isValidActivityDescription(this.activityDescription)
					&& isValidTotalCost(this.totalCost);
		
		return isValid;
	}


	public String whyCannotBeInsertedInDatabase() {
    	String why = "workedHours:"+this.workedHours+", userProfile:"+this.userProfile
    			+", smartworked"+this.smartworked+", taskCode:"+this.taskCode
    			+", editable:"+this.editable+", day:"+this.day
				+", activityDescription:"+this.activityDescription
				+", totalCost:"+this.totalCost;

		return why;
	}
    

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getWorkedHours() {
		return workedHours;
	}

	public void setWorkedHours(Short workedHours) {
		this.workedHours = workedHours;
	}

	public static boolean isValidWorkedHours(Short workedHours) {
    	if(workedHours==null) {
    		return false;
    	}
    	if(workedHours < MIN_TOTAL_HOURS || workedHours > MAX_TOTAL_HOURS) {
    		return false;
    	}
    	return true;
    }

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public static boolean isValidTotalCost(Double totalCost) {
		if(totalCost==null) {
			return false;
		}
		if(totalCost.doubleValue() < 0d) {
			return false;
		}
		return true;
	}
	
    public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public static boolean isValidDay(Date day) {
    	if(day==null) {
    		return false;
    	}
    	return true;
    }

	
    public Boolean getSmartworked() {
		return smartworked;
	}

	public void setSmartworked(Boolean smartworked) {
		this.smartworked = smartworked;
	}

	public static boolean isValidSmartworked(Boolean smartworked){
    	if(smartworked==null) {
    		return false;
    	}
    	return true;
    }
    

    
    public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public static boolean isValidUserProfile(UserProfile userProfile) {
    	if(userProfile==null) {
    		return false;
    	}
    	return true;
    }

   
	public WorkTask getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(WorkTask taskCode) {
		this.taskCode = taskCode;
	}

	public static boolean isValidWorkTask(WorkTask taskCode) {
    	if(taskCode==null) {
    		return false;
    	}
    	return true;
    }
	
	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
	
	public static boolean isValidEditable(Boolean editable) {
		return editable!=null;
	}


	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public String getActivityDescription(){
    	return this.activityDescription;
	}

	public static boolean isValidActivityDescription(String activityDescription) {
    	if(activityDescription==null){
    		return true;
		}
    	if(activityDescription.length()>ACTIVITY_DESCRIPTION_MAX_SIZE){
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
        if (!(object instanceof CompletedTask)) {
            return false;
        }
        CompletedTask other = (CompletedTask) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.CompletedTask[ id=" + id + " ]";
    }


}
