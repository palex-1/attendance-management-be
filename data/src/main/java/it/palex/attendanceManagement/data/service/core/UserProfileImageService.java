package it.palex.attendanceManagement.data.service.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.dto.documents.DocumentoReadInternalResponse;
import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.QUserProfileImage;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.UserProfileImage;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedImageCompression;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.UserProfileImageRepository;
import it.palex.attendanceManagement.data.service.documento.DocumentService;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.IterableUtils;
import it.palex.attendanceManagement.library.utils.crypto.TokenGenerator;
import it.palex.attendanceManagement.data.utils.ImageCompressionUtils;


@Service
public class UserProfileImageService implements GenericService {

	private final QUserProfileImage QUPI = QUserProfileImage.userProfileImage;
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory
			.getLogger(UserProfileImageService.class);

	@Autowired
	private UserProfileImageRepository userProfileImageRepo;

	@Autowired
	private DocumentService documentService;
	

	public UserProfileImage save(UserProfileImage newAppUser, boolean generateToken) {
		if (newAppUser == null) {
			throw new NullPointerException();
		}
		if(generateToken) {
			newAppUser.setDownloadToken(TokenGenerator.generateTokenOf40Characters());
			newAppUser.setDownloadTokenCreationDate(DateUtility.getCurrentDateInUTC());
		}
		if (!newAppUser.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(newAppUser);
		}
		

		return this.userProfileImageRepo.save(newAppUser);
	}

	public boolean existsImage(UserProfileImage userProfileDetails) {
		if (userProfileDetails == null || userProfileDetails.getId() == null) {
			return false;
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUPI.id.eq(userProfileDetails.getId()));

		Iterator<UserProfileImage> it = this.userProfileImageRepo.findAll(cond).iterator();

		return it.hasNext();
	}

	public UserProfileImage findByID(Integer id) {
		if (id == null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUPI.id.eq(id));

		Iterator<UserProfileImage> it = this.userProfileImageRepo.findAll(cond).iterator();

		if (it.hasNext()) {
			return it.next();
		}

		return null;
	}

	public UserProfileImage findByUnique(UserProfile userProfile, 
				SupportedImageCompression compression) {
		if(userProfile==null || compression==null) {
			return null;
		}
		
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUPI.userProfile.id.eq(userProfile.getId()));
		cond.and(QUPI.imageCompression.equalsIgnoreCase(compression.name()));
		
		return this.getFirstResultFromIterable(this.userProfileImageRepo.findAll(cond));
	}
	
	public int deleteAllImageWithCompressionDifferentFrom(UserProfile userProfile, 
			SupportedImageCompression compression) {
		if(userProfile==null || compression==null) {
			throw new NullPointerException();
		}
		List<UserProfileImage> images = findAllImagesOfUser(userProfile);
		
		int deleted = 0;
		for (UserProfileImage userProfileImage : images) {
			Document document = userProfileImage.getProfileImageId();
			this.userProfileImageRepo.delete(userProfileImage);
			
			if(document!=null) {
				try {
					this.documentService.deleteDocumentAndFile(document);
					deleted++;
				}catch(Exception e) {
					LOGGER.error("", e);
				}
			}
		}
		
		return deleted;
	}
	
	public List<UserProfileImage> findAllUserProfileImageTooBig(long minSizeToConsiderImageBig, 
			Pageable pageable){
		if(pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUPI.profileImageId.fileSize.gt(minSizeToConsiderImageBig));
		
		return IterableUtils.iterableToList(this.userProfileImageRepo.findAll(cond, pageable));
	}

	public List<UserProfileImage> findAllUserProfileImageWithoutCompression(
			SupportedImageCompression compression, Pageable pageable) {
		if(compression==null || pageable==null) {
			throw new NullPointerException();
		}
		
		return this.userProfileImageRepo.findAllUserProfileImageWithoutCompression(
				SupportedImageCompression.NORMAL.name(), compression.name(), pageable);		
	}

	public UserProfileImage saveOrUpdateImageProfile(UserProfile userProfile,
			Document document, SupportedImageCompression compression) {
		if(userProfile==null || document==null || compression==null) {
			throw new NullPointerException();
		}
		UserProfileImage old = this.findByUnique(userProfile, compression);
		
		if(old!=null) {
			old.setProfileImageId(document);
			old.setDownloadToken(TokenGenerator.generateTokenOf40Characters());
			old.setDownloadTokenCreationDate(DateUtility.getCurrentDateInUTC());
			return this.userProfileImageRepo.save(old);
		}
		
		UserProfileImage newImage = new UserProfileImage();
		newImage.setImageCompression(compression.name());
		newImage.setProfileImageId(document);
		newImage.setUserProfile(userProfile);
		newImage.setDownloadToken(TokenGenerator.generateTokenOf40Characters());
		newImage.setDownloadTokenCreationDate(DateUtility.getCurrentDateInUTC());
		
		return this.userProfileImageRepo.save(newImage);
	}
	
	public List<UserProfileImage> findAllImagesOfUser(UserProfile userProfile){
		if(userProfile==null) {
			return null;
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUPI.userProfile.id.eq(userProfile.getId()));
		
		return IterableUtils.iterableToList(this.userProfileImageRepo.findAll(cond));		
	}
	
	/**
	 * 
	 * @param userProfile
	 * @param imageProfileCompression
	 * @return
	 * @throws Exception
	 */
	public UserProfileImage findBestMatchUserProfileImage(UserProfile userProfile,
			SupportedImageCompression imageProfileCompression) {
		List<UserProfileImage> images = findAllImagesOfUser(userProfile);
		if(images==null || images.isEmpty()) {
			return null;
		}
		List<String> supportedExt = new LinkedList<>();
		
		for(UserProfileImage image: images) {
			supportedExt.add(image.getImageCompression());
		}
		
		SupportedImageCompression bestMatch = ImageCompressionUtils
				.findBestMatch(supportedExt, imageProfileCompression);
		
		if(bestMatch!=null) {
			Iterator<UserProfileImage> it = images.iterator();
			while(it.hasNext()) {
				UserProfileImage image = it.next();
				if(StringUtils.equalsIgnoreCase(bestMatch.name(), image.getImageCompression())) {
					return image;
				}
			}
		}
		
		return null;
	}

	public UserProfileImage findByToken(String token) {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUPI.downloadToken.eq(token));
		
		return this.getFirstResultFromIterable(this.userProfileImageRepo.findAll(cond));
	}
	
	public DocumentoReadInternalResponse openStreamOnUserProfileImage(
			String downloadToken) throws Exception {
		if(downloadToken==null) {
			return null;
		}
		
		UserProfileImage image = this.findByToken(downloadToken);
		if(image==null) {
			return null;
		}
		
		return this.documentService.openStreamOnFile(image.getProfileImageId());
	}
}