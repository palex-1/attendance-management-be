package it.palex.attendanceManagement.data.service.core;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.CompletedTask;
import it.palex.attendanceManagement.data.entities.FoodVoucherRequest;
import it.palex.attendanceManagement.data.entities.QFoodVoucherRequest;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.FoodVoucherRequestRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class FoodVoucherRequestService implements BasicGenericService {

	private final QFoodVoucherRequest QFVR = QFoodVoucherRequest.foodVoucherRequest;
	
	@Autowired
	private FoodVoucherRequestRepository foodVoucherRequestRepository;
	
		
	public FoodVoucherRequest saveOrUpdate(FoodVoucherRequest request) {
		if(request==null) {
			throw new NullPointerException();
		}
		if(!request.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(request);
		}
		
		return this.foodVoucherRequestRepository.save(request);
	}
	
	public FoodVoucherRequest findById(Long id) {
		if(id==null) {
			return null;
		}
		
		Optional<FoodVoucherRequest> res = this.foodVoucherRequestRepository.findById(id);
		
		return this.getFromOptional(res);
	}
	
	/**
	 * Use this method to check that the request belong to the user
	 * @param foodVoucherReqId
	 * @param user
	 * @return
	 */
	public FoodVoucherRequest findByIdAndUser(Long foodVoucherReqId, UserProfile user) {
		if(user==null || user==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QFVR.id.eq(foodVoucherReqId));
		cond.and(QFVR.userProfile.id.eq(user.getId()));
		
		FoodVoucherRequest res = this.getFirstResultFromIterable(
				this.foodVoucherRequestRepository.findAll(cond)
			);
	
		return res;
	}
	
	
	public FoodVoucherRequest findByDayAndUser(Date day, UserProfile user) {
		if(day==null || user==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QFVR.day.eq(day));
		cond.and(QFVR.userProfile.id.eq(user.getId()));
		
		FoodVoucherRequest res = this.getFirstResultFromIterable(
					this.foodVoucherRequestRepository.findAll(cond)
				);
		
		return res;
	}

	public void delete(FoodVoucherRequest request) {
		if(request==null) {
			throw new NullPointerException();
		}
		
		this.foodVoucherRequestRepository.delete(request);
	}

	public List<FoodVoucherRequest> findRequestOfUserInDateRange(UserProfile profile, Date from, Date to) {
		if(profile==null || from==null || to==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QFVR.userProfile.id.eq(profile.getId()));
		cond.and(QFVR.day.goe(from));
		cond.and(QFVR.day.loe(to));
		
		List<FoodVoucherRequest> res = this.iterableToList(
									this.foodVoucherRequestRepository.findAll(cond)
								);
		
		return res;
	}

	
	
	
	
	
	
	
}
