package it.palex.attendanceManagement.library.rest.dtos;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageableCustomDTO {
	
	private static int MIN_SIZE=1;
	private static int MAX_SIZE=2000;
	
	private int page=0;
	private int size=10;
	
	public PageableCustomDTO() {}
	
	public PageableCustomDTO(int page, int size) {
		this.setPage(page);
		this.setSize(size);
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
		
		if(page<0){
			 this.page=0;
		}		
	}
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;

		if(size<MIN_SIZE){
			 this.size=MIN_SIZE;

		}	
		if(size>MAX_SIZE){
			 this.size=MAX_SIZE;
		}
	}
	
	public Pageable toPageable() {			
		return PageRequest.of(this.page, this.size);		
	}
	
}
