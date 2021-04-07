package it.palex.attendanceManagement.commons.utils.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.ApplicationVersionService;


@Aspect
@Component
public class ApplicationVersionAdderToGenericResponse {

	//private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ApplicationVersionAdderToGenericResponse.class); 

	@Autowired
	private ApplicationVersionService versionSrv;
	
	
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void controller() {
	}

	@Pointcut("execution(* *.*(..))")
	protected void allMethod() {
	}

//	@Pointcut("execution(public * *(..))")
//	protected void loggingPublicOperation() {
//	}
//
//	@Pointcut("execution(* *.*(..))")
//	protected void loggingAllOperation() {
//	}
//
//	@Pointcut("within(org.learn.log..*)")
//	private void logAnyFunctionWithinResource() {
//	}

	@Around("controller() && allMethod()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		
		if (joinPoint != null) {
			Object obj = null;
			
			try {
				obj = joinPoint.proceed();
			}catch(Exception e) {
				throw e;
			}
			
			if (obj != null) {
				if (obj instanceof ResponseEntity) {
					Object res = ((ResponseEntity<?>) obj).getBody();
					if(res!=null && res instanceof GenericResponse) {
						GenericResponse<?> park = ((GenericResponse<?>) res);
						if(park!=null) {
							park.setVersion(versionSrv.getVersion());
						}
					}
				}
			}
		
			return obj;
		} 
		
		return null;
	}

}
