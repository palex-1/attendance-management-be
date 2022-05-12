package it.palex.attendanceManagement.data.entities.core;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
@Entity
@Table(name = "task_expenses")
public class TaskExpenses extends AuditableEntity implements DatabaseCheckableEntity {

	private static final long serialVersionUID = -1682224887713114428L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	public static final int TITLE_MIN_SIZE = 1;
	public static final int TITLE_MAX_SIZE = 255;

	@Basic(optional = false)
	@NotNull
	@Size(min = TITLE_MIN_SIZE, max = TITLE_MAX_SIZE)
	@Column(name = "title")
	private String title;

	public static final int DESCRIPTION_MIN_SIZE = 1;
	public static final int DESCRIPTION_MAX_SIZE = 255;

	@Basic(optional = false)
	@NotNull
	@Size(min = DESCRIPTION_MIN_SIZE, max = DESCRIPTION_MAX_SIZE)
	@Column(name = "description")
	private String description;

	public static final int EXPENSE_TYPE_MIN_SIZE = 1;
	public static final int EXPENSE_TYPE_MAX_SIZE = 255;

	@Basic(optional = false)
	@NotNull
	@Size(min = EXPENSE_TYPE_MIN_SIZE, max = EXPENSE_TYPE_MAX_SIZE)
	@Column(name = "expense_type")
	private String expenseType;

	@Basic(optional = false)
	@NotNull
	@Column(name = "day")
	@Temporal(TemporalType.DATE)
	private Date day;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "amount")
	private Float amount;
	

	@JoinColumn(name = "task_id", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private WorkTask workTask;
	
	

	public TaskExpenses() {
	}

	
	@Override
	public boolean canBeInsertedInDatabase() {
		return isValidTitle(this.title) && isValidDescription(this.description) &&
				isValidDay(this.day) && isValidExpenseType(this.expenseType) &&
				  isValidWorkTask(this.workTask) && isValidAmount(this.amount);
	}


	public String whyCannotBeInsertedInDatabase() {
		return "title:"+this.title+", description:"+this.description
				+", day:"+this.day+", expenseType:"+this.expenseType
				  +"workTask:"+ this.workTask+", amount:"+this.amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public static boolean isValidTitle(String title) {
		if(title==null) {
			return false;
		}
		if( title.length() < TITLE_MIN_SIZE ||  
				title.length() > TITLE_MAX_SIZE){
			return false;
		}
		
		return true;
	}
	
	
	

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public static boolean isValidAmount(Float amount) {
		if(amount==null) {
			return false;
		}
		if(amount.floatValue()<0f) {
			return false;
		}
		return true;
	}
	
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static boolean isValidDescription(String description) {
		if(description==null) {
			return false;
		}
		if( description.length() < DESCRIPTION_MIN_SIZE ||  
				description.length() > DESCRIPTION_MAX_SIZE){
			return false;
		}
		
		return true;
	}
	
	
	
	public String getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}
	
	public static boolean isValidExpenseType(String expenseType) {
		if(expenseType==null) {
			return false;
		}
		if( expenseType.length() < EXPENSE_TYPE_MIN_SIZE ||  
				expenseType.length() > EXPENSE_TYPE_MAX_SIZE){
			return false;
		}
		
		return true;
	}
	
	
	
	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}
	
	public static boolean isValidDay(Date day) {
		return day!=null;
	}
	
	
	
	public WorkTask getWorkTask() {
		return workTask;
	}

	public void setWorkTask(WorkTask workTask) {
		this.workTask = workTask;
	}

	public static boolean isValidWorkTask(WorkTask workTask) {
		return workTask!=null;
	}
	
	
	
	@Override
	public String toString() {
		return "TaskExpenses [id=" + id + "]";
	}

}
