package it.palex.attendanceManagement.library.utils.aspects;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.palex.attendanceManagement.library.exception.CustomHttpException;
import it.palex.attendanceManagement.library.exception.InternalServerErrorException;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.utils.HttpCodes;

/**
 * @author Alessandro Pagliaro
 *
 */
@Aspect
@Component
public class LoggableAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggableAspect.class); 
	
	@Around("@annotation(it.palex.attendanceManagement.library.utils.aspects.Loggable)")
	public Object rollbackForReturnCodes(ProceedingJoinPoint joinPoint)
			throws Throwable {
		
		if (joinPoint != null) {
		
			LogForStart logStart = new LogForStart();
			
			String target = "null";
			String aspectOperationId = UUID.randomUUID().toString();
			
			logStart.setAspectUUID(aspectOperationId);
			
			if(joinPoint.getTarget()!=null && joinPoint.getTarget().getClass()!=null &&
					joinPoint.getTarget().getClass().getName()!=null) {
				target =  joinPoint.getTarget().getClass().getName();
			}
			
			if(joinPoint.getSignature()!=null && joinPoint.getSignature().getName()!=null) {
				target = target + " - " +joinPoint.getSignature().getName();
			}

			List<Object> arguments = new LinkedList<Object>();
			Loggable annotation = getLoggableAnnotation(joinPoint);
			
			long start = 0;
			if(annotation.logExecutionTime()) {
				 start = System.currentTimeMillis();
			}
			if(annotation.logParameters()) {
				arguments = Arrays.asList(joinPoint.getArgs());
				logStart.setInputParams(arguments);
			}
			
			logStart.setTarget(target);
			
			String logStringStart = getStringValue(logStart);
			
			LOGGER.info(logStringStart);
			
			Object obj = null;
			
			try {
				obj = joinPoint.proceed();
			}catch(CustomHttpException e) {
				if(e!=null) {
					e.setOperationUUID(aspectOperationId);
				}
				throw e;
			}
			catch(Exception e) {
				LOGGER.error(logStringStart, e);
				InternalServerErrorException ex = new InternalServerErrorException(e);
				ex.setOperationUUID(aspectOperationId);
				
				throw ex;
			}

			
			String methodOperationId = aspectOperationId;
			if (obj != null && annotation != null) {
				if (obj instanceof ResponseEntity) {
					Object res = ((ResponseEntity<?>) obj).getBody();
					if(res!=null && res instanceof GenericResponse) {
						GenericResponse<?> park = ((GenericResponse<?>) res);
						methodOperationId = park.getOperationIdentifier();
						if(park.getCode()==HttpCodes.INTERNAL_SERVER_ERROR) {
							LOGGER.error(park.getMessage());
						}
					}
				}
			}
			
			Object responseParams = null;
			LogForEnd logForEnd = new LogForEnd();
				
			logForEnd.setOperationId(methodOperationId);
			logForEnd.setAspectUUID(aspectOperationId);
			
			if(annotation.logResponseParameter()) {
				responseParams = obj;
				logForEnd.setOutputParams(responseParams);
			}
			
			if(annotation.logExecutionTime()) {
				long executionTime = System.currentTimeMillis() - start;
				logForEnd.setExecutionTimeMilliseconds(executionTime);
			}
			
			logForEnd.setTarget(target);
			String logStringEnd = getStringValue(logForEnd);
			
			LOGGER.info(logStringEnd);
			
			return obj;
		}
		return null;
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
	private Loggable getLoggableAnnotation(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		
		Loggable loggable = method.getAnnotation(Loggable.class);
		
		return loggable;
	}
		
}
