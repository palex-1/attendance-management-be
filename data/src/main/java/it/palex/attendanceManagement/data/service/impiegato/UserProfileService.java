package it.palex.attendanceManagement.data.service.impiegato;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.dto.core.UserProfileDTO;
import it.palex.attendanceManagement.data.dto.transformers.UserProfileTransformer;
import it.palex.attendanceManagement.data.entities.QUserProfile;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.UserProfileImage;
import it.palex.attendanceManagement.data.entities.enumTypes.SupportedImageCompression;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.auth.UserProfileRepository;
import it.palex.attendanceManagement.data.service.core.UserProfileImageService;
import it.palex.attendanceManagement.data.service.user.UsersAuthDetailsService;
import it.palex.attendanceManagement.library.service.BasicGenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;

/**
 * @author Alessandro Pagliaro
 *
 */
@Service
public class UserProfileService implements BasicGenericService {
	private final QUserProfile QUP = QUserProfile.userProfile;
	
	@Autowired
	private UserProfileRepository userProfileRepo;
	
	@Autowired
	private ChangeEmailRequestService changeEmailSrv;
	
	@Autowired
	private UsersAuthDetailsService userAuthDetailsSrv;
	
	@Autowired
	private UserProfileImageService userProfileImageService;
	
	
	public UserProfile saveOrUpdate(UserProfile userProfile, boolean isFirstSave) {
		if(userProfile==null) {
			throw new NullPointerException();
		}
		if(!userProfile.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(userProfile);
		}
		
		if(isFirstSave) {
			userProfile.setId(userProfile.getUsersAuthDetails().getId());
			return this.userProfileRepo.persistForFirstTime(userProfile);
		}
		
		return this.userProfileRepo.save(userProfile);		
	}
	
	
	/**
	 * @param hashedUsername
	 */
	public UserProfile findByUsername(String username) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QUP.usersAuthDetails.username.eq(username));
		
		return this.getFirstResultFromIterable(this.userProfileRepo.findAll(condition));
	}

	public UserProfile findById(Integer id) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QUP.usersAuthDetails.id.eq(id));
		
		Iterator<UserProfile> it = this.userProfileRepo.findAll(condition).iterator();
		
		if(it.hasNext()){
			return it.next();
		}
		return null;
	}
	
	public UserProfileDTO buildUserProfileInfo(UserProfile userProfile, SupportedImageCompression compression) {
		if(userProfile==null || compression==null) {
			throw new NullPointerException();
		}
		
		UserProfileImage image = this.userProfileImageService.findBestMatchUserProfileImage(
				userProfile, compression);

		String userProfileImageDownloadToken = image == null ? null : image.getDownloadToken();
		
		
		return UserProfileTransformer.mapToDTO(userProfile, userProfileImageDownloadToken);
	}


	public List<UserProfile> findAll(Integer id, Date birthDate, String cf, String email, Date employmentDate, String level,
			String name, String phoneNumber, String sex, String surname, Pageable pageable) {
		if(pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = this.buildFindAllCondition(id, birthDate, cf, email, 
				employmentDate, level, name, phoneNumber, sex, surname);
		
		List<UserProfile> res = this.iterableToList(
					this.userProfileRepo.findAll(cond, pageable)
				);
		
		return res;
	}


	public long countAll(Integer id, Date birthDate, String cf, String email, Date employmentDate, String level,
			String name, String phoneNumber, String sex, String surname) {
		BooleanBuilder cond = this.buildFindAllCondition(id, birthDate, cf, email, 
				employmentDate, level, name, phoneNumber, sex, surname);
		
		long count = this.userProfileRepo.count(cond);
		
		return count;
	}
	
	
	private BooleanBuilder buildFindAllCondition(Integer id, Date birthDate, String cf, String email, Date employmentDate, String level,
			String name, String phoneNumber, String sex, String surname) {
		BooleanBuilder cond = new BooleanBuilder();
		
		if(id!=null) {
			cond.and(QUP.id.eq(id));
		}
		if(birthDate!=null) {
			Date startOfDate = DateUtility.startOfDayOfDate(birthDate);
			Date endOfDate = DateUtility.endOfDayOfDate(birthDate);
			cond.and(QUP.birthDate.goe(startOfDate));
			cond.and(QUP.birthDate.loe(endOfDate));
		}
		if(cf!=null) {
			cond.and(QUP.fiscalCode.containsIgnoreCase(cf));
		}
		if(email!=null) {
			cond.and(QUP.email.containsIgnoreCase(email));
		}
		if(employmentDate!=null) {
			Date startOfDate = DateUtility.startOfDayOfDate(employmentDate);
			Date endOfDate = DateUtility.endOfDayOfDate(employmentDate);
			cond.and(QUP.dateOfEmployment.goe(startOfDate));
			cond.and(QUP.dateOfEmployment.loe(endOfDate));
		}
		if(level!=null) {
			cond.and(QUP.userProfileContractInfo.level.level.containsIgnoreCase(level));
		}
		if(name!=null) {
			cond.and(QUP.name.containsIgnoreCase(name));
		}
		if(phoneNumber!=null) {
			cond.and(QUP.phoneNumber.containsIgnoreCase(phoneNumber));
		}
		if(sex!=null) {
			cond.and(QUP.sex.eq(phoneNumber));
		}
		if(surname!=null) {
			cond.and(QUP.surname.containsIgnoreCase(surname));
		}
		
		return cond;
	}


	public List<UserProfile> findAllUserWithActiveAccount(Pageable pageable) {
		if(pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUP.usersAuthDetails.isEnabled.isTrue());
		
		return this.iterableToList(this.userProfileRepo.findAll(cond, pageable));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
