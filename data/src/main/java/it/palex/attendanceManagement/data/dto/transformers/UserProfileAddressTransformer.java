package it.palex.attendanceManagement.data.dto.transformers;

import it.palex.attendanceManagement.data.dto.core.UserProfileAddressDTO;
import it.palex.attendanceManagement.data.entities.UserProfileAddress;

public class UserProfileAddressTransformer {

	public static UserProfileAddressDTO mapToDTO(UserProfileAddress address) {
		if(address==null) {
			return null;
		}
		UserProfileAddressDTO res = new UserProfileAddressDTO();
		res.setAddressType(address.getAddressType());
		res.setCity(address.getCity());
		res.setId(address.getId());
		res.setNation(address.getNation());
		res.setProvince(address.getProvince());
		res.setStreet(address.getStreet());
		res.setZipCode(address.getZipCode());
		
		return res;
	}

}
