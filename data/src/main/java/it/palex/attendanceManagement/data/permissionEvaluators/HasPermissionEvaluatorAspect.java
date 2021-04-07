package it.palex.attendanceManagement.data.permissionEvaluators;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.library.exception.ForbiddenException;


/**
 * @author Alessandro Pagliaro
 *
 */
@Aspect
@Component
public class HasPermissionEvaluatorAspect {

	@Autowired
	private ChainPermissionEvaluator permissionEvaluator;
		
	
	@Around("@annotation(it.palex.attendanceManagement.data.permissionEvaluators.HasPermission)")
	public Object hasPermission(ProceedingJoinPoint joinPoint)
			throws Throwable {
		if (joinPoint != null) {
			HasPermission annotation = getLoggableAnnotation(joinPoint);
			
			Object identifier = findIdentifierParameter(joinPoint, annotation.identifierParamName());
			
			Authentication auth = getCurrentAuthenticatedUser();
			
			if(!this.isAuthenticated(auth)) {
				throw new AccessDeniedException("Not Authenticated");
			}
			
			boolean hasPermission = permissionEvaluator.hasPermission(auth,
					identifier, annotation.targetObject(), annotation.permission());
			
			if(!hasPermission) {
				throw new ForbiddenException("User has no access to resource");
			}
			
			Object obj = joinPoint.proceed();
			
			return obj;
			
			
		}
		return null;
	}
	
	private boolean isAuthenticated(Authentication auth) {
		if(auth!=null) {
			if(auth.getName()=="anonymousUser") {
				return false;
			}
			return auth.isAuthenticated();
		}
		return false;
	}
	
	public static Authentication getCurrentAuthenticatedUser() {
		if(SecurityContextHolder.getContext()!=null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth!=null) {
				return auth;
			}
		}
		return null;
	}
	
	private Object findIdentifierParameter(ProceedingJoinPoint joinPoint, String paramName) {
		if(paramName!=null && joinPoint != null && joinPoint.getSignature()!=null
				&& joinPoint.getArgs()!=null) {
			CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
			if(codeSignature!=null) {
				String[] sigParamNames = codeSignature.getParameterNames(); 
				
				MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
				
				if(sigParamNames!=null) {
					Object[] args = joinPoint.getArgs();
				    for(int i=0;i<sigParamNames.length;i++) {
				    	if(StringUtils.equals(paramName, sigParamNames[i])) {
				    		return args[i];
				    	}
				    }
				}
			}
			
		}
		return null;
	}
	
	private HasPermission getLoggableAnnotation(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		HasPermission hasPermission = method.getAnnotation(HasPermission.class);
		return hasPermission;
	}
}
