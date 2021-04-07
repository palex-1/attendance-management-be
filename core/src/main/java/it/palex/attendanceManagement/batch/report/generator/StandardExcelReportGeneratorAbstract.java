package it.palex.attendanceManagement.batch.report.generator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import it.palex.attendanceManagement.commons.utils.tables.CellColor;
import it.palex.attendanceManagement.commons.utils.tables.CellStringValue;
import it.palex.attendanceManagement.commons.utils.tables.CellValue;
import it.palex.attendanceManagement.commons.utils.tables.ColumnLabel;
import it.palex.attendanceManagement.commons.utils.tables.DinamicTable;
import it.palex.attendanceManagement.commons.utils.tables.Table;
import it.palex.attendanceManagement.commons.utils.tables.TableRowDinamic;
import it.palex.attendanceManagement.data.entities.CompletedTask;
import it.palex.attendanceManagement.data.entities.FoodVoucherRequest;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.core.ExpenseReport;
import it.palex.attendanceManagement.data.entities.core.WorkTransferRequest;
import it.palex.attendanceManagement.library.utils.DateUtility;

public abstract class StandardExcelReportGeneratorAbstract {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StandardExcelReportGeneratorAbstract.class);
		
	
	protected ArrayList<Table> buildSheets(UserProfile profile, List<CompletedTask> completedTasksOfUserInMonth, 
			List<FoodVoucherRequest> foodVoucherReq, List<WorkTransferRequest> workTransferRequest, List<ExpenseReport> expenseReport, 
			List<Calendar> datesOfYearToBeNotWorked, int year, int month, Integer userWorkingHours){
		if(profile==null || completedTasksOfUserInMonth==null || foodVoucherReq==null || expenseReport==null || datesOfYearToBeNotWorked==null) {
			throw new NullPointerException("profile:"+profile+", completedTasksOfUserInMonth:"+completedTasksOfUserInMonth+""
					+ ", foodVoucherReq:"+foodVoucherReq+", workTransferRequest:"+workTransferRequest+", expenseReport:"+expenseReport
					+", datesOfYearToBeNotWorked:"+datesOfYearToBeNotWorked);
		}
		
		String name = profile.getName();
		String surname = profile.getSurname();
		
		if(name==null) {
			LOGGER.warn("A report has a user with no name. profile:"+profile);
		}
		if(surname==null) {
			LOGGER.warn("A report has a user with no surname. profile:"+profile);
		}
				
		List<WorkTask> allWorkTask = this.extractAllWorkTaskOfCompletedTask(completedTasksOfUserInMonth);
		this.orderWorkTaskToGetTaskOfAbsenceLast(allWorkTask);
		
		//map of day number and day resume
		Map<Integer, DailyResume> resume = new HashMap<>();
		
		
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.YEAR, year);
		startDate.set(Calendar.MONTH, month);
		
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		
		
		while(month==startDate.get(Calendar.MONTH)) {
			DailyResume dailyResume = this.buildResumeForDate(startDate, completedTasksOfUserInMonth);
			
			resume.put(startDate.get(Calendar.DAY_OF_MONTH), dailyResume);
			
			startDate.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		int totalFoodVoucher = 0;
		int totalWorkedHours = 0;
		int extraTimeTotalHours = 0;
		Map<WorkTask, Integer> totalHoursRegisteredForTaskInMonth = new HashMap<>(); 
		
		DinamicTable table = new DinamicTable("Riepilogo Mensile");
				
		//add blank rows
		table.addBlankRow();
				
		//add 1 to month because numeration start from 0 in java
		this.addDateAndUserData(table, profile.getId(), name, surname, month + 1, year);
		
		//add 2 blank rows
		table.addBlankRows(2);
		
		TableRowDinamic labelTableRow = new TableRowDinamic();
		
		//use the same position in table columns as in the ArrayList of allWorkTask
		//This is important to make calculations
		
		final int START_COLUMN_INDEX = 2;
		
		int columnIndex = START_COLUMN_INDEX;
		labelTableRow.setColumnValue(columnIndex, new ColumnLabel("Giorno", CellColor.AQUA.color));
		columnIndex++;
		
		for(WorkTask task: allWorkTask) {
			labelTableRow.setColumnValue(columnIndex, new ColumnLabel(task.getTaskCode(), CellColor.AQUA.color));
			columnIndex++;
		}
		labelTableRow.setColumnValue(columnIndex, new ColumnLabel("Totale Ore Lavorate", CellColor.AQUA.color));
		columnIndex++;
		labelTableRow.setColumnValue(columnIndex, new ColumnLabel("Straordinario", CellColor.AQUA.color));
		columnIndex++;
		
		labelTableRow.setColumnValue(columnIndex, new ColumnLabel("Buono Pasto", CellColor.AQUA.color));
		columnIndex++;
		
		labelTableRow.setColumnValue(columnIndex, new ColumnLabel("Trasferta", CellColor.AQUA.color));
		columnIndex++;
		
		table.addTableRow(labelTableRow);
		
		
		for (Map.Entry<Integer, DailyResume> entry : resume.entrySet()) {
			columnIndex = START_COLUMN_INDEX;
			TableRowDinamic dailyResumeRow = new TableRowDinamic();
			Integer day = entry.getKey();
			DailyResume currentDayResume = entry.getValue();
			
			//set coloured in red if is a not working
			CellValue dayCellValue = this.buildDayCellValue(year, month, day, datesOfYearToBeNotWorked);
			
			dailyResumeRow.setColumnValue(columnIndex, dayCellValue);
			columnIndex++;
			
			for(WorkTask task: allWorkTask) {
				int workedHoursForTask = currentDayResume.calculateHoursRegisteredForTask(task);
				
				
				if(totalHoursRegisteredForTaskInMonth.containsKey(task)) {
					Integer monthlyResumeOldValue = totalHoursRegisteredForTaskInMonth.get(task);
					totalHoursRegisteredForTaskInMonth.replace(task, workedHoursForTask+monthlyResumeOldValue);
				}else {
					totalHoursRegisteredForTaskInMonth.put(task, workedHoursForTask);
				}
				
				dailyResumeRow.setColumnValue(columnIndex, new CellStringValue(workedHoursForTask));
				columnIndex++;
			}
			
	        int totalWorkedHoursInDay = currentDayResume.calculateOnlyWorkedHours();
	        dailyResumeRow.setColumnValue(columnIndex, new CellStringValue(totalWorkedHoursInDay));
	        columnIndex++;
	        
	        Integer extraHours = -1; 
	        if(userWorkingHours!=null) {
	        	extraHours = 0;
	        	if(totalWorkedHoursInDay>userWorkingHours.intValue()) {
	        		extraHours = (totalWorkedHoursInDay - userWorkingHours); 
	        		extraTimeTotalHours = extraTimeTotalHours + extraHours;
	        	}
	        }
	        
	        dailyResumeRow.setColumnValue(columnIndex, new CellStringValue(extraHours));
	        columnIndex++;
	        
	        //food voucher 
	        FoodVoucherRequest foodRequestInDay = this.findFoodVoucherRequestInDay(foodVoucherReq, day, month, year);
	        if(foodRequestInDay!=null) {
	        	totalFoodVoucher++;
	        	dailyResumeRow.setColumnValue(columnIndex, new CellStringValue("X"));
		        columnIndex++;
	        }else {
	        	dailyResumeRow.setColumnValue(columnIndex, new CellStringValue(""));
		        columnIndex++;
	        }
	        
	        //trasferta
	        WorkTransferRequest workTransferRequestInDay = this.findWorkTransferRequestInDay(workTransferRequest, day, month, year);
	        if(workTransferRequestInDay!=null) {
	        	dailyResumeRow.setColumnValue(columnIndex, new CellStringValue(workTransferRequestInDay.getType()));
		        columnIndex++;
	        }else {
	        	dailyResumeRow.setColumnValue(columnIndex, new CellStringValue(""));
		        columnIndex++;
	        }
	        
	        totalWorkedHours = totalWorkedHours + totalWorkedHoursInDay;
	        
	        table.addTableRow(dailyResumeRow);
	    }
		
		TableRowDinamic dailyResumeRow = this.buildResumeLine(START_COLUMN_INDEX, totalWorkedHours, extraTimeTotalHours, 
				totalFoodVoucher, allWorkTask, totalHoursRegisteredForTaskInMonth);
		
		table.addTableRow(dailyResumeRow);
		
		
		columnIndex = START_COLUMN_INDEX;
		//add 3 blank lines
		table.addBlankRows(3);
		
		//add expense report lines to table
		this.addExpenseResumeReportLines(expenseReport, table, START_COLUMN_INDEX + 1);
		
		
		ArrayList<Table> sheets = new ArrayList<>();
		sheets.add(table);
		
		return sheets;
	}
	
	

	private CellValue buildDayCellValue(int year, int month, int day, List<Calendar> datesOfYearToBeNotWorked) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, 10, 10);
		
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || 
				calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return new ColumnLabel(""+day, CellColor.RED.color);
		}
		
		for (Calendar park : datesOfYearToBeNotWorked) {
			if(park.get(Calendar.YEAR)==year && park.get(Calendar.MONTH)==month && park.get(Calendar.DAY_OF_MONTH)==day) {
				return new ColumnLabel(""+day, CellColor.RED.color);
			}
		}
		
		return new ColumnLabel(""+day, CellColor.WHITE.color);
	}


	private TableRowDinamic buildResumeLine(int startColumnIndex, int totalWorkedHours, int extraTimeTotalHours, int totalFoodVoucher, 
			List<WorkTask> allWorkTask, Map<WorkTask, Integer> totalHoursRegisteredForTaskInMonth) {
		int columnIndex = startColumnIndex;
		TableRowDinamic dailyResumeRow = new TableRowDinamic();
		//add blank for day column
		dailyResumeRow.setColumnValue(columnIndex, new CellStringValue("-"));
		columnIndex++;
		
		for(WorkTask task: allWorkTask) {
			int workedHours = totalHoursRegisteredForTaskInMonth.get(task);
			dailyResumeRow.setColumnValue(columnIndex, new ColumnLabel(""+workedHours, CellColor.GREY_25_PERCENT.color));
			columnIndex++;
		}
		
		dailyResumeRow.setColumnValue(columnIndex, new ColumnLabel(""+totalWorkedHours, CellColor.GREY_25_PERCENT.color));
		columnIndex++;
		
		dailyResumeRow.setColumnValue(columnIndex, new ColumnLabel(""+extraTimeTotalHours, CellColor.GREY_25_PERCENT.color));
		columnIndex++;
		
		dailyResumeRow.setColumnValue(columnIndex, new ColumnLabel(""+totalFoodVoucher, CellColor.GREY_25_PERCENT.color));
		columnIndex++;
		
		return dailyResumeRow;
	}
	
	
	private void addExpenseResumeReportLines(List<ExpenseReport> expenseReport, DinamicTable table, int startColumnIndex) {
		ExpenseReportResume expenseResume = calculateExpenseReportSum(expenseReport);
		
		int columnIndex = startColumnIndex;
		TableRowDinamic titleRow = new TableRowDinamic();
		titleRow.setColumnValue(columnIndex, new ColumnLabel("Riepilogo Nota Spese", CellColor.YELLOW.color));
		columnIndex++;
		
		table.addTableRow(titleRow);
		
		
		columnIndex = startColumnIndex;
		TableRowDinamic totalRequestRow = new TableRowDinamic();
		
		totalRequestRow.setColumnValue(columnIndex, new ColumnLabel("Totale Richiesto", CellColor.GREY_25_PERCENT.color));
		columnIndex++;
		
		totalRequestRow.setColumnValue(columnIndex, new CellStringValue(""+expenseResume.getTotalRequestedExpenses()));
		columnIndex++;
		
		table.addTableRow(totalRequestRow);
		
		
		columnIndex = startColumnIndex;
		TableRowDinamic acceptedRequestRow = new TableRowDinamic();
		
		acceptedRequestRow.setColumnValue(columnIndex, new ColumnLabel("Totale Accettato", CellColor.GREY_25_PERCENT.color));
		columnIndex++;
		
		acceptedRequestRow.setColumnValue(columnIndex, new CellStringValue(""+expenseResume.getTotalAcceptedExpenses()));
		columnIndex++;
		
		table.addTableRow(acceptedRequestRow);
	}
	
	private ExpenseReportResume calculateExpenseReportSum(List<ExpenseReport> expenseReports) {
		double totalAcceptedExpenses = 0d;
		double totalRequestedExpenses = 0d;
		
		for (ExpenseReport expenseReport : expenseReports) {
			totalRequestedExpenses = totalRequestedExpenses + expenseReport.getAmount();
			totalAcceptedExpenses = totalAcceptedExpenses + expenseReport.getAmountAccepted();
		}
		
		return new ExpenseReportResume(totalAcceptedExpenses, totalRequestedExpenses);
	}
	
	
	private WorkTransferRequest findWorkTransferRequestInDay(List<WorkTransferRequest> workTransferRequests, Integer day,
			int month, int year) {
		for (WorkTransferRequest workTransferRequest : workTransferRequests) {
			Calendar dayOfRequest = DateUtility.dateToCalendar(workTransferRequest.getDay());
			
			if(dayOfRequest.get(Calendar.DAY_OF_MONTH)==day && dayOfRequest.get(Calendar.MONTH)==month && 
					dayOfRequest.get(Calendar.YEAR)==year) {
				return workTransferRequest;
			}
		}
		return null;
	}




	private FoodVoucherRequest findFoodVoucherRequestInDay(List<FoodVoucherRequest> foodVoucherReqs, Integer day,
			int month, int year) {
		for (FoodVoucherRequest foodVoucherReq : foodVoucherReqs) {
			Calendar dayOfRequest = DateUtility.dateToCalendar(foodVoucherReq.getDay());
			
			if(dayOfRequest.get(Calendar.DAY_OF_MONTH)==day && dayOfRequest.get(Calendar.MONTH)==month && 
					dayOfRequest.get(Calendar.YEAR)==year) {
				return foodVoucherReq;
			}
		}
		return null;
	}




	private void addDateAndUserData(DinamicTable table, Integer userId, String name, String surname, int month, int year) {
		TableRowDinamic yearTableRow = new TableRowDinamic();
		yearTableRow.setColumnValue(2, new ColumnLabel("Anno", CellColor.GREY_25_PERCENT.color));
		yearTableRow.setColumnValue(3, new CellStringValue(year+""));
		table.addTableRow(yearTableRow);
		
		TableRowDinamic monthTableRow = new TableRowDinamic();
		monthTableRow.setColumnValue(2, new ColumnLabel("Mese", CellColor.GREY_25_PERCENT.color));
		String monthStr = month<10? "0"+month:month+"";
		monthTableRow.setColumnValue(3, new CellStringValue(monthStr));
		table.addTableRow(monthTableRow);
		
		TableRowDinamic nameTableRow = new TableRowDinamic();
		nameTableRow.setColumnValue(2, new ColumnLabel("Nome", CellColor.GREY_25_PERCENT.color));
		nameTableRow.setColumnValue(3, new CellStringValue(name));
		table.addTableRow(nameTableRow);
		
		TableRowDinamic surnameTableRow = new TableRowDinamic();
		surnameTableRow.setColumnValue(2, new ColumnLabel("Cognome", CellColor.GREY_25_PERCENT.color));
		surnameTableRow.setColumnValue(3, new CellStringValue(surname));
		table.addTableRow(surnameTableRow);
		
		TableRowDinamic idTableRow = new TableRowDinamic();
		idTableRow.setColumnValue(2, new ColumnLabel("User ID", CellColor.GREY_25_PERCENT.color));
		idTableRow.setColumnValue(3, new CellStringValue(userId));
		table.addTableRow(idTableRow);
	}
	
	
	/**
	 * COPY THE dateOfDailyResume FOR ANTIALIASING
	 * @param dateOfDailyResume
	 * @param completedTasksOfUserInMonth
	 * @return
	 */
	private DailyResume buildResumeForDate(Calendar dateOfDailyResume, List<CompletedTask> completedTasksOfUserInMonth) {
		DailyResume dailyResume = new DailyResume(DateUtility.copy(dateOfDailyResume));
		
		for (CompletedTask completedTask : completedTasksOfUserInMonth) {
			Calendar dayOfTask = DateUtility.dateToCalendar(completedTask.getDay());
			
			if(dayOfTask.get(Calendar.DAY_OF_MONTH)==dateOfDailyResume.get(Calendar.DAY_OF_MONTH) && 
				 dayOfTask.get(Calendar.MONTH)==dateOfDailyResume.get(Calendar.MONTH) && 
					dayOfTask.get(Calendar.YEAR)==dateOfDailyResume.get(Calendar.YEAR)) {
				dailyResume.addWorkTaskForDay(completedTask.getTaskCode(), completedTask.getWorkedHours().intValue());
			}
		}
		
		return dailyResume;
	}


	private void orderWorkTaskToGetTaskOfAbsenceLast(List<WorkTask> allWorkTask) {
		allWorkTask.sort(new Comparator<WorkTask>() {

			@Override
			public int compare(WorkTask t1, WorkTask t2) {
				boolean t1IsAnAbsenceTask = BooleanUtils.toBoolean(t1.getIsAbsenceTask());
				boolean t2IsAndAbsenceTask = BooleanUtils.toBoolean(t2.getIsAbsenceTask());
				
				if(!t1IsAnAbsenceTask && t2IsAndAbsenceTask) {
					return 1;
				}
				
				if(t1IsAnAbsenceTask && !t2IsAndAbsenceTask) {
					return -1;
				}
				
				return t1.getTaskCode().compareTo(t2.getTaskCode());
			}
		});
	}


	private List<WorkTask> extractAllWorkTaskOfCompletedTask(List<CompletedTask> completedTasksOfUserInMonth) {
		List<WorkTask> allWorkTasks = new ArrayList<>();
		
		for(CompletedTask task: completedTasksOfUserInMonth) {
			WorkTask park = task.getTaskCode();
			
			if(!allWorkTasks.contains(park)) {
				allWorkTasks.add(park);
			}
		}
		
		return allWorkTasks;
	}
	
	
	
	
	
	
	
	
	
	
	
	class ExpenseReportResume {
		double totalAcceptedExpenses = 0d;
		double totalRequestedExpenses = 0d;
		
		public ExpenseReportResume(double totalAcceptedExpenses, double totalRequestedExpenses) {
			this.totalAcceptedExpenses = totalAcceptedExpenses;
			this.totalRequestedExpenses = totalRequestedExpenses;
		}

		public double getTotalAcceptedExpenses() {
			return totalAcceptedExpenses;
		}

		public double getTotalRequestedExpenses() {
			return totalRequestedExpenses;
		}
		
	}
	
	
	
	
	class DailyResume {
		Map<WorkTask, Integer> workedHoursPerTaskInDay = null; 
		Calendar day = null;
		
		public DailyResume(Calendar day){
			this.workedHoursPerTaskInDay = new HashMap<>();
			this.day = day;
		}
		
		/**
		 * This method will return all worked hours without 
		 * @return
		 */
		public int calculateOnlyWorkedHours() {
			int totalWorkedHours = 0;
			for (Map.Entry<WorkTask, Integer> entry : workedHoursPerTaskInDay.entrySet()) {
				if(BooleanUtils.isFalse(entry.getKey().getIsAbsenceTask())) {
					totalWorkedHours = totalWorkedHours + entry.getValue();
				}
			}
			
			return totalWorkedHours;
		}
		
		public int calculateHoursRegisteredForTask(WorkTask task) {
			Integer workedHours = workedHoursPerTaskInDay.get(task);
			if(workedHours!=null) {
				return workedHours;
			}
			return 0;
		}

		public void addWorkTaskForDay(WorkTask task, Integer workedHours){
			Integer hours = workedHoursPerTaskInDay.get(task);
			
			if(hours==null) {
				workedHoursPerTaskInDay.put(task, workedHours);
			}else {
				workedHoursPerTaskInDay.replace(task, hours + workedHours);
			}
		}
		
		
		public Map<WorkTask, Integer> getResume(){
			return workedHoursPerTaskInDay;
		}
		
	}
}
