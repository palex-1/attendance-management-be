package it.palex.attendanceManagement.data.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.dom4j.tree.AbstractEntity;

import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "global_configurations")
public class GlobalConfigurations extends AbstractEntity implements  DatabaseCheckableEntity {
	
	private static final long serialVersionUID = -22834245279762931L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    
    public static final int MIN_SETTING_AREA_SIZE = 1;
    public static final int MAX_SETTING_AREA_SIZE = 50;
    	
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_SETTING_AREA_SIZE, max = MAX_SETTING_AREA_SIZE)
    @Column(name = "setting_area")
    private String settingArea;
    
    public static final int MIN_SETTING_KEY_SIZE = 1;
    public static final int MAX_SETTING_KEY_SIZE = 50;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_SETTING_KEY_SIZE, max = MAX_SETTING_KEY_SIZE)
    @Column(name = "setting_key")
    private String settingKey;
    
    
    public static final int MAX_SETTING_VALUE_SIZE = 2147483647;
    
    @Size(max = MAX_SETTING_VALUE_SIZE)
    @Column(name = "setting_value")
    private String settingValue;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "visible")
    private Boolean visible = true;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "editable")
    private Boolean editable = true;

    public GlobalConfigurations() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidSettingArea(this.settingArea)  && isValidSettingKey(this.settingKey) 
				&& isValidSettingValue(this.settingValue) && isValidVisible(this.visible)
				&& isValidEditable(this.editable);
		
		return isValid;
	}
    
    @Override
	public String whyCannotBeInsertedInDatabase() {
		final String why = "settingArea:"+this.settingArea+", settingKey:"+ this.settingKey+", settingValue:"+ this.settingValue
				+", visible:"+ this.visible+", editable:"+ this.editable;
		
		return why;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
    	if( settingArea.length() < MIN_SETTING_AREA_SIZE ||  settingArea.length() > MAX_SETTING_AREA_SIZE){
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
    	if( settingKey.length() < MIN_SETTING_KEY_SIZE ||  settingKey.length() > MAX_SETTING_KEY_SIZE){
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
    		return true; // is valid
    	}
    	if( settingValue.length() > MAX_SETTING_VALUE_SIZE){
    		return false;
    	}
    	return true;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    
    public static boolean isValidVisible(Boolean visible) {
    	return visible!=null;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GlobalConfigurations)) {
            return false;
        }
        GlobalConfigurations other = (GlobalConfigurations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "GlobalConfigurations [id=" + id + "]";
	}

}
