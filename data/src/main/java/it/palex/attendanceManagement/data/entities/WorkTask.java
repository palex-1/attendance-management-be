package it.palex.attendanceManagement.data.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "work_task")
public class WorkTask extends AuditableEntity implements DatabaseCheckableEntity {
	
	private static final long serialVersionUID = 2223399805548618898L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
	
	public static final int TASK_CODE_MIN_SIZE = 1;
    public static final int TASK_CODE_MAX_SIZE = 25;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = TASK_CODE_MIN_SIZE, max = TASK_CODE_MAX_SIZE)
    @Column(name = "task_code")
    private String taskCode;
    
    public static final int TASK_DESCRIPTION_MIN_SIZE = 1;
    public static final int TASK_DESCRIPTION_MAX_SIZE = 1000;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = TASK_DESCRIPTION_MIN_SIZE, max = TASK_DESCRIPTION_MAX_SIZE)
    @Column(name = "task_description")
    private String taskDescription;
    
    public static final int PIVA_VAT_NUM_MAX_SIZE = 30;
    
    @Size(max = PIVA_VAT_NUM_MAX_SIZE)
    @Column(name = "client_vat_num")
    private String clientVatNum;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "billable")
    private Boolean billable;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_enabled_for_all_user")
    private Boolean isEnabledForAllUser;
        
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_absence_task")
    private Boolean isAbsenceTask;
        
    @Basic(optional = false)
    @NotNull
    @Column(name = "activation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activationDate;
    
    @Column(name = "deactivation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deactivationDate;
    
    @Column(name = "total_budget")
    @Basic(optional = false)
    @NotNull
    private Double totalBudget;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskCode")
    private List<CompletedTask> completedTaskList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskCode")
    private List<TeamComponentTask> teamComponentTaskList;
    
    public WorkTask() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidTaskCode(this.taskCode) 
				&& isValidTaskDescription(this.taskDescription)
				 && isValidClientVatNum(this.clientVatNum)
				  && isValidBillable(this.billable)
				   && isValidActivationDate(this.activationDate)
				   && isValidDeactivationDate(this.deactivationDate)
				   && areAdmissibleActivationDeactivation(this.activationDate, this.deactivationDate)
				   && isValidIsEnabledForAllUser(this.isEnabledForAllUser)
				   && isValidIsAbsenceTask(this.isAbsenceTask)
				   && isValidTotalBudget(this.totalBudget);
		
		return isValid;
	}
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public static boolean isValidTaskCode(String taskCode){
    	if(taskCode==null) {
    		return false;
    	}
    	if(taskCode.length() < TASK_CODE_MIN_SIZE || taskCode.length() > TASK_CODE_MAX_SIZE){
			return false;
		}
		return true;
    }

   
    public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public static boolean isValidTaskDescription(String taskDescription) {
    	if(taskDescription==null){
			return false;
		}
		if(taskDescription.length() < TASK_DESCRIPTION_MIN_SIZE || taskDescription.length() > TASK_DESCRIPTION_MAX_SIZE){
			return false;
		}
		return true;
    }
    
    public Double getTotalBudget() {
		return totalBudget;
	}

	public void setTotalBudget(Double totalBudget) {
		this.totalBudget = totalBudget;
	}

	public static boolean isValidTotalBudget(Double totalBudget) {
		if(totalBudget==null) {
			return false;
		}
		if(totalBudget.doubleValue() < 0d) {
			return false;
		}
		return true;
	}

	public String getClientVatNum() {
		return clientVatNum;
	}

	public void setClientVatNum(String clientVatNum) {
		this.clientVatNum = clientVatNum;
	}

	public static boolean isValidClientVatNum(String clientVatNum) {
    	if(clientVatNum==null){
			return true;
		}
		if(clientVatNum.length() > PIVA_VAT_NUM_MAX_SIZE){
			return false;
		}
		return true;
    }
    
    
    
    public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public static boolean isValidBillable(Boolean billable) {
    	if(billable==null) {
    		return false;
    	}
    	return true;
    }
    
    public static boolean isValidDisattivato(Boolean disattivato) {
    	if(disattivato==null) {
    		return false;
    	}
    	return true;
    }

    
    
    public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public static boolean isValidActivationDate(Date activationDate) {
    	if(activationDate==null) {
    		return false;
    	}
    	return true;
    }

    /**
	 * @param dataAttivazione
	 * @param dataDisattivazione
	 * @return
	 */
	public static boolean areAdmissibleActivationDeactivation(Date dataAttivazione, Date dataDisattivazione) {
		boolean isAdmissible = isValidActivationDate(dataAttivazione) 
				&& isValidDeactivationDate(dataDisattivazione);
		
		if (!isAdmissible) {
			return false;
		}
		if (dataAttivazione == null && dataDisattivazione != null) {
			return false;
		}
		if (dataAttivazione != null && dataDisattivazione == null) {
			return true;
		}

		return dataAttivazione.before(dataDisattivazione);
	}
	
    
    public Date getDeactivationDate() {
		return deactivationDate;
	}

	public void setDeactivationDate(Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	public static boolean isValidDeactivationDate(Date deactivationDate) {
    	return true; //no constraints
    }

	public Boolean getIsEnabledForAllUser() {
		return isEnabledForAllUser;
	}

	public void setIsEnabledForAllUser(Boolean isEnabledForAllUser) {
		this.isEnabledForAllUser = isEnabledForAllUser;
	}

	public static boolean isValidIsEnabledForAllUser(Boolean isEnabledForAllUser) {
		return isEnabledForAllUser!=null;
	}
   
	
	public Boolean getIsAbsenceTask() {
		return isAbsenceTask;
	}

	public void setIsAbsenceTask(Boolean isAbsenceTask) {
		this.isAbsenceTask = isAbsenceTask;
	}

	public static  boolean isValidIsAbsenceTask(Boolean isAbsenceTask) {
		return isAbsenceTask!=null;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

	//NOTE IF YOY CHANGE THIS METHOD THE StandardReportGeneratorXLS COULD NOT WORK
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkTask)) {
            return false;
        }
        WorkTask other = (WorkTask) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.TaskCode[ id=" + id + " ]";
    }
    
}
