package it.palex.attendanceManagement.data.entities.core;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import org.apache.commons.lang3.StringUtils;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "user_notification")
public class UserNotification extends AuditableEntity implements Serializable, DatabaseCheckableEntity {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    public static final int TITLE_MAX_SIZE = 256;
    
    @Size(max = TITLE_MAX_SIZE)
    @Column(name = "title")
    private String title;
    
    public static final int TEXT_MAX_SIZE = 4096;
    
    @Size(max = TEXT_MAX_SIZE)
    @Column(name = "text")
    private String text;
    
    public static final int TARGET_ID_MAX_SIZE = 255;
    
    @Size(max = TARGET_ID_MAX_SIZE)
    @Column(name = "target_id")
    private String targetId;
    
    @Size(max = TARGET_ID_MAX_SIZE)
    @Column(name = "target_sub_id")
    private String targetSubId;
    
    
    public static final int LANDING_PAGE_MAX_SIZE = 255;
    
    @Size(max = LANDING_PAGE_MAX_SIZE)
    @Column(name = "landing_page")
    private String landingPage;
    
    
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;

    
    
    
    public UserNotification() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidTitle(this.title) && isValidText(this.text) 
				&& isValidUserProfile(this.userProfile) 
				&& isValidLandingPage(this.landingPage)
				&& isValidTargetId(this.targetId)
				&& isValidTargetSubId(this.targetSubId);
		
		return isValid;
	}

    

    @Override
 	public String whyCannotBeInsertedInDatabase() {
 		final String why = "title:"+this.title+", text:"+ this.text+", userProfile:"+ this.userProfile
 				+", landingPage:"+ this.landingPage+", targetId:"+ this.targetId+", targetSubId:"+ this.targetSubId;
 		
 		return why;
 	}
	

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public static boolean isValidTargetId(String targetId) {
    	if(targetId==null) {
			return true;
		}
		if(targetId.length()>TARGET_ID_MAX_SIZE) {
			return false;
		}
		return true;
	}
	
	public static boolean isValidTargetSubId(String targetSubId) {
		if(targetSubId==null) {
			return true;
		}
		if(targetSubId.length()>TARGET_ID_MAX_SIZE) {
			return false;
		}
		return true;
	}

	public String getTargetSubId() {
		return targetSubId;
	}

	public void setTargetSubId(String targetSubId) {
		this.targetSubId = targetSubId;
	}
	
	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	public static boolean isValidLandingPage(String landingPage) {
		if(landingPage==null) {
			return true;
		}
		if(landingPage.length()>LANDING_PAGE_MAX_SIZE) {
			return false;
		}
		return true;
	}
	
	public static boolean isValidText(String text) {
		if(text==null) {
			return true;
		}
		if(text.length()>TEXT_MAX_SIZE) {
			return false;
		}
		return true;
	}

    public static boolean isValidTitle(String title) {
		if(title==null){
			return true;
		}
		if(title.length()>TITLE_MAX_SIZE) {
			return false;
		}
		return true;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void safeSetTitle(String title) {
    	this.title = StringUtils.substring(title, 0, TITLE_MAX_SIZE);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public void safeSetText(String text) {
    	this.text = StringUtils.substring(text, 0, TEXT_MAX_SIZE);
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public static boolean isValidUserProfile(UserProfile userProfile) {
		return userProfile!=null;
	}
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserNotification)) {
            return false;
        }
        UserNotification other = (UserNotification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "UserNotification [id=" + id + "]";
	}

}

