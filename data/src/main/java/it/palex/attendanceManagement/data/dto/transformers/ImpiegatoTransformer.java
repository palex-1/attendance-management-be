package it.palex.attendanceManagement.data.dto.transformers;

import it.palex.attendanceManagement.data.dto.impiegato.ImpiegatoSmallDTO;
import it.palex.attendanceManagement.data.dto.impiegato.ImpiegatoTinyDTO;
import it.palex.attendanceManagement.data.entities.UserProfile;

/**
 * @author Alessandro Pagliaro
 *
 */
public class ImpiegatoTransformer {

	public static ImpiegatoTinyDTO mapToTinyDTO(UserProfile impiegato) {
		if(impiegato==null) {
			return null;
		}
		ImpiegatoTinyDTO res = new ImpiegatoTinyDTO();
		res.setCognome(impiegato.getSurname());
		res.setNome(impiegato.getName());
		res.setId(impiegato.getId());
		res.setEmail(impiegato.getEmail());
		
		return res;
	}
	
	public static ImpiegatoSmallDTO mapToSmallDTO(UserProfile impiegato) {
		if(impiegato==null) {
			return null;
		}
		ImpiegatoSmallDTO res = new ImpiegatoSmallDTO();
		res.setCognome(impiegato.getSurname());
		res.setNome(impiegato.getName());
		res.setId(impiegato.getId());
		res.setEmail(impiegato.getEmail());
		res.setNumero(impiegato.getPhoneNumber());
		
		if(impiegato.getCompany()!=null) {
			res.setNomeAzienda(impiegato.getCompany().getName());
		}
		
		
		return res;
	}
}
