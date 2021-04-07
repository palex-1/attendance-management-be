package it.palex.attendanceManagement.data.service.impiegato;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.QUserProfileContractInfo;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.UserProfileContractInfo;
import it.palex.attendanceManagement.data.entities.core.UserLevel;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.UserProfileContractInfoRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class UserProfileContractInfoService implements BasicGenericService {

	private final QUserProfileContractInfo QUPCI = QUserProfileContractInfo.userProfileContractInfo;
	
	@Autowired
	private UserProfileContractInfoRepository userProfileContractInfoRepository;
	
	
	public UserProfileContractInfo saveOrUpdate(UserProfileContractInfo info) {
		if(info==null) {
			throw new NullPointerException();
		}
		if(!info.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(info);
		}
		
		return this.userProfileContractInfoRepository.save(info);		
	}
	
	public UserProfileContractInfo findById(Integer id) {
		if(id==null) {
			return null;
		}
		
		return this.getFromOptional(
					this.userProfileContractInfoRepository.findById(id)
				);
	}
	
	public UserProfileContractInfo findByUserProfile(UserProfile profile) {
		if(profile==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUPCI.userProfile.id.eq(profile.getId()));
		
		return this.getFirstResultFromIterable(
					this.userProfileContractInfoRepository.findAll(cond)
				);
	}

	public boolean checkIfAnyUserHasTheLevel(UserLevel levelToDelete) {
		if(levelToDelete==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUPCI.level.id.eq(levelToDelete.getId()));
		
		return this.userProfileContractInfoRepository.count(cond)>0;
	}
	
	
	
}
