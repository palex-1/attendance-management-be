package it.palex.attendanceManagement.data.repository.incarico;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import it.palex.attendanceManagement.data.entities.CompletedTask;


/**
 * @author Alessandro Pagliaro
 *
 */
@Repository
public interface CompletedTaskRepository extends JpaRepository<CompletedTask, Long>, 
		QuerydslPredicateExecutor<CompletedTask>, CompletedTaskRepositoryCustom {

	@Query("select coalesce(sum(workedHours),0) FROM CompletedTask ctask where ctask.userProfile.id= ?1 AND ctask.day= ?2")
	public long sumAllUserHoursMadeOnDay(Integer id, Date day);

	@Query("select coalesce(sum(workedHours),0) FROM CompletedTask ctask where ctask.userProfile.id= ?1 AND ctask.day= ?2 AND ctask.taskCode.isAbsenceTask=FALSE")
	public long sumOnlyWorkedHoursMadeOnDay(Integer id, Date day);

	@Query("select coalesce(sum(workedHours),0) FROM CompletedTask ctask where ctask.userProfile.id= ?1 AND ctask.day= ?2 AND ctask.taskCode.isAbsenceTask=TRUE")
	public long sumOnlyHoursOfAbsenceMadeOnDay(Integer id, Date day);
	
	@Query("select coalesce(sum(workedHours),0) FROM CompletedTask ctask where ctask.taskCode.id=?1 AND ctask.taskCode.isAbsenceTask=FALSE AND ctask.userProfile.id= ?2 "
			+ "AND ctask.day>= ?3 AND ctask.day<= ?4 ")
	public long sumOnlyWorkedHoursMadeOnDateRangeByUser(Long taskId, Integer userProfileId, Date startDate, Date endDate);

	@Query("select coalesce(sum(workedHours),0) FROM CompletedTask ctask where ctask.taskCode.id=?1 AND ctask.userProfile.id= ?2 "
			+ "AND  ctask.day>= ?3 AND ctask.day<= ?4 ")
	public long sumRegisteredHoursMadeOnDateRangeByUser(Long taskId, Integer userProfileId, Date startDate, Date endDate);


}
