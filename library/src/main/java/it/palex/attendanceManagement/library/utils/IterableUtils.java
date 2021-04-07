package it.palex.attendanceManagement.library.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IterableUtils {

	public static <T> List<T>  iteratorToList(Iterator<T> it){
		if(it==null) {
			throw new NullPointerException();
		}
		List<T> res = new ArrayList<>();
		
		while(it.hasNext()) {
			res.add(it.next());
		}
		
		return res;
	}
	
	
	public static <T> List<T> iterableToList(Iterable<T> it){
		if(it==null) {
			throw new NullPointerException();
		}
		return IterableUtils.iteratorToList(it.iterator()); 
	}
	
}
