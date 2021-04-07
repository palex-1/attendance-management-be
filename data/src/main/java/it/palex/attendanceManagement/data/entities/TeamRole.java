package it.palex.attendanceManagement.data.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.enumTypes.TeamRoleEnum;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "team_role")
public class TeamRole implements Serializable, DatabaseCheckableEntity {
    
	private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    public static final int MIN_ROLE_SIZE = 1;
    public static final int MAX_ROLE_SIZE = 25;
    
    @Size(min=MIN_ROLE_SIZE, max = MAX_ROLE_SIZE)
    @NotNull
    @Column(name = "role")
    private String role;
    
    public TeamRole() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidRole(this.role);
		
		return isValid;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public static boolean isValidRole(String role) {
    	if(role==null) {
    		return false;
    	}
    	if(role.length()<MIN_ROLE_SIZE || role.length()>MAX_ROLE_SIZE) {
    		return false;
    	}
    	
    	return TeamRoleEnum.isValid(role);
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
        if (!(object instanceof TeamRole)) {
            return false;
        }
        TeamRole other = (TeamRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.TeamRole[ id=" + id + " ]";
    }

}
