package it.palex.attendanceManagement.data.utils;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;

public class QueryDslUtils {
	
	
	public static ArrayList<OrderSpecifier> sortToQueryDslOrderSpecifier(Sort sort){
		ArrayList<OrderSpecifier> orders = new ArrayList<>();
		
		if(sort!=null) {
			Iterator<Order> it = sort.iterator();
			while(it.hasNext()) {
				Order park = it.next();
				String orderByField = park.getProperty();
				String orderByDir = park.getDirection().name();
				
				if(orderByField!=null && orderByDir!=null) {
					if(StringUtils.equals(orderByDir, Direction.DESC.name())) {
						orders.add(
								new OrderSpecifier(com.querydsl.core.types.Order.DESC, Expressions.stringPath(orderByField))
							);
					}else {
						orders.add(
								new OrderSpecifier(com.querydsl.core.types.Order.ASC, Expressions.stringPath(orderByField))
							);
					}
				}
			}
		}
		
		return orders;
	}
	
	
	public static ArrayList<OrderSpecifier> convertPageableSortToQueryDslSort(Pageable pageable) {
		if(pageable==null || pageable.getSort()==null) {
			return new ArrayList<>();
		}
		

		return sortToQueryDslOrderSpecifier(pageable.getSort());
	}

}
