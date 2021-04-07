package it.palex.attendanceManagement.data.entities.core;

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
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "user_profile_setting")
public class UserProfileSetting extends AuditableEntity implements Serializable, DatabaseCheckableEntity {
	
	public static final String TRUE_PROPERTY_VALUE = "TRUE";
	public static final String FALSE_PROPERTY_VALUE = "FALSE";
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    public static final int SETTING_AREA_MIN_SIZE = 1;
    public static final int SETTING_AREA_MAX_SIZE = 50;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = SETTING_AREA_MIN_SIZE, max = SETTING_AREA_MAX_SIZE)
    @Column(name = "setting_area")
    private String settingArea;
    
    public static final int SETTING_KEY_MIN_SIZE = 1;
    public static final int SETTING_KEY_MAX_SIZE = 50;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = SETTING_KEY_MIN_SIZE, max = SETTING_KEY_MAX_SIZE)
    @Column(name = "setting_key")
    private String settingKey;
    
    public static final int SETTING_VALUE_MIN_SIZE = 1;
    public static final int SETTING_VALUE_MAX_SIZE = 1000;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = SETTING_VALUE_MIN_SIZE, max = SETTING_VALUE_MAX_SIZE)
    @Column(name = "setting_value")
    private String settingValue;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "editable")
    private boolean editable = true;
    
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;
    

    public UserProfileSetting() {
    }

    
    public UserProfileSetting(String settingArea, String settingKey, String settingValue, boolean editable,
			UserProfile userProfile) {
		super();
		this.settingArea = settingArea;
		this.settingKey = settingKey;
		this.settingValue = settingValue;
		this.editable = editable;
		this.userProfile = userProfile;
	}


	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidUserProfile(this.userProfile) && 
				isValidSettingArea(this.settingArea) && isValidSettingKey(this.settingKey) 
				&& isValidSettingValue(this.settingValue);
		
		return isValid;
	}
    
    @Override
	public String whyCannotBeInsertedInDatabase() {
		final String why = "userProfile:"+this.userProfile+", settingArea:"+ this.settingArea+", settingValue:"+ this.settingValue;
		
		return why;
	}
    

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSettingArea() {
        return settingArea;
    }

    public void setSettingArea(String settingArea) {
        this.settingArea = settingArea;
    }
    
    public static boolean isValidSettingArea(String settingArea) {
		if(settingArea==null) {
			return false;
		}
		if(settingArea.length()<SETTING_AREA_MIN_SIZE || settingArea.length()>SETTING_AREA_MAX_SIZE) {
			return false;
		}
		return true;
	}

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public static boolean isValidSettingKey(String settingKey) {
		if(settingKey==null) {
			return false;
		}
		if(settingKey.length()<SETTING_KEY_MIN_SIZE || settingKey.length()>SETTING_KEY_MAX_SIZE) {
			return false;
		}
		return true;
	}
    
    
    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
    
    public static boolean isValidSettingValue(String settingValue) {
		if(settingValue==null) {
			return false;
		}
		if(settingValue.length()<SETTING_VALUE_MIN_SIZE || settingValue.length()>SETTING_VALUE_MAX_SIZE) {
			return false;
		}
		return true;
	}
    

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
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
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserProfileSetting)) {
            return false;
        }
        UserProfileSetting other = (UserProfileSetting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "UserProfileSetting [id=" + id + "]";
	}
    
}

