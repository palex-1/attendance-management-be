package it.palex.attendanceManagement.data.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.enumTypes.SupportedImageCompression;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;


@Entity
@Table(name = "user_profile_image")
public class UserProfileImage extends AuditableEntity implements DatabaseCheckableEntity{

	private static final long serialVersionUID = 3597833699494569607L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
    @Column(name = "id")
    private Integer id;
	
	public static final int IMAGE_COMPRESSION_MAX_SIZE = 30;
	
	@Basic(optional = false)
    @NotNull
    @Size(max = IMAGE_COMPRESSION_MAX_SIZE)
    @Column(name = "image_compression")
    private String imageCompression = SupportedImageCompression.NORMAL.name();
	
	@JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;

    @JoinColumn(name = "profile_image_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Document profileImageId;
    
    public static final int DOWNLOAD_TOKEN_MAX_SIZE = 128;
    
    @NotNull
    @Size(max = DOWNLOAD_TOKEN_MAX_SIZE)
    @Column(name = "download_token")
    private String downloadToken;
    
    @NotNull
    @Column(name = "download_token_creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date downloadTokenCreationDate;
    
    
    public boolean canBeInsertedInDatabase() {
		boolean isValid= isValidUserProfile(this.userProfile) &&  
				isValidDocument(this.profileImageId)
				 && isValidImageCompression(this.imageCompression)
				  && isValidDownloadToken(this.downloadToken) 
				  && isValidDownloadTokenCreationDate(this.downloadTokenCreationDate);
		return isValid;
	}

    @Override
	public String whyCannotBeInsertedInDatabase() {
		final String why = "userProfile:"+this.userProfile+", profileImageId:"+ this.profileImageId+", imageCompression:"+ this.imageCompression
				+", downloadToken:"+ this.downloadToken+", downloadTokenCreationDate:"+ this.downloadTokenCreationDate;
		
		return why;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	
    public String getDownloadToken() {
		return downloadToken;
	}
    
	public void setDownloadToken(String downloadToken) {
		this.downloadToken = downloadToken;
	}
	
	private boolean isValidDownloadToken(String downloadToken) {
		if(downloadToken==null) {
			return false;
		}
		if(downloadToken.length()>DOWNLOAD_TOKEN_MAX_SIZE) {
			return false;
		}
		return true;
	}


	public Date getDownloadTokenCreationDate() {
		return downloadTokenCreationDate;
	}


	public void setDownloadTokenCreationDate(Date downloadTokenCreationDate) {
		this.downloadTokenCreationDate = downloadTokenCreationDate;
	}
	
	public static boolean isValidDownloadTokenCreationDate(Date downloadTokenCreationDate) {
		return downloadTokenCreationDate!=null;
	}


	public String getImageCompression() {
		return imageCompression;
	}

	public void setImageCompression(String imageCompression) {
		this.imageCompression = imageCompression;
	}

	public static boolean isValidImageCompression(String imageCompression) {
		if(imageCompression==null) {
			return false;
		}
		
		return SupportedImageCompression.isValid(imageCompression);
	}

	public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    
    public Document getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(Document profileImageId) {
        this.profileImageId = profileImageId;
    }
    
    public static boolean isValidUserProfile (UserProfile userProfile) {
    	return userProfile!=null;
    }
    
    public static boolean isValidDocument (Document profileImageId) {
    	return profileImageId!=null;
    }

	@Override
	public String toString() {
		return "UserProfileImage [id=" + id + "]";
	}
    
    
}

