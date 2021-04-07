package it.palex.attendanceManagement.data.service.documento;

import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.QTicketDownload;
import it.palex.attendanceManagement.data.entities.TicketDownload;
import it.palex.attendanceManagement.data.repository.documento.TicketDownloadRepository;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.crypto.TokenGenerator;

@Service
public class TicketDownloadService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TicketDownloadService.class);

	private final QTicketDownload QTicketDow = QTicketDownload.ticketDownload;
	
	@Autowired
	private TicketDownloadRepository ticketDownloadRepo;
	
	 
	public TicketDownload createAndSaveOneShotTicketForDocument(Document doc) {
		if(doc==null) {
			throw new NullPointerException();
		}
		TicketDownload ticket = new TicketDownload();
		Date currentDate = DateUtility.getCurrentDateInUTC();
		Date expirationDate = (Date) currentDate.clone();
		expirationDate = DateUtility.addMinutesToDate(expirationDate, 1); //set to expire after one minute
		
		ticket.setCreationDate(currentDate);		
		final String token = TokenGenerator.generateTokenOf64Characters();
		ticket.setTokenDownload(token);
		ticket.setDocument(doc);
		ticket.setExpirationDate(expirationDate);
		ticket.setIsOneShot(true);
		
		return this.ticketDownloadRepo.save(ticket);
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 * @throws NullPointerException if doc is null
	 */
	public TicketDownload createAndSaveTicketForDocument(Document doc, int secondsTicketDownloadValidity) {
		if(doc==null) {
			throw new NullPointerException();
		}
		Date currentDate = DateUtility.getCurrentDateInUTC();
		Date expirationDate = (Date) currentDate.clone();
		
		expirationDate = DateUtility.addSecondsToCalendar(expirationDate, 
				secondsTicketDownloadValidity);
		
		return createAndSaveTicketForDocument(doc, expirationDate);
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 * @throws NullPointerException if doc is null
	 */
	public TicketDownload createAndSaveTicketForDocument(Document doc, Date expirationDate) {
		if(doc==null || expirationDate==null) {
			throw new NullPointerException();
		}
		TicketDownload ticket = new TicketDownload();
		Date currentDate = DateUtility.getCurrentDateInUTC();
		
		ticket.setCreationDate(currentDate);		
		final String token = TokenGenerator.generateTokenOf64Characters();
		ticket.setTokenDownload(token);
		ticket.setDocument(doc);
		ticket.setExpirationDate(expirationDate);
		ticket.setIsOneShot(false);
		
		return this.ticketDownloadRepo.save(ticket);
	}
	
	public TicketDownload findByToken(String downloadToken) {
		if(downloadToken==null) {
			return null;
		}
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QTicketDow.tokenDownload.eq(downloadToken));
		
		Iterator<TicketDownload> it = this.ticketDownloadRepo.findAll(condition).iterator();
		
		if(!it.hasNext()) {
			return null;
		}
		
		TicketDownload ticket = it.next();
		
		return ticket;
	}

	public void delete(TicketDownload ticket) {
		if(ticket!=null) {
			this.ticketDownloadRepo.delete(ticket);
		}
	}

	public int deleteAllExpiredTicketsBefore(Date beforeDate) {
		return this.ticketDownloadRepo.deleteTokenExpiredBefore(beforeDate);
	}
	
}
