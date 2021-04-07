package it.palex.attendanceManagement.data.service.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.dto.core.UserProfileAddressDTO;
import it.palex.attendanceManagement.data.dto.transformers.UserProfileAddressTransformer;
import it.palex.attendanceManagement.data.entities.QUserProfileAddress;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.UserProfileAddress;
import it.palex.attendanceManagement.data.entities.enumTypes.AddressTypeEnum;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.UserProfileAddressRepository;
import it.palex.attendanceManagement.library.service.GenericService;

@Service
public class UserProfileAddressService implements GenericService {

	private final QUserProfileAddress QUPS = QUserProfileAddress.userProfileAddress;
	
	@Autowired
	private UserProfileAddressRepository userProfileAddressRepository;
	
	
	public UserProfileAddress saveOrUpdate(UserProfileAddress address) {
		if(address==null) {
			throw new NullPointerException();
		}
		if(!address.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(address);
		}
		
		return this.userProfileAddressRepository.save(address);
	}
	
	
	public UserProfileAddressDTO findByUserProfileAndAddressForWeb(UserProfile user, AddressTypeEnum addressType) {
		UserProfileAddress address = this.findByUserProfileAndAddress(user, addressType);
		
		return UserProfileAddressTransformer.mapToDTO(address);
	}
	
	
	public UserProfileAddress findByUserProfileAndAddress(UserProfile user, AddressTypeEnum addressType) {
		if(user==null || addressType==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUPS.addressType.eq(addressType.name()));
		cond.and(QUPS.userProfile.id.eq(user.getId()));
		
		return this.getFirstResultFromIterable(
					this.userProfileAddressRepository.findAll(cond)
				);
	}
	
}
