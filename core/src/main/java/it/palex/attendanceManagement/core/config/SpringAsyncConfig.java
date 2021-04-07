package it.palex.attendanceManagement.core.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import it.palex.attendanceManagement.commons.exeptions.CustomAsyncExceptionHandler;
import it.palex.attendanceManagement.data.exceptions.InvalidConfigurationException;

@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {
	
	@Value("${spring-async.core-pool-size}")
	private int corePoolSize=-1;
	
	@Value("${spring-async.max-pool-size}")
	private int maxPoolSize=-1;
	
	@Value("${spring-async.queue-capacity}")
	private int queueCapacity=-1;
     
    @Override
    public Executor getAsyncExecutor() {
    	
    	if(corePoolSize<0 || maxPoolSize<0 || queueCapacity<0) {
    		throw new InvalidConfigurationException();
    	}
    	
    	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
  	    executor.setCorePoolSize(corePoolSize);
  	    executor.setMaxPoolSize(maxPoolSize);

  	    executor.setWaitForTasksToCompleteOnShutdown(true);

  	    executor.setQueueCapacity(queueCapacity);
  	    executor.setThreadNamePrefix("cloudunit-Executor-");
  	    executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }
     
}
