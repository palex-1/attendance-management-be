package it.palex.attendanceManagement.commons.security;

import java.util.Collection;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.service.user.UsersAuthDetailsService;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Value("${security.login.bcrypt.streght}")
	private Integer bcryptStrenght;
	
	@Autowired
	private UsersAuthDetailsService authDetailsService;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		if(auth==null || auth.getCredentials()==null){
			throw new BadCredentialsException("Username or password are wrong!");
		}
		
		//getName() Returns the name of this principal.
		final String username = auth.getName();
		/*             auth.getCredentials()
		 * The credentials that prove the principal is correct. 
		 * This is usually a password, but could be anything relevant to the AuthenticationManager.
		 *  Callers are expected to populate the credentials.
		 */
	    final String passwordPlainText = auth.getCredentials().toString();
         /*
          * Il dbAuthService deve popolare l'authorithies dell'utente.
          */
	     UsersAuthDetails user = getUserDetailsByUsername(username);
	     
	     
	     if (user == null || passwordPlainText==null || user.getPassword()==null || !checkBCryptPassword(passwordPlainText, user.getPassword())) {
	    	 throw new BadCredentialsException("Username or password are wrong!");
         }
	     if(checkOtherPrerequisiteToLogin(user)){
	    	 throw new BadCredentialsException("Account currently deactivated");
	     }
   
          Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
          
          
          if(authorities==null){
        	  authorities = new LinkedList<>();
          }
          
          return new UsernamePasswordAuthenticationToken(user, passwordPlainText, authorities);
	}

	
	/**
	 * 
	 * @param username
	 * @return UserAuthDetails with the specified email, null will be returned if email is null
	 */
	private UsersAuthDetails getUserDetailsByUsername(String username){
		if(username==null){
			return null;
		}
		
		return authDetailsService.findByUsername(username); 
	}	
	
	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
	
	/**
	 * 
	 * @param plainTextPSW password in plain text
	 * @param hashedPSW hashed password with Bcrypt 
	 * @return true if hashedPSW is a valid BcryptHash(plainTextPSW)
	 */
	public boolean checkBCryptPassword(String plainTextPSW, String hashedPSW){
		if(plainTextPSW==null || hashedPSW==null ){
			throw new NullPointerException("Cannot apply bcrypt on null password");
		}
		return BCrypt.checkpw(plainTextPSW, hashedPSW);
	}
	
	private boolean checkOtherPrerequisiteToLogin(UserDetails user){
		return !user.isEnabled() || !user.isCredentialsNonExpired() ||
		!user.isAccountNonLocked() || !user.isAccountNonExpired();
	}
	
    
	public String hashPassword(String psw){
		PasswordEncoder crypter = new BCryptPasswordEncoder(this.bcryptStrenght);
		return crypter.encode(psw);
	}
	
	
	
}
