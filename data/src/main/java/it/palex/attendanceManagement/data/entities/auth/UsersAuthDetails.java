package it.palex.attendanceManagement.data.entities.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.palex.attendanceManagement.data.entities.UserContacts;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthProvidersEnum;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author palex
 */
@Entity
@Table(name = "users_auth_details")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsersAuthDetails.findById", query = "SELECT u FROM UsersAuthDetails u WHERE u.id = :id"),
    @NamedQuery(name = "UsersAuthDetails.findByUsername", query = "SELECT u FROM UsersAuthDetails u WHERE u.username = :username")
})
public class UsersAuthDetails implements Serializable, UserDetails, DatabaseCheckableEntity {
	
    private static final long serialVersionUID = 4767677938945546L;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    public static final int MIN_USERNAME_SIZE = 3;
    public static final int MAX_USERNAME_SIZE = 255;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_USERNAME_SIZE, max = MAX_USERNAME_SIZE)
    @Column(name = "username", unique = true)
    private String username;
    
    public static final int MIN_HASHED_PASSWORD_SIZE = 16;
    public static final int MAX_HASHED_PASSWORD_SIZE = 128;
    
    
    @Size(min = MIN_HASHED_PASSWORD_SIZE, max = MAX_HASHED_PASSWORD_SIZE)
    @Column(name = "hashed_password")
    private String hashedPassword;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "last_password_change_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordChangeDate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ISENABLED")
    private boolean isEnabled;  
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ISACCOUNTNONEXPIRED")
    private boolean isAccountNonExpired;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ISACCOUNTNONLOCKED")
    private boolean isAccountNonLocked;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ISCREDENTIALSNONEXPIRED")
    private boolean isCredentialsNonExpired;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "must_reset_password")
    private Boolean mustResetPassword = false;
    
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "two_fa_enabled")
    private Boolean twoFaEnabled;    
    
    public static final int PERMISSION_GROUP_MAX_SIZE = 50;
    
    @JoinColumn(name = "permission_group_name", referencedColumnName = "name")
    @ManyToOne
    private PermissionGroupLabel permissionGroupName;
    
    public static final int MIN_REGISTERED_WITH_SIZE = 1;
    public static final int MAX_REGISTERED_WITH_SIZE = 20;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_REGISTERED_WITH_SIZE, max = MAX_REGISTERED_WITH_SIZE)
    @Column(name = "REGISTERED_WITH")
    private String registeredWith = AuthProvidersEnum.LOCAL.name();
    
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usersAuthDetails")
    private List<SuccessfullyLoginLogs> successfullyLoginLogsList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usersAuthDetails")
    private List<UserPasswordChangeHistory> userPasswordChangeHistoryList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkIdUsersAuthDetails", fetch = FetchType.EAGER)
    private List<Authorities> authoritiesList;
    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usersAuthDetails")
    private UserProfile userProfile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkIdUsersAuthDetails")
    private List<UserContacts> userContactsList;
    
    
    
    public UsersAuthDetails() {
    }


	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidUsername(this.username) && isValidPassword(this.hashedPassword)
				&& isValidLastPasswordChangeDate(this.lastPasswordChangeDate) 
				&& isValidIsEnabled(this.isEnabled) && isValidIsAccountNonExpired(this.isAccountNonExpired)
				&& isValidIsAccountNonLocked(this.isAccountNonLocked)
				&& isValidIsCredentialsNonExpired(this.isCredentialsNonExpired)
				&& isValidTwoFaEnabled(this.twoFaEnabled)
				&& isValidPermissionGroup(this.permissionGroupName)
				&& isValidMustResetPassword(this.mustResetPassword)
				&& isValidRegisteredWith(this.registeredWith);

		return isValid;
	}
	

	@Override
	public String whyCannotBeInsertedInDatabase() {
		final String why = "username:"+this.username+", hashedPassword:"+ this.hashedPassword+", lastPasswordChangeDate:"+ this.lastPasswordChangeDate
				+", isEnabled:"+ this.isEnabled+", isAccountNonExpired:"+ this.isAccountNonExpired+", isAccountNonLocked:"+ this.isAccountNonLocked
				+", isCredentialsNonExpired:"+ this.isCredentialsNonExpired+", twoFaEnabled:"+ this.twoFaEnabled
				+", permissionGroupName:"+this.permissionGroupName+", mustResetPassword:"+this.mustResetPassword
				+", registeredWith:"+this.registeredWith;
		
		return why;
	}


	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    

	public static boolean isValidUsername(String username) {
    	if(username==null) {
    		return false;
    	}
    	if( username.length() < MIN_USERNAME_SIZE ||  username.length() > MAX_USERNAME_SIZE){
			return false;
		}
    	return true;
    }
	
	public Boolean getMustResetPassword() {
		return mustResetPassword;
	}
    
	public void setMustResetPassword(Boolean mustResetPassword) {
		this.mustResetPassword = mustResetPassword;
	}
	
	private boolean isValidMustResetPassword(Boolean mustResetPassword) {
		if(mustResetPassword==null) {
			return false;
		}
		return true;
	}
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    
    public static boolean isValidPassword(String password) {
    	if(password==null) {
    		return true;
    	}
    	if( password.length() < MIN_HASHED_PASSWORD_SIZE ||  password.length() > MAX_HASHED_PASSWORD_SIZE){
			return false;
		}
    	return true;
    }

    public Date getLastPasswordChangeDate() {
        return lastPasswordChangeDate;
    }

    public void setLastPasswordChangeDate(Date lastPasswordChangeDate) {
        this.lastPasswordChangeDate = lastPasswordChangeDate;
    }
    
    public static boolean isValidLastPasswordChangeDate(Date lastPasswordChangeDate) {
    	return lastPasswordChangeDate!=null;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public String getRegisteredWith() {
		return registeredWith;
	}

	public void setRegisteredWith(String registeredWith) {
		this.registeredWith = registeredWith;
	}
	
	public static boolean isValidRegisteredWith(String registeredWith) {
		if(registeredWith==null) {
    		return false;
    	}
    	if(registeredWith.length() < MIN_REGISTERED_WITH_SIZE ||  registeredWith.length() > MAX_REGISTERED_WITH_SIZE){
			return false;
		}
    	return true;
	}
	
    @Override
	public boolean isEnabled() {
		return this.isEnabled;
	}
    
    public static boolean isValidIsEnabled(Boolean isEnabled) {
    	return isEnabled!=null;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setIsAccountNonExpired(Boolean isAccountNonExpired) {
        this.isAccountNonExpired = isAccountNonExpired;
    }
    
    public static boolean isValidIsAccountNonExpired(Boolean isAccountNonExpired) {
    	return isAccountNonExpired!=null;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setIsAccountNonLocked(Boolean isAccountNonLocked) {
        this.isAccountNonLocked = isAccountNonLocked;
    }
    
    public static boolean isValidIsAccountNonLocked(Boolean isAccountNonLocked) {
    	return isAccountNonLocked!=null;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setIsCredentialsNonExpired(Boolean isCredentialsNonExpired) {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }

	public UserProfile getUserProfile() {
		return userProfile;
	}


	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}


	public static boolean isValidIsCredentialsNonExpired(Boolean isCredentialsNonExpired) {
    	return isCredentialsNonExpired!=null;
    }
    
    @Override
	public String getPassword() {
		return this.getHashedPassword();
	}

    @Override
   	public Collection<? extends GrantedAuthority> getAuthorities() {
   		Collection<GrantedAuthority> grantedAuths = new ArrayList<>();
   		
   		if(this.getAuthoritiesList()!=null){
   			for(Authorities auth: this.getAuthoritiesList()){
   		    	grantedAuths.add(
   		    			new SimpleGrantedAuthority(
   		    					auth.getAuthority().getAuthority()
   		    					)
   		    			);
   		    }
   		}
   	    return grantedAuths;
   	}
    
    public boolean getTwoFaEnabled() {
        return twoFaEnabled;
    }

    public void setTwoFaEnabled(Boolean twoFaEnabled) {
        this.twoFaEnabled = twoFaEnabled;
    }
    
    public static boolean isValidTwoFaEnabled(Boolean twoFaEnabled) {
    	return twoFaEnabled!=null;
    }
   
    public static boolean isValidIsChangePasswordRequired(Boolean isChangePasswordRequired) {
    	return isChangePasswordRequired!=null;
    }

    
    public PermissionGroupLabel getPermissionGroup() {
		return permissionGroupName;
	}
    
	public void setPermissionGroup(PermissionGroupLabel permissionGroup) {
		this.permissionGroupName = permissionGroup;
	}
	
	public static boolean isValidPermissionGroup(PermissionGroupLabel permissionGroupName) {
		if(permissionGroupName==null) {
			return false;
		}		
		return true;
	}
    
    @XmlTransient
    public List<SuccessfullyLoginLogs> getSuccessfullyLoginLogsList() {
        return successfullyLoginLogsList;
    }

    public void setSuccessfullyLoginLogsList(List<SuccessfullyLoginLogs> successfullyLoginLogsList) {
        this.successfullyLoginLogsList = successfullyLoginLogsList;
    }

    @XmlTransient
    public List<UserPasswordChangeHistory> getUserPasswordChangeHistoryList() {
        return userPasswordChangeHistoryList;
    }

    public void setUserPasswordChangeHistoryList(List<UserPasswordChangeHistory> userPasswordChangeHistoryList) {
        this.userPasswordChangeHistoryList = userPasswordChangeHistoryList;
    }

    @XmlTransient
    public List<Authorities> getAuthoritiesList() {
        return authoritiesList;
    }

    public void setAuthoritiesList(List<Authorities> authoritiesList) {
        this.authoritiesList = authoritiesList;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersAuthDetails)) {
            return false;
        }
        UsersAuthDetails other = (UsersAuthDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.UsersAuthDetails[ id=" + id + " ]";
    }
    
}
