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

import it.palex.attendanceManagement.data.entities.enumTypes.TurnstileTypeEnum;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 26 giu 2020
 */
@Entity
@Table(name = "turnstile")
public class Turnstile extends AuditableEntity implements DatabaseCheckableEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    public static final int TITLE_MAX_SIZE = 100;
    
    @NotNull
    @Size(max = TITLE_MAX_SIZE)
    @Column(name = "title")
    private String title;
    
    public static final int DESCRIPTION_MAX_SIZE = 1000;
    
    @NotNull
    @Size(max = DESCRIPTION_MAX_SIZE)
    @Column(name = "description")
    private String description;
    
    public static final int POSITION_MAX_SIZE = 200;
    
    @NotNull
    @Size(max = POSITION_MAX_SIZE)
    @Column(name = "position")
    private String position;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "deactivated")
    private Boolean deactivated;
    
    public static final int TYPE_MAX_SIZE = 20;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = TYPE_MAX_SIZE)
    @Column(name = "type")
    private String type;
 
    
    public Turnstile() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidTitle(this.title) && isValidDescription(this.description)
				&& isValidPosition(this.position) && isValidDeactivated(this.deactivated)
				&& isValidType(this.type);
		
		return isValid;
	}
    

	
	public String whyCannotBeInsertedInDatabase() {
		String why = "title:"+this.title+", description:"+this.description
					 +", position:"+this.position+", deactivated:"+this.deactivated;
		
		return why;
	}
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public static boolean isValidTitle(String title) {
		if(title==null) {
			return false;
		}
		if(title.length()>TITLE_MAX_SIZE) {
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
		if(description.length()>DESCRIPTION_MAX_SIZE) {
			return false;
		}
		return true;
	}
    
    
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public static boolean isValidPosition(String position) {
		if(position==null) {
			return false;
		}
		if(position.length()>POSITION_MAX_SIZE) {
			return false;
		}
		return true;
	}
    
    
    public Boolean getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(Boolean deactivated) {
        this.deactivated = deactivated;
    }
    
    public static boolean isValidDeactivated(Boolean deactivated) {
		return deactivated!=null;
	}
    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	private boolean isValidType(String type) {
		if(type==null) {
			return false;
		}
		if(type.length()>TYPE_MAX_SIZE) {
			return false;
		}
		return TurnstileTypeEnum.isValid(type);
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
        if (!(object instanceof Turnstile)) {
            return false;
        }
        Turnstile other = (Turnstile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.Turnstile[ id=" + id + " ]";
    }
    
}

