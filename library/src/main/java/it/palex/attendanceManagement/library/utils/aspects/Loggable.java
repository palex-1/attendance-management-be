package it.palex.attendanceManagement.library.utils.aspects;

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
public @interface Loggable {

	boolean logParameters() default false;
    boolean logExecutionTime() default true;
    boolean logResponseParameter() default false;
    
}
