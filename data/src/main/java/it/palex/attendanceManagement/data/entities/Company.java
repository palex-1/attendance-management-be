package it.palex.attendanceManagement.data.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.converters.OneLineStringSanitizerConverter;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "company")
public class Company extends AuditableEntity implements DatabaseCheckableEntity {
	
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    public static final int MIN_NAME_SIZE = 1;
	public static final int MAX_NAME_SIZE = 255;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_NAME_SIZE, max = MAX_NAME_SIZE)
    @Column(name = "name")
    private String name;
    
    public static final int MIN_DESCRIPTION_SIZE = 1;
	public static final int MAX_DESCRIPTION_SIZE = 500;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "description")
    private String description;

        
    public static final boolean IS_ROOT_DEFAULT = false;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "IS_ROOT")
    private Boolean isRoot = IS_ROOT_DEFAULT;
    
    
    
    public Company() {
    }

    @PrePersist
    @PreUpdate
    public void sanitizeBeforeUpdate() {
    	this.name = OneLineStringSanitizerConverter.convertToDatabaseColumn(this.name);
    	this.description = OneLineStringSanitizerConverter.convertToDatabaseColumn(this.description);
    }
    
    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidName(this.name) && isValidDescription(this.description)
				&& isValidIsRoot(this.isRoot);
		
		return isValid;
	}
    

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean isValidName(String name) {
    	if(name==null) {
    		return false;
    	}
    	if( name.length() < MIN_NAME_SIZE ||  name.length() > MAX_NAME_SIZE){
			return false;
		}
    	return true;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public static boolean isValidDescription(String description) {
    	if(description==null) {
    		return false;
    	}
    	if( description.length() < MIN_DESCRIPTION_SIZE ||  description.length() > MAX_DESCRIPTION_SIZE){
			return false;
		}
    	return true;
    }
    
    public Boolean getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}

	private boolean isValidIsRoot(Boolean isRoot) {
		return this.isRoot!=null;
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
        if (!(object instanceof Company)) {
            return false;
        }
        Company other = (Company) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.Company[ id=" + id + " ]";
    }
    
}
