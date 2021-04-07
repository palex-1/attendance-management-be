package it.palex.attendanceManagement.data.service.core;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.QWorkTransferRequest;
import it.palex.attendanceManagement.data.entities.core.WorkTransferRequest;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.WorkTransferRequestRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class WorkTransferRequestService implements BasicGenericService {

	private final QWorkTransferRequest QWTR = QWorkTransferRequest.workTransferRequest;

	@Autowired
	private WorkTransferRequestRepository workTransferRequestRepository;

	public WorkTransferRequest saveOrUpdate(WorkTransferRequest req) {
		if (req == null) {
			throw new NullPointerException();
		}
		if (!req.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(req);
		}

		return this.workTransferRequestRepository.save(req);
	}

	public WorkTransferRequest findById(Long id) {
		if (id == null) {
			return null;
		}

		return this.getFromOptional(this.workTransferRequestRepository.findById(id));
	}

	public WorkTransferRequest findByUserAndDay(UserProfile user, Date day) {
		if (user == null || day == null) {
			throw new NullPointerException("user:" + user + ", day:" + day);
		}

		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QWTR.userProfile.id.eq(user.getId()));
		cond.and(QWTR.day.eq(day));

		WorkTransferRequest req = this.getFirstResultFromIterable(this.workTransferRequestRepository.findAll(cond));

		return req;
	}

	public void delete(WorkTransferRequest req) {
		if (req == null) {
			throw new NullPointerException("Req is null");
		}
		this.workTransferRequestRepository.delete(req);
	}

	public WorkTransferRequest findByIdAndUser(Long workTransferRequestId, UserProfile user) {
		if (workTransferRequestId == null || user == null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QWTR.id.eq(workTransferRequestId));
		cond.and(QWTR.userProfile.id.eq(user.getId()));

		WorkTransferRequest req = this.getFirstResultFromIterable(this.workTransferRequestRepository.findAll(cond));

		return req;
	}

	public List<WorkTransferRequest> findRequestOfUserInDateRange(UserProfile profile, Date from, Date to) {
		if (profile == null || from == null || to == null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QWTR.userProfile.id.eq(profile.getId()));
		cond.and(QWTR.day.goe(from));
		cond.and(QWTR.day.loe(to));

		List<WorkTransferRequest> res = this.iterableToList(this.workTransferRequestRepository.findAll(cond));

		return res;
	}

}
