package it.palex.attendanceManagement.data.utils;

import java.util.List;

import org.codehaus.plexus.util.StringUtils;

import it.palex.attendanceManagement.data.entities.GlobalConfigurations;

public class GlobalConfigurationUtils {

	public static String getValue(List<GlobalConfigurations> configs, String area, String key) {
		if (configs == null) {
			return null;
		}

		for (GlobalConfigurations conf : configs) {
			if (StringUtils.equalsIgnoreCase(conf.getSettingArea(), area)
					&& StringUtils.equalsIgnoreCase(conf.getSettingKey(), key)) {
				return conf.getSettingValue();
			}
		}

		return null;
	}

	public static String getValueSkippingArea(List<GlobalConfigurations> configs, String key) {
		if (configs == null) {
			return null;
		}

		for (GlobalConfigurations conf : configs) {
			if (StringUtils.equalsIgnoreCase(conf.getSettingKey(), key)) {
				return conf.getSettingValue();
			}
		}

		return null;
	}
}
