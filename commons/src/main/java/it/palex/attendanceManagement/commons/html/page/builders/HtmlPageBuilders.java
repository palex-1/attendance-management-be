package it.palex.attendanceManagement.commons.html.page.builders;

import org.springframework.stereotype.Component;

/**
 * Very basic implementation of web pages static.
 * If you want to add dynamic content remember to escape every dinamic value with 
 *  org.springframework.web.util.HtmlUtils.htmlEscape(placeholderValue);
 *  
 * @author Alessandro Pagliaro
 *
 */
@Component
public class HtmlPageBuilders {
	
//	@Autowired
//	private GlobalConfigurationsService globalConfigService;
//	
//    public String buildInternalServerError() {
//    	GlobalConfigurations config = this.globalConfigService.findByAreaAndKey(
//				GlobalConfigurationSettingsTuple.HTML_PAGES.AREA_NAME,  
//				GlobalConfigurationSettingsTuple.HTML_PAGES.INTERNAL_SERVER_ERROR_PAGE_HTML);
//    	
//    	if(config==null || config.getSettingValue()==null) {
//			throw new InvalidConfigurationException("Invalid area:"+GlobalConfigurationSettingsTuple.HTML_PAGES.AREA_NAME+" "
//					+ "key:"+GlobalConfigurationSettingsTuple.HTML_PAGES.INTERNAL_SERVER_ERROR_PAGE_HTML);
//		}
//		
//		return config.getSettingValue();
//    }

}
