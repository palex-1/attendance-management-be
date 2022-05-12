package it.palex.attendanceManagement.data.service.core;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import it.palex.attendanceManagement.library.utils.DateUtility;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.QUserAttendance;
import it.palex.attendanceManagement.data.entities.core.Turnstile;
import it.palex.attendanceManagement.data.entities.core.UserAttendance;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.UserAttendanceRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class UserAttendanceService implements BasicGenericService {

	private final QUserAttendance QUA = QUserAttendance.userAttendance;
		
	@Autowired
	private UserAttendanceRepository userAttendanceRepository;
	
	
	public UserAttendance saveOrUpdate(UserAttendance attendance) {
		if(attendance==null) {
			throw new NullPointerException();
		}
		if(!attendance.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(attendance);
		}
		return this.userAttendanceRepository.save(attendance);
	}
	
	public UserAttendance findById(Long id) {
		if(id==null) {
			return null;
		}
		Optional<UserAttendance> opt = this.userAttendanceRepository.findById(id);
		
		return this.getFromOptional(opt);
	}
	
	
	public Pair<List<UserAttendance>, Long> findAllAttendanceOfTurnstile(Turnstile turnstile,
			Date dateFrom, Date dateTo, Pageable pageable){
		if(turnstile==null || pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUA.turnstile.id.eq(turnstile.getId()));
		cond.and(QUA.deleted.isFalse());
		
		if(dateFrom!=null) {
			cond.and(QUA.timestamp.goe(dateFrom));
		}
		if(dateTo!=null) {
			cond.and(QUA.timestamp.loe(dateTo));
		}
		
		long totalCount = this.userAttendanceRepository.count(cond);
		List<UserAttendance> list = this.iterableToList(
						this.userAttendanceRepository.findAll(cond, pageable)
					);
		
		return Pair.of(list, totalCount);
	}
	
	
	
	public void deleteAttendance(UserAttendance attendance) {
		if(attendance==null) {
			throw new NullPointerException();
		}
		attendance.setDeleted(true);
		
		this.userAttendanceRepository.save(attendance);
	}

	public List<UserAttendance> findTaskOfUserInDateRange(UserProfile profile, Sort sort, Date startOfDayOfDate,
			Date endOfDayOfDate) {
		if(profile==null || sort==null || startOfDayOfDate==null || endOfDayOfDate==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUA.userProfile.id.eq(profile.getId()));
		cond.and(QUA.timestamp.goe(startOfDayOfDate));
		cond.and(QUA.timestamp.loe(endOfDayOfDate));
		cond.and(QUA.deleted.isFalse());
		
		List<UserAttendance> list = this.iterableToList(
				this.userAttendanceRepository.findAll(cond, sort)
			);
		
		return list;
	}

	public UserAttendance findLastAttendanceOfUser(UserProfile profile) {
		if(profile==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUA.userProfile.id.eq(profile.getId()));
		
		Sort sort = Sort.by(Direction.DESC, "timestamp");
		
		PageRequest page = PageRequest.of(0, 1, sort);
		
		UserAttendance attendance = this.getFirstResultFromIterable(
										this.userAttendanceRepository.findAll(cond, page)
									);
		
		return attendance;
	}


	public List<UserAttendance> findAttendanceOfDay(Long turnstileId, Date dateAttedance, Sort sort) {
		if(dateAttedance==null){
			throw new IllegalArgumentException("Day must be specified. Or the result could be too large");
		}

		if(sort==null){
			throw new NullPointerException("sort arg required");
		}

		BooleanBuilder cond = new BooleanBuilder();

		if(turnstileId!=null){
			cond.and(QUA.turnstile.id.eq(turnstileId));
		}

		cond.and(QUA.timestamp.goe(DateUtility.startOfDayOfDate(dateAttedance)));
		cond.and(QUA.timestamp.loe(DateUtility.endOfDayOfDate(dateAttedance)));
		cond.and(QUA.deleted.isFalse());

		List<UserAttendance> list = this.iterableToList(
				this.userAttendanceRepository.findAll(cond, sort)
		);

		return list;
	}
}
