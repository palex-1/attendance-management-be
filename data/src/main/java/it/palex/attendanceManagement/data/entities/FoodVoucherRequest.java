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

import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 10 giu 2020
 */
@Entity
@Table(name = "food_voucher_request")
public class FoodVoucherRequest extends AuditableEntity implements DatabaseCheckableEntity {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "day")
    @Temporal(TemporalType.DATE)
    private Date day;
    
    public static final int DEFAULT_QUANTITY = 1;
    
    @NotNull
    @Basic(optional = false)
    @Column(name = "quantity")
    private Integer quantity = DEFAULT_QUANTITY;
    
    @NotNull
    @Basic(optional = false)
    @Column(name = "editable")
    private Boolean editable;
    
    
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;

    public FoodVoucherRequest() {
    }
    
    @Override
	public boolean canBeInsertedInDatabase() {
    	boolean isValid = isValidDay(this.day) && isValidQuantity(this.quantity)
    			&& isValidUserProfile(this.userProfile) 
    			&& iaValidEditable(this.editable);
    	
		return isValid;
	}
    
	public String whyCannotBeInsertedInDatabase() {
		return "day: "+this.day+", quantity:"+this.quantity
				+", userProfile:"+this.userProfile
				+", editable:"+this.editable;
	}
    
	
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }
    
    public static boolean isValidDay(Date day) {
		return day!=null;
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

    
    public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public static boolean isValidQuantity(Integer quantity) {
		return quantity!=null;
	}
	
	
	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
	
	public static boolean iaValidEditable(Boolean editable) {
		return editable!=null;
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
        if (!(object instanceof FoodVoucherRequest)) {
            return false;
        }
        FoodVoucherRequest other = (FoodVoucherRequest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.FoodVoucherRequest[ id=" + id + " ]";
    }

	
    
}
