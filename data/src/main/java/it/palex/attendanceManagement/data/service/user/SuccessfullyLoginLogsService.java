package it.palex.attendanceManagement.data.service.user;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.dto.security.SuccessfullyLoginLogsDTO;
import it.palex.attendanceManagement.data.entities.auth.QSuccessfullyLoginLogs;
import it.palex.attendanceManagement.data.entities.auth.SuccessfullyLoginLogs;
import it.palex.attendanceManagement.data.exceptions.EntityAlreadyExistsException;
import it.palex.attendanceManagement.data.exceptions.EntityCreationException;
import it.palex.attendanceManagement.data.repository.auth.SuccessfullyLoginLogsRepository;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.HttpCodes;

/**
 * @author Alessandro Pagliaro
 *
 */
@Service
public class SuccessfullyLoginLogsService implements GenericService {

	private final QSuccessfullyLoginLogs QSLL = QSuccessfullyLoginLogs.successfullyLoginLogs;
	
	@Autowired
	private SuccessfullyLoginLogsRepository sllRepo;

	/**
	 * @param attempt
	 * @return
	 */
	public boolean exists(SuccessfullyLoginLogs attempt) {
		if(attempt==null || attempt.getSuccessfullyLoginLogsPK()==null){
			throw new NullPointerException("Cannot check existance of a null SuccessfullyLoginLogs attempt");
		}
		String ip = attempt.getSuccessfullyLoginLogsPK().getIp();
		String userAgent = attempt.getSuccessfullyLoginLogsPK().getUserAgent();
		Integer idUserAuthDetails = attempt.getSuccessfullyLoginLogsPK().getFkIdUsersAuthDetails();
		Date loginDate = attempt.getSuccessfullyLoginLogsPK().getLoginDate();
		
		SuccessfullyLoginLogs check = this.getByKey(idUserAuthDetails, ip, userAgent, loginDate);
		
		return check!=null;
	}

	/**
	 * @param attempt
	 * @throws EntityCreationException 
	 * @throws EntityAlreadyExistsException 
	 */
	@Transactional(rollbackFor=Exception.class)
	public void create(SuccessfullyLoginLogs attempt) throws EntityCreationException, EntityAlreadyExistsException {
		if(attempt==null){
			throw new NullPointerException("cannot create a null attempt");
		}
		if(!attempt.canBeInsertedInDatabase()){
			throw new EntityCreationException("The entity cannot be added in database");
		}
		String ip = attempt.getSuccessfullyLoginLogsPK().getIp();
		String userAgent = attempt.getSuccessfullyLoginLogsPK().getUserAgent();
		Integer idUserAuthDetails = attempt.getSuccessfullyLoginLogsPK().getFkIdUsersAuthDetails();
		Date loginDate = attempt.getSuccessfullyLoginLogsPK().getLoginDate();
		
		SuccessfullyLoginLogs check = this.getByKey(idUserAuthDetails, ip, userAgent, loginDate);
		if(check!=null){
			throw new EntityAlreadyExistsException("Entity already exists exception");
		}
		
		this.sllRepo.save(attempt);
	}
	
	
	public SuccessfullyLoginLogs getByKey(Integer idUserAuthDetails, String ip, String userAgent, Date loginDate){
		if(idUserAuthDetails==null || ip==null || userAgent==null || loginDate==null){
			throw new NullPointerException("idUserAuthDetails:"+idUserAuthDetails+"ip:"+ip+" userAgent:"+userAgent+" loginDate:"+loginDate);
		}
		
		return this.sllRepo.getByKey(idUserAuthDetails, ip, userAgent, loginDate);
	}

	/**
	 * @param pageable
	 * @param predicate
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public GenericResponse<Page<SuccessfullyLoginLogsDTO>> findAll(Pageable pageable, 
			Date fromDate, Date toDate) {
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(username==null){
			return this.buildUnauthorizedResponse("User not found");
		}
		if(pageable==null) {
			return this.buildBadDataResponse("pageble cannot be null");
		}
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QSLL.usersAuthDetails.username.eq(username));
		if(fromDate!=null){
			condition.and(QSLL.successfullyLoginLogsPK.loginDate.after(fromDate)
							.or(QSLL.successfullyLoginLogsPK.loginDate.eq(fromDate))
					);
		}
		if(toDate!=null){
			condition.and(QSLL.successfullyLoginLogsPK.loginDate.before(toDate)
							.or(QSLL.successfullyLoginLogsPK.loginDate.eq(toDate))
					);
		}
		
		Iterator<SuccessfullyLoginLogs> it = this.sllRepo.findAll(condition, pageable).iterator();
		List<SuccessfullyLoginLogsDTO> list = new LinkedList<SuccessfullyLoginLogsDTO>();
		while(it.hasNext()){
			list.add(mapToDTO(it.next()));
		}
		
		long totalCount = this.sllRepo.count(condition);
		
		return this.buildPageableOkResponse(list, totalCount, pageable);
	}
	
	
	private SuccessfullyLoginLogsDTO mapToDTO(SuccessfullyLoginLogs logs){
		SuccessfullyLoginLogsDTO park = new SuccessfullyLoginLogsDTO();
		park.setIp(logs.getSuccessfullyLoginLogsPK().getIp());
		park.setLoginDate(logs.getSuccessfullyLoginLogsPK().getLoginDate());
		park.setUserAgent(logs.getSuccessfullyLoginLogsPK().getUserAgent());
		
		return park;
	}

	public int deleteAllSuccLoginLogsMadeBefore(Calendar date) {
		return this.sllRepo.clearLoginHistoryOfAllAccountBeforeDate(date);
	}
}
