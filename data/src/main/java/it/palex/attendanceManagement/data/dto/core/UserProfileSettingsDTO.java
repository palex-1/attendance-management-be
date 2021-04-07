package it.palex.attendanceManagement.data.dto.core;

public class UserProfileSettingsDTO {
	private Long id;
    private String settingArea;
    private String settingKey;
    private String settingValue;
    private Boolean editable;
    
    
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
	
	public String getSettingKey() {
		return settingKey;
	}
	public void setSettingKey(String settingKey) {
		this.settingKey = settingKey;
	}
	
	public String getSettingValue() {
		return settingValue;
	}
	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}
	
	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
}
