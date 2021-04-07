package it.palex.attendanceManagement.data.entities;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import it.palex.attendanceManagement.data.entities.enumTypes.ContactTypeEnum;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "user_contact_type")
public class UserContactType implements Serializable, DatabaseCheckableEntity {
	    
	private static final long serialVersionUID = -7789224925009713919L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
	public static final int MIN_C_TYPE_SIZE = 1;
	public static final int MAX_C_TYPE_SIZE = 30;
	
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_C_TYPE_SIZE, max = MAX_C_TYPE_SIZE)
    @Column(name = "c_type")
    private String cType;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userContactType")
    private List<UserContacts> userContactsList;

    public UserContactType() {
    }
    
    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidCType(this.cType);
		
		return isValid;
	}
    
    @Override
	public String whyCannotBeInsertedInDatabase() {
		final String why = "cType:"+this.cType;
		
		return why;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCType() {
        return cType;
    }

    public void setCType(String cType) {
        this.cType = cType;
    }
    
    public static boolean isValidCType(String cType) {
    	if(cType==null) {
    		return false;
    	}
    	if( cType.length() < MIN_C_TYPE_SIZE ||  cType.length() > MAX_C_TYPE_SIZE){
    		return false;
    	}
    	
    	return ContactTypeEnum.isValid(cType);
    }

    @XmlTransient
    public List<UserContacts> getUserContactsList() {
        return userContactsList;
    }

    public void setUserContactsList(List<UserContacts> userContactsList) {
        this.userContactsList = userContactsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserContactType)) {
            return false;
        }
        UserContactType other = (UserContactType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

   
    
    
}
