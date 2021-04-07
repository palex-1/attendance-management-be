package it.palex.attendanceManagement.data.dto.transformers;

import it.palex.attendanceManagement.data.dto.documents.TicketDownloadDTO;
import it.palex.attendanceManagement.data.entities.TicketDownload;

public class TicketDownloadTransformer {

	public static TicketDownloadDTO mapToDTO(TicketDownload ticket) {
		if(ticket==null) {
			return null;
		}
		TicketDownloadDTO res = new TicketDownloadDTO();
		res.setCreationDate(ticket.getCreationDate());
		res.setTokenDownload(ticket.getTokenDownload());
		res.setExpirationDate(ticket.getExpirationDate());
		
		return res;
	}
	
}
