package it.palex.attendanceManagement.core.controllers.rest.endpoints.company;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.palex.attendanceManagement.core.service.company.CompanyCoreService;
import it.palex.attendanceManagement.data.dto.core.CompanyDTO;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * @author Alessandro Pagliaro
 *
 */
@RestController
@RequestMapping(path="company")
public class CompanyController extends RestEndpoint {
	
	@Autowired
	private CompanyCoreService companyCoreSrv;
	
	
	@PostMapping
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.COMPANY_CREATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<CompanyDTO>> create(
							@RequestBody CompanyDTO company){
		GenericResponse<CompanyDTO> response = this.companyCoreSrv.create(company);
		
		return this.buildGenericResponse(response);
	}
	
	@PutMapping
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.COMPANY_UPDATE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<CompanyDTO>> update(
			@RequestBody CompanyDTO company){
		GenericResponse<CompanyDTO> response = this.companyCoreSrv.update(company);
			
		return this.buildGenericResponse(response);
	}
	
	
    
	@GetMapping("findAll")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.COMPANY_READ+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<CompanyDTO>>> findAll(
			@RequestParam(value="name", required=false) String name,
		     @RequestParam(value="description", required=false) String description,
			   @PageableDefault(page = 0, size = 10, sort={"id"},direction=Direction.ASC) Pageable pageable){
		
		GenericResponse<Page<CompanyDTO>> response = this.companyCoreSrv.
				findAll(name, description, pageable);
					
		return this.buildGenericResponse(response);
	}
	
	@DeleteMapping("delete")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.COMPANY_DELETE+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<StringDTO>> delete(
			@RequestParam(value="companyId", required= false) Integer companyId){
		
		GenericResponse<StringDTO> response = this.companyCoreSrv
				.delete(companyId);
					
		return this.buildGenericResponse(response);
	}
	
	
}
