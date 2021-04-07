package it.palex.attendanceManagement.data.repository.documento;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.TicketDownload;

@Repository
public interface TicketDownloadRepository  extends JpaRepository<TicketDownload, Integer>, 
			QuerydslPredicateExecutor<TicketDownload>{

	@Modifying
	@Query("DELETE from TicketDownload where expirationDate < ?1")
	public int deleteTokenExpiredBefore(Date beforeDate);
	
}
