package it.palex.attendanceManagement.commons.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author Alessandro Pagliaro
 *
 */
@Component
public class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FailedAuthenticationEntryPoint.class);

	
    private static final long serialVersionUID = 345345435679027364L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
    	logger.debug("FailedAuthenticationEntryPoint called for a none authenticated request");
    	
    	//Questo entry point viene invocato quando si tenta di accedere ad una pagina protetta senza avere le credenziali
    	//in questo caso viene semplicemente inviato un messagio di Unauthorized 401
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
    
    
}

