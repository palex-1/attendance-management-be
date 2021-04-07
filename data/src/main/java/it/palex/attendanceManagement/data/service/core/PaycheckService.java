package it.palex.attendanceManagement.data.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.Paycheck;
import it.palex.attendanceManagement.data.entities.core.QPaycheck;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.PaycheckRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class PaycheckService implements BasicGenericService {

	private final QPaycheck QP = QPaycheck.paycheck;
	
	@Autowired
	private PaycheckRepository paycheckRepository;
	
	
	public Paycheck saveOrUpdate(Paycheck paycheck) {
		if(paycheck==null) {
			throw new NullPointerException();
		}
		if(!paycheck.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(paycheck);
		}
		return this.paycheckRepository.save(paycheck);
	}
	
	public Paycheck findById(Long id) {
		if(id==null) {
			return null;
		}
		
		return this.getFromOptional(
						this.paycheckRepository.findById(id)
					);
	}
	
	/**
	 * Use this function to check that user is the owner of the paycheck
	 * @param paycheckId
	 * @param profile
	 * @return
	 */
	public Paycheck findByIdAndUserProfile(Long paycheckId, UserProfile profile) {
		if(paycheckId==null || profile==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QP.id.eq(paycheckId));
		cond.and(QP.userProfile.id.eq(profile.getId()));
		
		return this.getFirstResultFromIterable(
					this.paycheckRepository.findAll(cond)
				);
	}
	
	
	
	private BooleanBuilder buildFindAllPaycheckOfUserCondition(UserProfile profile, Integer year, Integer month) {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QP.userProfile.id.eq(profile.getId()));
		
		if(year!=null) {
			cond.and(QP.year.eq(year));
		}
		
		if(month!=null) {
			cond.and(QP.month.eq(month));
		}
		
		return cond;
	}
	
	public List<Paycheck> findAllPaycheckOfUser(UserProfile profile, Integer year, 
			Integer month, Pageable pageable){
		if(profile==null || pageable==null) {
			throw new NullPointerException();
		}
		
		BooleanBuilder cond = buildFindAllPaycheckOfUserCondition(profile, year, month);
		
		List<Paycheck> res = this.iterableToList(
					this.paycheckRepository.findAll(cond, pageable)
				);
		
		return res;
	}
	
	public long countAllPaycheckOfUser(UserProfile profile, Integer year, 
			Integer month){
		if(profile==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = buildFindAllPaycheckOfUserCondition(profile, year, month);
		
		return this.paycheckRepository.count(cond);
	}

	public boolean checkExistanceOfAnotherPaycheckInMonthAndDateForUser(UserProfile profile, 
			Integer year, Integer month) {
		if(profile==null || year==null || month==null) {
			throw new NullPointerException();
		}
		
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QP.userProfile.id.eq(profile.getId()));
		cond.and(QP.year.eq(year));
		cond.and(QP.month.eq(month));
		
		return this.paycheckRepository.count(cond)>0;
	}

	public void delete(Paycheck paycheck) {
		if(paycheck==null) {
			throw new NullPointerException();
		}
		this.paycheckRepository.delete(paycheck);
	}

	
	
	
}
