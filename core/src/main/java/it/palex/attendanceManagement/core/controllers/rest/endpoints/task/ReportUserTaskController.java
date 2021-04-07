package it.palex.attendanceManagement.core.controllers.rest.endpoints.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.service.tasks.ReportUserTaskWebService;
import it.palex.attendanceManagement.data.dto.documents.TicketDownloadDTO;
import it.palex.attendanceManagement.data.dto.tasks.ReportUserTaskDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

@RestController
@RequestMapping(path="reports")
public class ReportUserTaskController extends RestEndpoint {

	@Autowired
	private ReportUserTaskWebService reportUserTaskWebService;
	
	@GetMapping(path = "/findAll")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.REPORT_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<ReportUserTaskDTO>>> findAll(
			@RequestParam(name = "month", required = false) Integer month,
			@RequestParam(name = "year", required = false) Integer year,
			@RequestParam(name = "includeDeleted", defaultValue = "true") Boolean includeDeleted,
			@PageableDefault(page = 0, size = 10, sort={"year","month", "createdDate"},direction=Direction.DESC) Pageable pageable) {
		
		GenericResponse<Page<ReportUserTaskDTO>> res = this.reportUserTaskWebService
				.findAll(includeDeleted, year, month, pageable);
		
		return this.buildGenericResponse(res);
	}
	
	
	@PostMapping(path="create")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.REPORT_CREATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<ReportUserTaskDTO>> generateReport(
			@RequestParam(name = "month", required = true) Integer month,
			@RequestParam(name = "year", required = true) Integer year) {
		
		GenericResponse<ReportUserTaskDTO> res = this.reportUserTaskWebService
				.generateReport(year, month);
		
		return this.buildGenericResponse(res);
	}
	
	
	@DeleteMapping(path="delete")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.REPORT_DELETE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<ReportUserTaskDTO>> logicalDeleteReport(
			@RequestParam(name = "reportId", required = true) Long reportId) {
		
		GenericResponse<ReportUserTaskDTO> res = this.reportUserTaskWebService
				.logicalDeleteReport(reportId);
		
		return this.buildGenericResponse(res);
	}
	
	
	@GetMapping(path = "/download")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.REPORT_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<TicketDownloadDTO>> downloadUserPaycheck(
			@RequestParam(name = "reportId", required = true) Long reportId) {
		
		GenericResponse<TicketDownloadDTO> res = this.reportUserTaskWebService
				.downloadReport(reportId);
		
		return this.buildGenericResponse(res);
	}
}
