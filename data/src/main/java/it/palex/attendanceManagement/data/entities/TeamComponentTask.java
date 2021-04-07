package it.palex.attendanceManagement.data.entities;

import java.io.Serializable;

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

import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "team_component_task")
public class TeamComponentTask extends AuditableEntity implements Serializable, DatabaseCheckableEntity {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;
    
    @JoinColumn(name = "task_code", referencedColumnName = "task_code")
    @ManyToOne(optional = false)
    private WorkTask taskCode;
    
    @NotNull
    @Column(name = "deleted")
    @Basic(optional = false)
    private Boolean deleted = false;
    
    @JoinColumn(name = "team_role", referencedColumnName = "id")
    @ManyToOne
    private TeamRole teamRole;

    public TeamComponentTask() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidUserProfile(this.userProfile) && isValidDeleted(this.deleted)
				&& isValidTaskCode(this.taskCode) && isValidTeamRole(this.teamRole);
		
		return isValid;
	}

    

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

	
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	private boolean isValidDeleted(Boolean deleted) {
		return deleted!=null;
	}
    
    public WorkTask getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(WorkTask taskCode) {
		this.taskCode = taskCode;
	}

	public static boolean isValidTaskCode(WorkTask taskCode) {
    	if(taskCode==null) {
    		return false;
    	}
    	return true;
    }
	
    public TeamRole getTeamRole() {
		return teamRole;
	}

	public void setTeamRole(TeamRole teamRole) {
		this.teamRole = teamRole;
	}

	public static boolean isValidTeamRole(TeamRole teamRole){
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
        if (!(object instanceof TeamComponentTask)) {
            return false;
        }
        TeamComponentTask other = (TeamComponentTask) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.TeamComponentTask[ id=" + id + " ]";
    }

    
}

