package it.palex.attendanceManagement.data.permissionEvaluators.chain;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

public class AuthoritiesChecker {

	
	public static boolean hasAuthority(Collection<? extends GrantedAuthority> authorities,
			 String authority) {
		if(authorities==null) {
			return false;
		}
		Iterator<? extends GrantedAuthority> it = authorities.iterator();
		
		while(it.hasNext()) {
			GrantedAuthority park = it.next();
			if(StringUtils.equals(park.getAuthority(), authority)) {
				return true;
			}
		}
		return false;
	}
}
