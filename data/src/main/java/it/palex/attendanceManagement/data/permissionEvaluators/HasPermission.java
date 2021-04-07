package it.palex.attendanceManagement.data.permissionEvaluators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Alessandro Pagliaro
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermission {
	
    String identifierParamName();
    String permission();
    String targetObject();
    
}

