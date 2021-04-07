package it.palex.attendanceManagement.core.service.tasks;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.palex.attendanceManagement.data.dto.documents.TicketDownloadDTO;
import it.palex.attendanceManagement.data.dto.tasks.ReportUserTaskDTO;
import it.palex.attendanceManagement.data.dto.transformers.ReportUserTaskTransformer;
import it.palex.attendanceManagement.data.dto.transformers.TicketDownloadTransformer;
import it.palex.attendanceManagement.data.entities.TicketDownload;
import it.palex.attendanceManagement.data.entities.core.ReportUserTask;
import it.palex.attendanceManagement.data.entities.enumTypes.ReportStatusEnum;
import it.palex.attendanceManagement.data.service.core.ReportUserTaskService;
import it.palex.attendanceManagement.data.service.documento.TicketDownloadService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.service.GenericService;

@Service
public class ReportUserTaskWebService implements GenericService {
	
	@Autowired
	private ReportUserTaskService reportUserTaskService;
	
	@Autowired
	private TicketDownloadService ticketDownloadService;
	
	
	@Transactional(rollbackFor = Exception.class)
	public GenericResponse<ReportUserTaskDTO> generateReport(Integer year, Integer month){
		if(year==null || month==null) {
			return this.buildBadDataResponse();
		}
		
		ReportUserTask toStart = new ReportUserTask();
		toStart.setYear(year);
		toStart.setStatus(ReportStatusEnum.TODO.name());
		toStart.setReport(null);
		toStart.setYear(year);
		toStart.setMonth(month);
		toStart.setLogs(null);
		toStart.setDeleted(false);
		
		toStart = this.reportUserTaskService.saveOrUpdate(toStart);
		this.reportUserTaskService.flushChanges();
		
		ReportUserTaskDTO report = ReportUserTaskTransformer.mapToDTO(toStart);
				
		return this.buildOkResponse(report);
	}
	
	
	public GenericResponse<ReportUserTaskDTO> logicalDeleteReport(Long id) {
		if(id==null) {
			return this.buildBadDataResponse();
		}
		ReportUserTask report = this.reportUserTaskService.findById(id);
		
		if(report==null) {
			return this.buildNotFoundResponse();
		}
		
		report.setDeleted(true);
		report = this.reportUserTaskService.saveOrUpdate(report);
		
		ReportUserTaskDTO res = ReportUserTaskTransformer.mapToDTO(report);
		
		return this.buildOkResponse(res);
	}
	
	
	public GenericResponse<Page<ReportUserTaskDTO>> findAll(Boolean includeDeleted, Integer year, Integer month, Pageable pageable){
		if(pageable==null) {
			return this.buildBadDataResponse();
		}
		Pair<List<ReportUserTask>, Long> pair = this.reportUserTaskService.findAllAndCount(includeDeleted, year, month, pageable);
		
		long totalCount = pair.getValue();
		List<ReportUserTaskDTO> list = ReportUserTaskTransformer.mapToDTO(pair.getKey());
		
		return this.buildPageableOkResponse(list, totalCount, pageable);
	}


	public GenericResponse<TicketDownloadDTO> downloadReport(Long reportId) {
		if(reportId==null) {
			return this.buildBadDataResponse();
		}
		ReportUserTask report = this.reportUserTaskService.findById(reportId);
		
		if(report==null) {
			return this.buildNotFoundResponse();
		}
		
		TicketDownload ticket= this.ticketDownloadService.createAndSaveOneShotTicketForDocument(report.getReport());
		
		return this.buildOkResponse(TicketDownloadTransformer.mapToDTO(ticket));
	}
	

}
