package it.palex.attendanceManagement.commons.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import it.palex.attendanceManagement.data.entities.auth.UsersAccessToken;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.service.auth.UserAccessTokenService;
import it.palex.attendanceManagement.data.service.user.UsersAuthDetailsService;
import it.palex.attendanceManagement.library.utils.DateUtility;

public class CustomAuthenticationTokenFilter extends OncePerRequestFilter {

	@Autowired
    private UsersAuthDetailsService userAuthDetailsService;
	
	@Autowired
	private UserAccessTokenService tokenService;
	
	@Value("${security.auth.header}")
    private String tokenHeader;
	
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
    
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
		
	    	this.populateSecurityContextHolder(request, response);
    	
	        try{
	        	
	        	filterChain.doFilter(request, response);
	        	
	        }catch(javax.servlet.ServletException e){ 
	        	if( e.getCause().getCause() instanceof java.io.EOFException ){
	        		/* This exception can happen if the client close the connection during a transfer es--> Unexpected EOF read on the socket*/
	            	logger.warn("This is an UnexpectedEOF exception could be ignored in most case. This happen "
	            			+ "if the user refresh the page during a file upload: ", e);
	        	}else{
	        		throw e; //rethrwo exception if check fail checks
	        	}
	        }
        
	    }catch(Exception e){
	    	logger.error(e);
	    	throw new AuthenticationServiceException("Internal error occurred");
	    }
		
	}
	
	
	private void populateSecurityContextHolder(HttpServletRequest request, HttpServletResponse response) {
		String headerTokenValue = request.getHeader(this.tokenHeader);
    	
    	String authToken = extractTokenFromHeader(headerTokenValue);
    	UsersAccessToken accessToken = null;
    	
    	boolean isValidAccessToken = false;
    	
    	try {
    		if(authToken==null){
        		logger.debug("couldn't find bearer string, will ignore the header");
        	}else {
        		accessToken = this.getUsersAccessTokenFromToken(authToken);
        		isValidAccessToken = validateToken(accessToken);
        	}
    	}catch(Exception e) {
    		isValidAccessToken = false;
    	}
    	
    	if(isValidAccessToken) {
    		
    		boolean isValidSignature = this.jwtTokenUtil.validateToken(authToken, accessToken.getFkIdUsersAuthDetails());
    		if(isValidSignature) {
    			Integer id = accessToken.getFkIdUsersAuthDetails().getId();
        		
        		UserDetails userDetails = this.getUserByID(id);
        		
        		if(userDetails!=null && userDetails.isAccountNonExpired() 
        			&& userDetails.isAccountNonLocked()
                		&& userDetails.isCredentialsNonExpired() && userDetails.isEnabled()) {
        			
        			
        			Collection<? extends GrantedAuthority> permissions = null;
        			
        			permissions = userDetails.getAuthorities();
        			
        			boolean isTwoFAInProgress = accessToken.isTwoFaInProgress();
        			
        			if(isTwoFAInProgress) {
        				List<GrantedAuthority> park = new LinkedList<GrantedAuthority>();
        				park.add(new SimpleGrantedAuthority(AuthenticationIntermediateRoles.TWO_FA_IN_PROGRESS));
        				
        				permissions = park;
        			}
        				
        			//if is not two fa in progress check if user must change password
    				if(!isTwoFAInProgress && BooleanUtils.isTrue(accessToken.getMustResetPassword())) {
    					List<GrantedAuthority> park = new LinkedList<GrantedAuthority>();
        				park.add(new SimpleGrantedAuthority(AuthenticationIntermediateRoles.USER_MUST_CHANGE_PASSWORD));
        				
        				permissions = park;
    				}
        			
        			
        			
        			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, permissions);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    logger.debug("authenticated user " + id + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }	
    		}
    	}
	}
	
	private boolean validateToken(UsersAccessToken accessToken) {
		if(accessToken==null) {
			return false;
		}
		if(isCreatedBeforeLastPasswordReset(accessToken, accessToken.getFkIdUsersAuthDetails())) {
			return false;
		}
		if(isExpired(accessToken)) {
			return false;
		}
		
		return true;
	}
	
	
	private boolean isExpired(UsersAccessToken accessToken) {
		if(accessToken==null ) {
			return false;
		}
		Date expirationDate = accessToken.getExpirationDate();
		
		return DateUtility.getCurrentDateInUTC().after(expirationDate);
	}


	private boolean isCreatedBeforeLastPasswordReset(UsersAccessToken accessToken, 
			UsersAuthDetails usersAuthDetails) {
		if(accessToken==null || usersAuthDetails==null) {
			return false;
		}
		if(accessToken.getIssuedDate()==null) {
			return false;
		}
		return accessToken.getIssuedDate().before(usersAuthDetails.getLastPasswordChangeDate());
	}


	private UsersAccessToken getUsersAccessTokenFromToken(String authToken) {
		UsersAccessToken userAccessToken = this.tokenService.findByToken(authToken);
		
		return userAccessToken;
	}

	/**
	 * 
	 * @param headerTokenValue
	 * @return the value of token without Bearer initial string and null if <strong>headerTokenValue</strong> is null
	 */
	public static String extractTokenFromHeader(String headerTokenValue) {
		if(headerTokenValue!=null){
    		if(headerTokenValue.startsWith("Bearer ")) {
    			return headerTokenValue.substring(7);
    		}
    	}
		return  null;
	}
	
	private UserDetails getUserByID(Integer id){
		if(id==null){
			return null;
		}
		
		return userAuthDetailsService.getByID(id);
	}
	
}
