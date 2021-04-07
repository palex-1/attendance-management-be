package it.palex.attendanceManagement.batch.report.generator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import it.palex.attendanceManagement.data.entities.CompletedTask;
import it.palex.attendanceManagement.data.entities.Document;
import it.palex.attendanceManagement.data.entities.FoodVoucherRequest;
import it.palex.attendanceManagement.data.entities.GlobalConfigurations;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.ExpenseReport;
import it.palex.attendanceManagement.data.entities.core.ReportUserTask;
import it.palex.attendanceManagement.data.entities.core.WorkTransferRequest;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.entities.enumTypes.ReportStatusEnum;
import it.palex.attendanceManagement.data.entities.enumTypes.StandardDocumentDescription;
import it.palex.attendanceManagement.data.exceptions.InvalidConfigurationException;
import it.palex.attendanceManagement.data.service.core.ExpenseReportService;
import it.palex.attendanceManagement.data.service.core.FoodVoucherRequestService;
import it.palex.attendanceManagement.data.service.core.GlobalConfigurationsService;
import it.palex.attendanceManagement.data.service.core.ReportUserTaskService;
import it.palex.attendanceManagement.data.service.core.WorkTransferRequestService;
import it.palex.attendanceManagement.data.service.documento.DocumentService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.data.service.incarico.CompletedTaskService;

@Component
public class ReportUserTaskGenerator {

	private static final String SUBFOLDER_TO_SAVE_REPORTS = "monthly_report";
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ReportUserTaskGenerator.class);
	
	@Autowired
	private GlobalConfigurationsService globalConfigurationsService;
	
	@Autowired
	private ReportUserTaskService reportUserTaskService;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private CompletedTaskService completedTaskService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private FoodVoucherRequestService foodVoucherRequestService;
	
	@Autowired
	private WorkTransferRequestService workTransferRequestService;
	
	@Autowired
	private ExpenseReportService expenseReportService;
	
	
	
	public ReportUserTask startReportGeneration(ReportUserTask report) {
		
		try {
			report.setStatus(ReportStatusEnum.IN_PROGRESS.name());
			report = this.reportUserTaskService.saveOrUpdateAndFlush(report);
			
			//start generation
			Document documentGenerated = this.executeGeneration(report);
						
			report.setReport(documentGenerated);
			report.setStatus(ReportStatusEnum.COMPLETED.name());
			report = this.reportUserTaskService.saveOrUpdate(report);
			
			return report;
			
		}catch(Exception e) {
			LOGGER.error("", e);
			report.setStatus(ReportStatusEnum.ERROR.name());
			report.setLogs(ExceptionUtils.getStackTrace(e));
			
			this.reportUserTaskService.saveOrUpdate(report);
			
			return null;
		}
	}


	private Document executeGeneration(ReportUserTask task) throws Exception {
		
		GlobalConfigurations config = this.globalConfigurationsService.findByAreaAndKeyAndAssertNotNull(GlobalConfigurationSettingsTuple.REPORT.AREA_NAME, 
				GlobalConfigurationSettingsTuple.REPORT.REPORT_CREATOR);
		
		String value = config.getSettingValue();
		
		ReportGenerator generator = ReportGeneratorFactory.makeGenerator(value);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zipOS = new ZipOutputStream(baos);
		
		int page = 0;
		
		Calendar startRange = Calendar.getInstance();
		startRange.set(Calendar.YEAR, task.getYear());
		startRange.set(Calendar.MONTH, task.getMonth());
		
		startRange.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar endRange = Calendar.getInstance();
		endRange.set(Calendar.YEAR, task.getYear());
		endRange.set(Calendar.MONTH, task.getMonth());
		int lastDayOfMonth = startRange.getActualMaximum(Calendar.DAY_OF_MONTH);
		endRange.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
		
		Sort sort = Sort.by(Direction.DESC, "day");
		
		
		List<UserProfile> users = this.userProfileService.findAllUserWithActiveAccount(
				PageRequest.of(page, 10));
		
		while(!users.isEmpty()) {
			
			for(UserProfile user: users) {
				List<CompletedTask>	completedTasksOfUserInMonth = this.completedTaskService.
						findTaskOfUserInDateRange(user, sort, startRange.getTime(), endRange.getTime());
				
				List<FoodVoucherRequest> foodVoucherReq = this.foodVoucherRequestService
						.findRequestOfUserInDateRange(user, startRange.getTime(), endRange.getTime());
					
				List<WorkTransferRequest> workTransferRequest = this.workTransferRequestService
						.findRequestOfUserInDateRange(user, startRange.getTime(), endRange.getTime());
					 
				List<ExpenseReport> expenseReport = this.expenseReportService
						.findRequestOfUserInDateRange(user, startRange.getTime(), endRange.getTime());
				
				
				Integer userWorkingHours = null;
				if(user.getUserProfileContractInfo()!=null && user.getUserProfileContractInfo().getWorkDayHours()!=null) {
					userWorkingHours = user.getUserProfileContractInfo().getWorkDayHours();
				}
				
				List<Calendar> datesOfYearToBeNotWorked = buildDatesOfYearToBeNotWorked();
				
				Pair<InputStream, String> reportAndExtension = generator.generateReport(user, completedTasksOfUserInMonth, 
						foodVoucherReq, workTransferRequest, expenseReport, datesOfYearToBeNotWorked, 
						task.getYear(), task.getMonth(), userWorkingHours);
				
				zipOS.putNextEntry(new ZipEntry(this.buildFileName(user, reportAndExtension.getValue()))); 
				
				IOUtils.copy(reportAndExtension.getKey(), zipOS);
				
				reportAndExtension.getKey().close();
			}
			
			page++;
			
			 users = this.userProfileService.findAllUserWithActiveAccount(
						PageRequest.of(page, 10));
		}
		
		zipOS.close();
		
		byte[] outputStreamByteStreamByte = baos.toByteArray();
		
		InputStream byteArrayInputStream = new ByteArrayInputStream(outputStreamByteStreamByte);
		
		Document doc = this.documentService.saveFileWithDefaultCryptedFM(
				task.getYear()+"_"+task.getMonth()+"_"+UUID.randomUUID()+".zip", 
				byteArrayInputStream, SUBFOLDER_TO_SAVE_REPORTS, StandardDocumentDescription.MONTHLY_REPORT.name());
		
		byteArrayInputStream.close();
		baos.close();
		
		return doc;
	}
	
	
	private String buildFileName(UserProfile profile, String ext) {
		String fileName = "."+ext;
		if(profile.getId()!=null) {
			fileName = profile.getId() +""+fileName;
		}
		if(profile.getName()!=null) {
			fileName = profile.getName() +"_"+fileName;
		}
		if(profile.getSurname()!=null) {
			fileName = profile.getSurname() +"_"+fileName;
		}
		
		return fileName;
	}
	
	
	private List<Calendar> buildDatesOfYearToBeNotWorked(){
		List<GlobalConfigurations> configs = this.globalConfigurationsService.findAllByArea(GlobalConfigurationSettingsTuple.PUBLIC_HOLIDAYS.AREA_NAME);
		
		List<Calendar> dates = new LinkedList<>();
		
		for (GlobalConfigurations config: configs) {
			String dateValue = config.getSettingValue();
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date;
			try {
				date = sdf.parse(dateValue);
			} catch (ParseException e) {
				LOGGER.error("", e);
				throw new InvalidConfigurationException("Invalid configuration of area:"
						+ GlobalConfigurationSettingsTuple.PUBLIC_HOLIDAYS.AREA_NAME+"\n "
						+ "key:"+config.getSettingKey());
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			
			
			dates.add(cal);
		}

		return dates;	
	}

	
}
