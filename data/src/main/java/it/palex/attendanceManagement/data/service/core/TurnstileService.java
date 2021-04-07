package it.palex.attendanceManagement.data.service.core;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.core.QTurnstile;
import it.palex.attendanceManagement.data.entities.core.Turnstile;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.TurnstileRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class TurnstileService implements BasicGenericService {

	private final QTurnstile QT = QTurnstile.turnstile;
	
	@Autowired
	private TurnstileRepository turnstileRepository;
	
	
	public Turnstile saveOrUpdate(Turnstile turnstile) {
		if(turnstile==null) {
			throw new NullPointerException();
		}
		if(!turnstile.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase();
		}
		return this.turnstileRepository.save(turnstile);
	}
	
	
	public Turnstile findById(Long id) {
		if(id==null) {
			return null;
		}
		Optional<Turnstile> opt = this.turnstileRepository.findById(id);
		
		return this.getFromOptional(opt);
	}
	
	public Pair<List<Turnstile>, Long> findAllAndCount(String title, String position, String description, 
			boolean includeDisabled, Pageable pageable){
		if(pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		
		if(title!=null) {
			cond.and(QT.title.containsIgnoreCase(title));
		}
		
		if(position!=null) {
			cond.and(QT.position.containsIgnoreCase(position));
		}
		
		if(description!=null) {
			cond.and(QT.description.containsIgnoreCase(description));
		}
			
		if(!includeDisabled) {
			cond.and(QT.deactivated.isFalse());
		}
		
		long count = this.turnstileRepository.count(cond);
		List<Turnstile> list = this.iterableToList(this.turnstileRepository.findAll(cond, pageable));
				
		return Pair.of(list, count);
	}


	
	
	
	
}
