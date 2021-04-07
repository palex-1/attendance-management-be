package it.palex.attendanceManagement.library.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import it.palex.attendanceManagement.library.service.ApplicationVersionService;
import it.palex.attendanceManagement.library.utils.HttpCodes;

/**
*
* @author Alessandro Pagliaro
* 
*/
@ControllerAdvice
public class CustomExceptionHandler {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CustomExceptionHandler.class);
	
	@Autowired
	private ApplicationVersionService applicationVersionSrv;
	
	
	@ExceptionHandler(Throwable.class)
    public final ResponseEntity<GenericResponse<String>> handleAllExceptions(Exception ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.INTERNAL_SERVER_ERROR);
    	res.setData("");
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
    	res.setOperationIdentifier(null); //cannot identify the operation id
    	
    	LOGGER.error(ex.getMessage()+ " id:"+res.getOperationIdentifier(), ex);
    	
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public final ResponseEntity<GenericResponse<String>> handleAccessDeniedException(
    		org.springframework.security.access.AccessDeniedException ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.UNAUTHORIZED);
    	res.setData("");
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
        
        return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
    }
	
	@ExceptionHandler({org.springframework.data.mapping.PropertyReferenceException.class,
			org.springframework.http.converter.HttpMessageNotReadableException.class,
			org.springframework.web.bind.MissingPathVariableException.class,
			org.springframework.web.bind.ServletRequestBindingException.class,
			org.springframework.beans.TypeMismatchException.class,
			org.springframework.http.converter.HttpMessageNotReadableException.class,
			org.springframework.web.bind.MethodArgumentNotValidException.class,
			org.springframework.web.bind.MissingServletRequestParameterException.class,
			org.springframework.validation.BindException.class
	})
    public final ResponseEntity<GenericResponse<String>> handleBadRequest(Exception ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.BAD_REQUEST);
    	res.setData("");
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
    	res.setOperationIdentifier(null); //cannot identify the operation id
    	
    	LOGGER.warn(ex.getMessage()+ " id:"+res.getOperationIdentifier(), ex);
    	
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
	public final ResponseEntity<GenericResponse<String>> handleNotFound(Exception ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.NOT_FOUND);
    	res.setData("");
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
    	res.setOperationIdentifier(null); //cannot identify the operation id
    	
    	LOGGER.warn(ex.getMessage()+ " id:"+res.getOperationIdentifier(), ex);
    	
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
	public final ResponseEntity<GenericResponse<String>> handleMediaTypeNotSupported(Exception ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.UNSUPPORTED_MEDIA_TYPE);
    	res.setData("");
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
    	res.setOperationIdentifier(null); //cannot identify the operation id
    	
    	LOGGER.warn(ex.getMessage()+ " id:"+res.getOperationIdentifier(), ex);
    	
        return new ResponseEntity<>(res, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
	
	
	@ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
	public final ResponseEntity<GenericResponse<String>> handleMethodNotSupportedException(Exception ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.METHOD_NOT_ALLOWED);
    	res.setData("");
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
    	res.setOperationIdentifier(null); //cannot identify the operation id
    	
    	LOGGER.warn(ex.getMessage()+ " id:"+res.getOperationIdentifier(), ex);
    	
        return new ResponseEntity<>(res, HttpStatus.METHOD_NOT_ALLOWED);
    }
	
	
	@ExceptionHandler(org.springframework.web.HttpMediaTypeNotAcceptableException.class)
	public final ResponseEntity<GenericResponse<String>> handleMediaTypeNotAcceptableException(Exception ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.NOT_ACCEPTABLE);
    	res.setData("");
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
    	res.setOperationIdentifier(null); //cannot identify the operation id
    	
    	LOGGER.warn(ex.getMessage()+ " id:"+res.getOperationIdentifier(), ex);
    	
        return new ResponseEntity<>(res, HttpStatus.NOT_ACCEPTABLE);
    }
	
	
	@ExceptionHandler(it.palex.attendanceManagement.library.exception.ForbiddenException.class)
    public final ResponseEntity<GenericResponse<String>> handleForbiddenException(
    		it.palex.attendanceManagement.library.exception.ForbiddenException ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.FORBIDDEN);
    	res.setOperationIdentifier(ex.getOperationUUID());
    	res.setSubcode(ex.getCode());
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
        
        return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
    }
	
	@ExceptionHandler(it.palex.attendanceManagement.library.exception.BadDataException.class)
    public final ResponseEntity<GenericResponse<String>> handleBadDataException(
    		it.palex.attendanceManagement.library.exception.BadDataException ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.BAD_REQUEST);
    	res.setOperationIdentifier(ex.getOperationUUID());
    	res.setSubcode(ex.getCode());
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
        
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(it.palex.attendanceManagement.library.exception.InternalServerErrorException.class)
	public final ResponseEntity<GenericResponse<String>> handleInternalServerException(
    		it.palex.attendanceManagement.library.exception.InternalServerErrorException ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.INTERNAL_SERVER_ERROR);
    	res.setOperationIdentifier(ex.getOperationUUID());
    	res.setSubcode(ex.getCode());
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
        
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler(it.palex.attendanceManagement.library.exception.NotAcceptableException.class)
	public final ResponseEntity<GenericResponse<String>> handleNotAcceptableException(
    		it.palex.attendanceManagement.library.exception.NotAcceptableException ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.NOT_ACCEPTABLE);
    	res.setOperationIdentifier(ex.getOperationUUID());
    	res.setSubcode(ex.getCode());
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
        
        return new ResponseEntity<>(res, HttpStatus.NOT_ACCEPTABLE);
    }
	
	@ExceptionHandler(it.palex.attendanceManagement.library.exception.NotFoundException.class)
	public final ResponseEntity<GenericResponse<String>> handleNotFoundException(
    		it.palex.attendanceManagement.library.exception.NotFoundException ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.NOT_FOUND);
    	res.setOperationIdentifier(ex.getOperationUUID());
    	res.setSubcode(ex.getCode());
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
        
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(it.palex.attendanceManagement.library.exception.UnauthorizedException.class)
	public final ResponseEntity<GenericResponse<String>> handleUnauthorizedException(
    		it.palex.attendanceManagement.library.exception.UnauthorizedException ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.UNAUTHORIZED);
    	res.setOperationIdentifier(ex.getOperationUUID());
    	res.setSubcode(ex.getCode());
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
        
        return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
    }
	
	@ExceptionHandler(it.palex.attendanceManagement.library.exception.UnprocessableEntityException.class)
	public final ResponseEntity<GenericResponse<String>> handleUnprocessableEntityException(
    		it.palex.attendanceManagement.library.exception.UnprocessableEntityException ex, WebRequest request){
    	GenericResponse<String> res = new GenericResponse<String>();
    	res.setCode(HttpCodes.UNPROCESSABLE_ENTITY);
    	res.setOperationIdentifier(ex.getOperationUUID());
    	res.setSubcode(ex.getCode());
    	res.setMessage(ex.getMessage());
    	res.setVersion(applicationVersionSrv.getVersion());
        
        return new ResponseEntity<>(res, HttpStatus.UNPROCESSABLE_ENTITY);
    }
	
}
