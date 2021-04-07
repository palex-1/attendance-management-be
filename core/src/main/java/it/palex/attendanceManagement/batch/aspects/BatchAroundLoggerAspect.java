package it.palex.attendanceManagement.batch.aspects;

import java.lang.reflect.Method;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.palex.attendanceManagement.library.utils.aspects.CustomLog;
import it.palex.attendanceManagement.library.utils.aspects.LogForEnd;
import it.palex.attendanceManagement.library.utils.aspects.LogForFailed;
import it.palex.attendanceManagement.library.utils.aspects.LogForStart;


/**
 * @author Alessandro Pagliaro
 *
 */
@Aspect
@Component
public class BatchAroundLoggerAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchAroundLoggerAspect.class); 

	
	@Around("@annotation(it.palex.attendanceManagement.batch.aspects.BatchAroundLogger)")
	public Object batchLog(ProceedingJoinPoint joinPoint)
			throws Throwable {
		if (joinPoint != null) {
			Object obj = null;
					
			LogForStart logStart = new LogForStart();
			long start = System.currentTimeMillis();
			
			String aspectOperationId = UUID.randomUUID().toString();
			logStart.setAspectUUID(aspectOperationId);
						
			BatchAroundLogger annotation = getAnnotation(joinPoint);
			
			String target = extractTarget(annotation, joinPoint);
			logStart.setTarget(target);

			LOGGER.info("BATCH: " + getStringValue(logStart));

			
			try {
				obj = joinPoint.proceed();
			}catch(Exception e) {
				LogForFailed logForFailed = new LogForFailed(aspectOperationId, System.currentTimeMillis() - start,
						target);
				
				LOGGER.error("BATCH: " + getStringValue(logForFailed) + " FAILED ", e);
				
				this.manageException(e);
			}

			LogForEnd logForEnd = new LogForEnd();
			logForEnd.setAspectUUID(aspectOperationId);
			logForEnd.setExecutionTimeMilliseconds(System.currentTimeMillis() - start);
			logForEnd.setTarget(target);
			
			LOGGER.info("BATCH: " + getStringValue(logForEnd) + " COMPLETED");
			
			return obj;
		}
		return null;
	}
	
	private String extractTarget(BatchAroundLogger annotation, ProceedingJoinPoint joinPoint) {
		String target = annotation==null ? null : annotation.batchName();
		
		if(target==null) {
			if(joinPoint.getTarget()!=null && joinPoint.getTarget().getClass()!=null &&
					joinPoint.getTarget().getClass().getName()!=null) {
				target =  joinPoint.getTarget().getClass().getName();
			}
		}
		
		return target;
	}
	
	
	private BatchAroundLogger getAnnotation(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		BatchAroundLogger loggable = method.getAnnotation(BatchAroundLogger.class);
		
		return loggable;
	}
	
	
	private void manageException(Exception e) {
		
	}
	
	private String getStringValue(CustomLog log) {
		if(log==null) {
			return "null";
		}
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(log);
			
		}catch(Exception e) {
			return log.toString();
		}
		
	}
}


