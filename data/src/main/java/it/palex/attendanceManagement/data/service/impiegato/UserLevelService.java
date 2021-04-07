package it.palex.attendanceManagement.data.service.impiegato;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.core.QUserLevel;
import it.palex.attendanceManagement.data.entities.core.UserLevel;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.UserLevelRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class UserLevelService implements BasicGenericService {

	private final QUserLevel QUL = QUserLevel.userLevel;
	
	@Autowired
	private UserLevelRepository userLevelRepository;
	
	
	public UserLevel saveOrUpdate(UserLevel level) {
		if(level==null) {
			throw new NullPointerException();
		}
		if(!level.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase();
		}
		
		return this.userLevelRepository.save(level);
	}
	
	public UserLevel findById(Integer id) {
		if(id==null) {
			return null;
		}
		
		return this.getFromOptional(
					this.userLevelRepository.findById(id)
				);
	}
	
	
	public List<UserLevel> findAll(){
		return this.userLevelRepository.findAll();
	}


	public UserLevel findByLevel(String level) {
		if(level==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUL.level.equalsIgnoreCase(level));
		
		return this.getFirstResultFromIterable(
					this.userLevelRepository.findAll(cond)
				);
	}

	public Pair<List<UserLevel>, Long> findAllAndCount(String levelFilter, Pageable pageable) {
		if(pageable==null) {
			throw new NullPointerException();
		}
		
		BooleanBuilder cond = new BooleanBuilder();
		
		if(levelFilter!=null) {
			cond.and(QUL.level.containsIgnoreCase(levelFilter));
		}
		
		long totalCount = this.userLevelRepository.count(cond);
		
		List<UserLevel> res = this.iterableToList(
									this.userLevelRepository.findAll(cond, pageable)
								);
		
		return Pair.of(res, totalCount);
	}

	public void delete(UserLevel levelToDelete) {
		if(levelToDelete==null) {
			throw new NullPointerException();
		}
		
		this.userLevelRepository.delete(levelToDelete);
	}


	
	
	
	
}
