package it.palex.attendanceManagement.data.aspects;

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
public @interface RollbackTransactionForReturnCodes {
	
	int[] rollbackForCodes() default {};
}
