package it.palex.attendanceManagement.data.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.converters.OneLineStringSanitizerConverter;
import it.palex.attendanceManagement.data.entities.converters.ToUpperCaseAndTrimmerSpaceReplacerConverter;
import it.palex.attendanceManagement.data.entities.converters.ToUpperCaseConverter;
import it.palex.attendanceManagement.data.entities.converters.TrimmerAndSpaceReplacerConverter;
import it.palex.attendanceManagement.data.entities.enumTypes.AddressTypeEnum;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author palex
 */
@Entity
@Table(name = "user_profile_address")
public class UserProfileAddress extends AuditableEntity implements DatabaseCheckableEntity {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    public static final int MIN_STREET_SIZE = 1;
    public static final int MAX_STREET_SIZE = 255;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = MAX_STREET_SIZE)
    @Column(name = "street")
    private String street;
    
    public static final int MAX_CITY_SIZE = 255;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = MAX_CITY_SIZE)
    @Column(name = "city")
    private String city;
    
    public static final int MAX_PROVINCE_SIZE = 255;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = MAX_PROVINCE_SIZE)
    @Column(name = "province")
    private String province;
    
    public static final int MAX_NATION_SIZE = 255;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = MAX_NATION_SIZE)
    @Column(name = "nation")
    private String nation;
    
    public static final int MIN_ZIP_CODE_SIZE = 1;
    public static final int MAX_ZIP_CODE_SIZE = 10;
    
    @Basic(optional = false)
    @NotNull
    @Size(max = MAX_ZIP_CODE_SIZE)
    @Column(name = "zip_code")
    private String zipCode;
    
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "address_type")
    private String addressType;
    
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;

    public UserProfileAddress() {
    }
    
    @PrePersist
    @PreUpdate
    public void sanitizeBeforeUpdate() {
    	this.zipCode = TrimmerAndSpaceReplacerConverter.convertToDatabaseColumn(this.zipCode);
    	this.nation = TrimmerAndSpaceReplacerConverter.convertToDatabaseColumn(this.nation);
    	this.province = ToUpperCaseAndTrimmerSpaceReplacerConverter.convertToDatabaseColumn(this.province);
    	this.city = TrimmerAndSpaceReplacerConverter.convertToDatabaseColumn(this.city);
    	this.street = TrimmerAndSpaceReplacerConverter.convertToDatabaseColumn(this.street);
    }
    
    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidStreet(this.street) && isValidCity(this.city)
				&& isValidProvice(this.province) && isValidNation(this.nation)
				&& isValidZipCode(this.zipCode) && isValidAddressType(this.addressType) 
				&& isValidUserProfile(this.userProfile);
		
		return isValid;
	}

	@Override
    public String whyCannotBeInsertedInDatabase() {
    	String why = "street:"+this.street+", city:"+this.city
    			+", province:"+this.province+", nation:"+this.nation
    			+", zipCode:"+this.zipCode+", addressType:"+this.addressType
    			+", userProfile:"+this.userProfile;
    	
    	return why;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public static boolean isValidStreet(String street) {
    	if(street==null) {
			return false;
		}
		if(street.length()<MIN_STREET_SIZE || street.length()>MAX_STREET_SIZE) {
			return false;
		}
		
		return true;
	}
    
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static boolean isValidCity(String city) {
    	if(city==null) {
			return false;
		}
		if(city.length()>MAX_CITY_SIZE) {
			return false;
		}
		
		return true;
	}
    
    
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public static boolean isValidProvice(String province) {
		if(province==null) {
			return false;
		}
		if(province.length()>MAX_PROVINCE_SIZE) {
			return false;
		}
		
		return true;
	}
    
    
    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public static boolean isValidNation(String nation) {
    	if(nation==null) {
			return false;
		}
		if(nation.length()>MAX_NATION_SIZE) {
			return false;
		}
		
		return true;
	}
    
    
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public static boolean isValidZipCode(String zipCode) {
    	if(zipCode==null) {
			return false;
		}
		if(zipCode.length()<MIN_ZIP_CODE_SIZE || zipCode.length()>MAX_ZIP_CODE_SIZE) {
			return false;
		}
		
		return true;
	}
    

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
    
    public static boolean isValidAddressType(String addressType) {
    	if(addressType==null) {
			return false;
		}
    	
		return AddressTypeEnum.isValid(addressType);
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
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserProfileAddress)) {
            return false;
        }
        UserProfileAddress other = (UserProfileAddress) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.UserProfileAddress[ id=" + id + " ]";
    }

	
}
