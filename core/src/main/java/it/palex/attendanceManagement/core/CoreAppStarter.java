//package it.palex.attendanceManagement.core;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
//@SpringBootApplication
//@EnableEurekaClient
//@ComponentScan({"it.palex.*"})
//public class CoreAppStarter extends SpringBootServletInitializer {
//	
//	public static void main(String[] args) throws Exception {
//		SpringApplication.run(CoreAppStarter.class, args);
//	}
//    
//	@Bean
//    public static PropertySourcesPlaceholderConfigurer properties() {
//      PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
//      propertySourcesPlaceholderConfigurer.setNullValue("@null");
//      
//      return propertySourcesPlaceholderConfigurer;
//    }
//	
//}
