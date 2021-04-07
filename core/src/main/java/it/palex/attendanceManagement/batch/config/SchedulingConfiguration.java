package it.palex.attendanceManagement.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@PropertySource({ 
	  "classpath:cron.properties"
})
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer {

	@Value("${scheduling.pool-size:25}")
	private int schedulingPoolSize;
	
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(schedulingPoolSize);
		taskScheduler.initialize();
		taskRegistrar.setTaskScheduler(taskScheduler);
	}

}
