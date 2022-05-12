package it.palex.attendanceManagement.data.dto.settings;

import it.palex.attendanceManagement.library.rest.dtos.DTO;

public class GlobalConfigurationsDTO implements DTO {

	private static final long serialVersionUID = -6985195575972834445L;
	
	private Integer id;
	private String settingArea;
	private String settingKey;
	private String settingValue;
	private Boolean visible;
	private Boolean editable;
	private Boolean secret;

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

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	public Boolean getSecret() {
		return secret;
	}

	public void setSecret(Boolean secret) {
		this.secret = secret;
	}
}
