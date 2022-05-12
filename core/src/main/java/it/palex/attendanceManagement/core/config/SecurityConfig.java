package it.palex.attendanceManagement.core.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import it.palex.attendanceManagement.commons.security.CustomAuthenticationTokenFilter;
import it.palex.attendanceManagement.commons.security.FailedAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {  
    
	@Value("#{'${security.cors.allowed_headers}'.split(',')}") 
    private List<String> allowedHeaders;
    
    @Value("#{'${security.cors.exposed_headers}'.split(',')}") 
    private List<String> exposedHeaders;
    
    @Value("#{'${security.cors.allowed_origins}'.split(',')}") 
    private List<String> allowedOrigins;
    
    @Value("#{'${security.cors.allowed_methods}'.split(',')}") 
    private List<String> allowedMethods;
    
    @Value("${security.cors.allow_credentials}")
    private boolean allowCredentials;
    
    @Value("${security.cors.max_age}")
    private Long maxAge;
    
    @Autowired
    private FailedAuthenticationEntryPoint unauthorizedHandler;
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest().permitAll();
        

        // Custom JWT based security filter
        httpSecurity.addFilterBefore(authenticationFilterBean(), UsernamePasswordAuthenticationFilter.class);

        
        httpSecurity
    	// we don't need CSRF because our token is not vulnerable
        .csrf().disable()
        	.cors().configurationSource(corsConfigurationSource());
        
        // disable page caching
        httpSecurity.headers()
                .frameOptions().sameOrigin()
                .cacheControl();
        
    }
    
    @Bean
    public CustomAuthenticationTokenFilter authenticationFilterBean() throws Exception {
        return new CustomAuthenticationTokenFilter();
    }
    
    
    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setExposedHeaders(exposedHeaders);
        if(allowedOrigins.contains("*")){
            configuration.addAllowedOriginPattern("*");
        }else{
            configuration.setAllowedOrigins(allowedOrigins);
        }

        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowCredentials(allowCredentials);
        configuration.setMaxAge(maxAge);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    
}
