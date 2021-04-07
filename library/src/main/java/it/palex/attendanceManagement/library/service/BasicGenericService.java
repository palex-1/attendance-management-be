package it.palex.attendanceManagement.library.service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import it.palex.attendanceManagement.library.utils.IterableUtils;

public interface BasicGenericService {
	
	default <T> T getFromOptional(Optional<T> optional) {
		if(optional==null || !optional.isPresent()) {
			return null;
		}
		
		return optional.get();
	}
	
	default <T> T getFirstResultFromIterable(Iterable<T> it) {
		if(it==null) {
			return null;
		}
		
		return this.getFirstResultFromIterator(it.iterator());
	}
	
	default <T> List<T> iterableToList(Iterable<T> it) {
		return IterableUtils.iterableToList(it);
	}
	
	default <T> List<T> iteratorToList(Iterator<T> it){
		return IterableUtils.iteratorToList(it);
	}
	
	default <T> T getFirstResultFromIterator(Iterator<T> it) {
		if(it==null) {
			return null;
		}
		if(it.hasNext()) {
			return it.next();
		}
		return null;
	}
}
