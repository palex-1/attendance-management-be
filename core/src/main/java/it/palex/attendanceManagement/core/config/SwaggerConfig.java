package it.palex.attendanceManagement.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * @author Alessandro Pagliaro
 *
 */
@Profile({"local", "dev"})
@Configuration
public class SwaggerConfig {

	@Value("${swagger.api.title}")
	private String apiTitle;
	
	@Value("${swagger.api.description}")
	private String apiDescription;
	
	@Value("${swagger.api.version}")
	private String apiVersion;
	
	@Value("${swagger.api.termsOfServiceUrl}")
	private String apiTermsOfService;
	
	@Value("${swagger.api.contact.name}")
	private String apiContactName;
	
	@Value("${swagger.api.contact.site-url}")
	private String apiContactSiteUrl;
	
	@Value("${swagger.api.contact.site-email}")
	private String apiContactSiteEmail;
	
	@Value("${swagger.api.license}")
	private String apiLicence;
	
	@Value("${swagger.api.licenseUrl}")
	private String apiLicenceUrl;
	
	
	
	@Bean
    public OpenAPI customOpenAPI() {
		Contact contact = new Contact();
		contact.setName(this.apiContactName);
		contact.setUrl(this.apiContactSiteUrl);

		License licence = new License();
		licence.setName(this.apiLicence);
		licence.setUrl(this.apiLicenceUrl);
		
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title(this.apiTitle)
                		.description(this.apiDescription)
                		.contact(contact)
                		.license(licence)
                	);
    }
	
	
}