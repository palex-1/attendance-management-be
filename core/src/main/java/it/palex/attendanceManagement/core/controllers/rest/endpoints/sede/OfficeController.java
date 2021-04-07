package it.palex.attendanceManagement.core.controllers.rest.endpoints.sede;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import it.palex.attendanceManagement.data.dto.sede.SedeLavorativaInDTO;
import it.palex.attendanceManagement.data.dto.sede.SedeLavorativaOutDTO;
import it.palex.attendanceManagement.data.dto.sede.SedeLavorativaUpdateDTO;
import it.palex.attendanceManagement.data.entities.Office;
import it.palex.attendanceManagement.data.entities.enumTypes.AuthoritiesValues;
import it.palex.attendanceManagement.data.service.sede.OfficeService;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.RestEndpoint;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.utils.aspects.Loggable;

/**
 * @author Alessandro Pagliaro
 *
 */
@RestController
@RequestMapping(path="sede")
public class OfficeController extends RestEndpoint {
	
	@Autowired
	private OfficeService sedeLavoSrv;
	
	
	@PostMapping
	@PreAuthorize("hasAuthority('SEDE_LAVORATIVA_CREATE')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<SedeLavorativaOutDTO>> create(
							@RequestBody SedeLavorativaInDTO sede){
		GenericResponse<SedeLavorativaOutDTO> response = this.sedeLavoSrv.create(sede);
		
		return this.buildGenericResponse(response);
	}
	
	
	@GetMapping(path="findAllEmploymentOffice")
	@PreAuthorize("hasAuthority('"+AuthoritiesValues.LOGGED_USER_PERMISSION+"')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<StringDTO>>> findAllEmploymentOffice(){
		GenericResponse<List<StringDTO>> response = this.sedeLavoSrv.findAllEmploymentOffice();
		
		return this.buildGenericResponse(response);
	}
	
	
	@PutMapping
	@PreAuthorize("hasAuthority('SEDE_LAVORATIVA_UPDATE')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<SedeLavorativaOutDTO>> update(
			@RequestBody SedeLavorativaUpdateDTO sede){
		GenericResponse<SedeLavorativaOutDTO> response = this.sedeLavoSrv.update(sede);
			
		return this.buildGenericResponse(response);
	}
	
	
    
	@GetMapping
	@PreAuthorize("hasAuthority('SEDE_LAVORATIVA_READ')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<Page<SedeLavorativaOutDTO>>> findAll(
			@RequestParam(value="nomeSedeCUSTOMIZED", required=false) String nomeSede,
		     @RequestParam(value="viaCUSTOMIZED", required=false) String via,
		      @RequestParam(value="companyBranchType", required=false) String companyBranchType,
		       @RequestParam(value="cittaCUSTOMIZED", required=false) String citta,
		        @RequestParam(value="provinciaCUSTOMIZED", required=false) String provincia,
		         @RequestParam(value="nazioneCUSTOMIZED", required=false) String nazione,
		          @RequestParam(value="capCUSTOMIZED", required=false) String cap,
				   @PageableDefault(page = 0, size = 10, sort={"id"},direction=Direction.ASC) Pageable pageable,
            		@QuerydslPredicate(root = Office.class) Predicate predicate){
		GenericResponse<Page<SedeLavorativaOutDTO>> response = this.sedeLavoSrv.
					findAll(pageable, predicate, nomeSede, via, companyBranchType, citta, provincia, 
							nazione, cap);
					
		return this.buildGenericResponse(response);
	}
	
	@GetMapping("/tipoSedi")
	@PreAuthorize("hasAuthority('SEDE_LAVORATIVA_READ')")
	@Loggable(logExecutionTime = true, logParameters = false, logResponseParameter = false)
	public ResponseEntity<GenericResponse<List<StringDTO>>> findAllTipoSedi(){
		GenericResponse<List<StringDTO>> response = this.sedeLavoSrv.getAllTipoSedi();
			
		return this.buildGenericResponse(response);
	}
	
}
