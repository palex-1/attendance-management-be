package it.palex.attendanceManagement.data.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Alessandro Pagliaro
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "it.palex.attendanceManagement.data", 
    entityManagerFactoryRef = "sqlEntityManager", 
    transactionManagerRef = "sqlTransactionManager"
)
public class PersistenceConfiguration {

	@Autowired
    private Environment env;
	
	@Bean(name="sqlDataSource")
    public DataSource sqlDataSource() {
	      DriverManagerDataSource dataSource = new DriverManagerDataSource();
	      
	      dataSource.setDriverClassName(env.getProperty("persistence.datasource.driver-class-name"));
	      dataSource.setUrl(env.getProperty("persistence.datasource.url"));
	      dataSource.setUsername(env.getProperty("persistence.datasource.username"));
	      dataSource.setPassword(env.getProperty("persistence.datasource.password"));
	      dataSource.setSchema(env.getProperty("persistence.hibernate.default_schema"));
	      
	      return dataSource;
    }
	
	 @Bean(name = "sqlTransactionManager")
     @Primary /* Add this annotation to make spring use this transaction manager 
      			for @Transactional annotation if you do not do this a double definition of 
      			bean error is thrown and you need to add the name in @Transactional(name="sqlTransactionManager") */
     public JpaTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
           JpaTransactionManager tm = new JpaTransactionManager();
           tm.setEntityManagerFactory(sqlEntityManagerFactory(builder).getObject());
           tm.setDataSource(sqlDataSource());
           
           return tm;
     }
	
	@PersistenceContext(unitName = "SQL-PU")
    @Bean(name = "sqlEntityManager")
    public LocalContainerEntityManagerFactoryBean sqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
         return  builder.dataSource(sqlDataSource())
        		 .properties(jpaProperties())
        		 .persistenceUnit("SQL-PU")
        		 .packages("it.palex.attendanceManagement.data.entities").build();
     }
	
	private Map<String, Object> jpaProperties() {
        HashMap<String, Object> properties = new HashMap<>();
        
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("persistence.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("persistence.hibernate.dialect"));
        properties.put("hibernate.default_schema", env.getProperty("persistence.hibernate.default_schema"));
        properties.put("hibernate.show_sql", env.getProperty("persistence.hibernate.show_sql"));
        properties.put("hibernate.format_sql", env.getProperty("persistence.hibernate.format_sql"));
        //properties.put("org.hibernate.flushMode", "COMMIT");
        
        properties.put("hibernate.jdbc.lob.non_contextual_creation", env.getProperty("persistence.hibernate.jdbc.lob.non_contextual_creation"));
        
        return properties;
  }
	
}
