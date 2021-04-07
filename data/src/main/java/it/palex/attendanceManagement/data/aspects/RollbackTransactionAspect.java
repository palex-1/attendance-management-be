package it.palex.attendanceManagement.data.aspects;

import java.lang.reflect.Method;  

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.palex.attendanceManagement.library.rest.GenericResponse;

import org.aspectj.lang.reflect.MethodSignature;


/**
 * @author Alessandro Pagliaro
 *
 */
@Aspect
@Component
public class RollbackTransactionAspect {
	
	
	@Around("@annotation(it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes)")
	public Object rollbackForReturnCodes(ProceedingJoinPoint joinPoint)
			throws Throwable {
		if (joinPoint != null) {
			Object obj = joinPoint.proceed();

			RollbackTransactionForReturnCodes annotation = getLoggableAnnotation(joinPoint);
			if (obj != null && annotation != null) {
				if (obj instanceof GenericResponse) {
					GenericResponse<?> res = ((GenericResponse<?>) obj);
					int code = res.getCode();
					this.rollbackFor(code, annotation);
				}
			}
			return obj;
		}
		return null;
	}
	
	private RollbackTransactionForReturnCodes getLoggableAnnotation(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		RollbackTransactionForReturnCodes loggable = method.getAnnotation(RollbackTransactionForReturnCodes.class);
		return loggable;
	}
	
	private void rollbackFor(int code, RollbackTransactionForReturnCodes annotation) {
		if(annotation==null || annotation.rollbackForCodes()==null) {
			return;
		}
		int[] codes= annotation.rollbackForCodes();
		
		for(int i=0; i<codes.length; i++) {
			if(codes[i]==code) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
		
	}
	
}

