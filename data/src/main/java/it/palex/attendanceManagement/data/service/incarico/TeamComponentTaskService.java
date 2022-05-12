package it.palex.attendanceManagement.data.service.incarico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang3.BooleanUtils;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

import it.palex.attendanceManagement.data.aspects.RollbackTransactionForReturnCodes;
import it.palex.attendanceManagement.data.dto.impiegato.ImpiegatoSmallDTO;
import it.palex.attendanceManagement.data.dto.security.GrantedPermissionsDTO;
import it.palex.attendanceManagement.data.dto.tasks.ComponentCreationDTO;
import it.palex.attendanceManagement.data.dto.tasks.ComponenteTeamOutDTO;
import it.palex.attendanceManagement.data.dto.tasks.TeamIncaricoAddComponentsDTO;
import it.palex.attendanceManagement.data.dto.transformers.ImpiegatoTransformer;
import it.palex.attendanceManagement.data.entities.QTeamComponentTask;
import it.palex.attendanceManagement.data.entities.QUserProfile;
import it.palex.attendanceManagement.data.entities.QWorkTask;
import it.palex.attendanceManagement.data.entities.TeamComponentTask;
import it.palex.attendanceManagement.data.entities.TeamRole;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.WorkTask;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.enumTypes.TeamRoleEnum;
import it.palex.attendanceManagement.data.permissionEvaluators.chain.ChainPermissions;
import it.palex.attendanceManagement.data.permissionEvaluators.chain.TeamIncaricoChainPermissionEvaluator;
import it.palex.attendanceManagement.data.repository.auth.UsersAuthDetailsRepository;
import it.palex.attendanceManagement.data.repository.incarico.TeamComponentTaskRepository;
import it.palex.attendanceManagement.data.service.audit.CurrentAuthenticatedUserService;
import it.palex.attendanceManagement.data.service.impiegato.UserProfileService;
import it.palex.attendanceManagement.data.service.user.UserContactsService;
import it.palex.attendanceManagement.data.utils.QueryDslUtils;
import it.palex.attendanceManagement.library.exception.StandardReturnCodesEnum;
import it.palex.attendanceManagement.library.rest.GenericResponse;
import it.palex.attendanceManagement.library.rest.dtos.StringDTO;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.DateUtility;
import it.palex.attendanceManagement.library.utils.HttpCodes;

/**
 * @author Alessandro Pagliaro
 *
 */
@Service
public class TeamComponentTaskService implements GenericService {

	private final QTeamComponentTask QCTI = QTeamComponentTask.teamComponentTask;
	private final QUserProfile QUP = QUserProfile.userProfile;

	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
	private TeamComponentTaskRepository teamComponentTaskRepo;
	
	@Autowired
	private WorkTaskService workTaskService;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private UsersAuthDetailsRepository userAuthDetailsRepo;
	
	@Autowired
	private TeamIncaricoChainPermissionEvaluator teamIncaricoChainPermissionEval;
	
	@Autowired
	private TeamRoleService teamRoleService;
	
	@Autowired
	private CurrentAuthenticatedUserService currentAuthenticatedUserService;
	
	@Autowired
	private UserContactsService userContactsService;
	
	
	
	@Transactional(rollbackOn = Exception.class)
	@RollbackTransactionForReturnCodes(rollbackForCodes = {HttpCodes.CONFLICT, HttpCodes.NOT_FOUND})
	public GenericResponse<StringDTO> addImpiegatiWithGenericRoleToTeam(
			TeamIncaricoAddComponentsDTO componentsToAdd, Long taskId){
		
		if(taskId==null || componentsToAdd==null || componentsToAdd.getComponentsToCreate()==null 
				|| taskId==null || componentsToAdd.getComponentsToCreate().isEmpty()) {
			return this.buildBadDataResponse();
		}
		List<ComponentCreationDTO> impiegatiIds = componentsToAdd.getComponentsToCreate();
		
		WorkTask incarico = this.workTaskService.findById(taskId);
		
		if(incarico==null) {
			return this.buildNotFoundResponse("Not found incarico");
		}
		
		if(BooleanUtils.isTrue(incarico.getIsEnabledForAllUser())) {
			return this.buildConflictEntity(StandardReturnCodesEnum.THE_TASK_IS_ENABLED_FOR_ALL_USER);
		}
		
		Iterator<ComponentCreationDTO> it = impiegatiIds.iterator();
		
		while(it.hasNext()) {
			ComponentCreationDTO toCreate = it.next();
			if(toCreate==null || toCreate.getIdImpiegato()==null) {
				return this.buildBadDataResponse();
			}
			Integer impiegatiId = toCreate.getIdImpiegato();
			UserProfile test = this.userProfileService.findById(impiegatiId);
			if(test==null) {
				return this.buildNotFoundResponse("Not found impiegato with id "+impiegatiId);
			}
		}
		
		TeamRole ruolo = this.teamRoleService.getByRuolo(TeamRoleEnum.RUOLO_GENERICO.name());
		if(ruolo==null) {
			throw new RuntimeException("Broken data! Not found Ruolo team "+TeamRoleEnum.RUOLO_GENERICO.name());
		}
		
		it = impiegatiIds.iterator();
		
		while(it.hasNext()) {
			ComponentCreationDTO toCreate = it.next();
			
			
			TeamComponentTask oldComponent = this.getComponentIfIsOrWasPartOfTheTeam(
					toCreate.getIdImpiegato(), incarico);
			
			//if was a part of the team set flag deleted to false
			if(oldComponent!=null) {
				oldComponent.setDeleted(false);
				this.teamComponentTaskRepo.save(oldComponent);
			}else {
				//if was not part of the team just add 
				this.addTeamComponentTask(toCreate.getIdImpiegato(), incarico, ruolo);
			}
			
			
		}
		
		return this.buildStringOkResponse("Successfully added componenti team");
	}
	
	
	
	
	/**
	 * check before call theis method that user already exists
	 * @param impiegatoId
	 * @param incarico
	 * @param ruolo
	 * @return
	 */
	private boolean addTeamComponentTask(Integer impiegatoId, WorkTask taskCode, TeamRole ruolo) {
		if(impiegatoId==null || taskCode==null || ruolo==null) {
			throw new NullPointerException();
		}
		
		TeamComponentTask componente = new TeamComponentTask();
		
		UserProfile userProfile = this.userProfileService.findById(impiegatoId);
		if(userProfile==null) {
			return false; //impiegato not exists
		}
		componente.setUserProfile(userProfile);
		componente.setTeamRole(ruolo);
		componente.setTaskCode(taskCode);

		if(!componente.canBeInsertedInDatabase()) {
			throw new RuntimeException("Error tring to insert a component in team "+componente);
		}
		
		this.teamComponentTaskRepo.save(componente);
		
		return true;
	}
	
	@Transactional(rollbackOn = Exception.class)
	public GenericResponse<StringDTO> changeRoleInTeam(Long taskId,
			ComponentCreationDTO componentToCreate) {
		if(taskId==null || componentToCreate==null || 
			 componentToCreate.getIdImpiegato()==null || componentToCreate.getRuolo()==null) {
			return this.buildBadDataResponse();
		}
		
		WorkTask incarico = this.workTaskService.findById(taskId);
		if(incarico==null) {
			return this.buildNotFoundResponse("Not found incarico");
		}
		TeamRole ruolo = this.teamRoleService.getByRuolo(componentToCreate.getRuolo());
		if(ruolo==null) {
			return this.buildNotFoundResponse("Not found role");
		}
		
		if(this.isASpecialRole(ruolo)) {
			if(existAUserWithTheRoleInTeam(taskId, componentToCreate.getRuolo())) {
				return this.buildConflictEntity("Is already added a user with the role "+componentToCreate.getRuolo()+" remove that before");
			}
		}

		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QCTI.taskCode.id.eq(taskId));
		condition.and(QCTI.userProfile.id.eq(componentToCreate.getIdImpiegato()));
		condition.and(QCTI.deleted.isFalse());
		
		Iterator<TeamComponentTask> it = this.teamComponentTaskRepo.findAll(condition).iterator();
		if(!it.hasNext()) {
			return this.buildNotFoundResponse("Not found impiegato in team");
		}
		
		TeamComponentTask park = it.next();
		park.setTeamRole(ruolo);
		
		this.teamComponentTaskRepo.save(park);
		
		return this.buildStringOkResponse("Successfully added");
	}
	
	private boolean existAUserWithTheRoleInTeam(Long taskId, String role) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QCTI.taskCode.id.eq(taskId));
		condition.and(QCTI.teamRole.role.eq(role));
		condition.and(QCTI.deleted.isFalse());
		
		Pageable pageble = PageRequest.of(0, 1);

		Iterator<TeamComponentTask> it = this.teamComponentTaskRepo.findAll(condition, pageble)
				.iterator();
		
		if(it.hasNext()) {
			return true;
		}	
		return false;
	}
	
	
	@Transactional(rollbackOn = Exception.class)
	public GenericResponse<StringDTO> removeStandardImpiegatoFromTeam(Long taskCode, 
			Integer impiegatoId){
		return this.removeImpiegatoFromTeam(taskCode, impiegatoId, 
				TeamRoleEnum.RUOLO_GENERICO.name());
	}
	
	
	@Transactional(rollbackOn = Exception.class)
	public GenericResponse<StringDTO> removeSpecialImpiegatoFromTeam(Long taskId, 
			ComponentCreationDTO componentToDelete) {
		if(taskId==null || componentToDelete==null || componentToDelete.getIdImpiegato()==null 
				|| componentToDelete.getRuolo()==null) {
			return this.buildBadDataResponse();
		}
		return this.removeImpiegatoFromTeam(taskId, componentToDelete.getIdImpiegato(), 
				componentToDelete.getRuolo());
	}
	
	private GenericResponse<StringDTO> removeImpiegatoFromTeam(Long taskCode, 
			Integer impiegatoId, String ruolo) {
		
		if(taskCode==null || impiegatoId==null || ruolo==null) {
			return this.buildBadDataResponse();
		}
		
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QCTI.taskCode.id.eq(taskCode));
		condition.and(QCTI.userProfile.id.eq(impiegatoId));
		condition.and(QCTI.teamRole.role.eq(ruolo));
		
		Iterator<TeamComponentTask> it = this.teamComponentTaskRepo.findAll(condition).iterator();
		if(!it.hasNext()) {
			return this.buildNotFoundResponse("Not found impiegato in team");
		}
		
		TeamComponentTask toDelete = it.next();
		
		//when the user is logically deleted set deleted flag to true and role to a generic role
		toDelete.setDeleted(true);
		TeamRole genericRole = this.teamRoleService.getByRuolo(TeamRoleEnum.RUOLO_GENERICO.name());
		toDelete.setTeamRole(genericRole);
		
		this.teamComponentTaskRepo.save(toDelete);
		
		return this.buildStringOkResponse("Successfully removed from team");
	}
	
	
	/**
	 * Do not use this method from RestControllers
	 * @param taskId
	 * @param ruolo
	 * @return
	 */
	public List<TeamComponentTask> getByIncaricoImpiegatiByRole(Long taskId, TeamRoleEnum ruolo){
		BooleanBuilder condition = new BooleanBuilder();
		String ruoloStr = null;
		if(ruolo!=null) {
			ruoloStr = ruolo.name();
		}
		condition.and(QCTI.taskCode.id.eq(taskId));
		condition.and(QCTI.teamRole.role.eq(ruoloStr));
		
		Iterator<TeamComponentTask> it = this.teamComponentTaskRepo.findAll(condition).iterator();
		List<TeamComponentTask> res = new ArrayList<>();
		
		while(it.hasNext()) {
			res.add(it.next());
		}
		
		return res;
	}
	
	public GenericResponse<Page<ComponenteTeamOutDTO>> getAllImpegatiOfIncarico(
			Long taskId, Pageable pageable, String nomeImpiegato, 
				String cognomeImpiegato, String emailImpiegato, String numeroTelefonoImpiegato){
		
		WorkTask incarico = this.workTaskService.findById(taskId);
		
		if(incarico==null) {
			return this.buildNotFoundResponse();
		}
		
		if(incarico.getIsEnabledForAllUser()) {
			return this.buildPageableOkResponse(new ArrayList<>(), 0, pageable, 
									StandardReturnCodesEnum.THE_TASK_IS_ENABLED_FOR_ALL_USER);
		}
		
		BooleanBuilder condition = new BooleanBuilder();
		if(nomeImpiegato!=null) {
			condition.and(QCTI.userProfile.name.containsIgnoreCase(nomeImpiegato));
		}
		if(cognomeImpiegato!=null) {
			condition.and(QCTI.userProfile.surname.containsIgnoreCase(cognomeImpiegato));
		}
		
		if(emailImpiegato!=null) {
			condition.and(QUP.email.containsIgnoreCase(emailImpiegato));
		}
		if(numeroTelefonoImpiegato!=null) {
			condition.and(QUP.phoneNumber.containsIgnoreCase(numeroTelefonoImpiegato));
		}
		
		condition.and(QCTI.taskCode.id.eq(taskId));
		condition.and(QCTI.deleted.isFalse());
		
		long totalCount = this.teamComponentTaskRepo.count(condition);
		
		Iterator<TeamComponentTask> it = this.teamComponentTaskRepo
				.findAll(condition, pageable).iterator();
		
		List<ComponenteTeamOutDTO> res = new ArrayList<>();
		
		while(it.hasNext()) {
			TeamComponentTask teamComp = it.next();
			UserProfile imp = teamComp.getUserProfile();
			
			ImpiegatoSmallDTO dto = this.buildImpiegatoSmallDTO(imp);
			
			ComponenteTeamOutDTO comp = new ComponenteTeamOutDTO();
			comp.setImpiegato(dto);
			comp.setRuolo(teamComp.getTeamRole().getRole());
			res.add(comp);
		}
		
		
		
		return this.buildPageableOkResponse(res, totalCount, pageable);
	}
	
	public GenericResponse<Page<ImpiegatoSmallDTO>> getAllImpiegatiNotOfOfIncarico(
			Long taskId, Pageable pageable, String nomeImpiegato, 
				String cognomeImpiegato, String emailImpiegato, String numeroTelefonoImpiegato){
		
		WorkTask incarico = this.workTaskService.findById(taskId);
		if(incarico==null) {
			return this.buildNotFoundResponse();
		}
		
		//if is enabled for all user all are already added
		if(BooleanUtils.isTrue(incarico.getIsEnabledForAllUser())) {
			return this.buildPageableOkResponse(new ArrayList<>(), 0, pageable, StandardReturnCodesEnum.THE_TASK_IS_ENABLED_FOR_ALL_USER);
		}
				
		BooleanBuilder condition = new BooleanBuilder();
		
		if(nomeImpiegato!=null) {
			condition.and(QUP.name.containsIgnoreCase(nomeImpiegato));
		}
		if(cognomeImpiegato!=null) {
			condition.and(QUP.surname.containsIgnoreCase(cognomeImpiegato));
		}
		
		if(emailImpiegato!=null) {
			condition.and(QUP.email.containsIgnoreCase(emailImpiegato));
		}
		if(numeroTelefonoImpiegato!=null) {
			condition.and(QUP.phoneNumber.containsIgnoreCase(numeroTelefonoImpiegato));
		}
		
		JPQLQuery<Integer> selectAllUserIdOfIncairco = JPAExpressions.select(QUP.id)
				.from(QUP, QCTI)
				.innerJoin(QCTI.userProfile, QUP)
				.on(QCTI.userProfile.id.eq(QUP.id))
				.where(
						QCTI.taskCode.id.eq(taskId)
						.and(QCTI.deleted.isFalse())
						);
		
		JPAQuery<UserProfile> query = new JPAQuery<UserProfile>(entityManager);
		condition.and(
					QUserProfile.userProfile.id.notIn(selectAllUserIdOfIncairco)
				);
		
		query.select(QUP).from(QUP);
		
		query.where(condition);
		
		long totalCount = query.fetchCount();
		
		
		
		query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        
        
        PathBuilder<UserProfile> entityPath = new PathBuilder<>(UserProfile.class, "userProfile");
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Object> path = entityPath.get(order.getProperty());
            
            query.orderBy(new OrderSpecifier(Order.valueOf(order.getDirection().name()), path).nullsLast());
        }
        
        List<UserProfile> list = query.fetch();
        Iterator<UserProfile> it = list.iterator();
        List<ImpiegatoSmallDTO> res = new ArrayList<>();
        
		while(it.hasNext()) {
			UserProfile imp = it.next();
			ImpiegatoSmallDTO dto = buildImpiegatoSmallDTO(imp);
			res.add(dto);
		}
		
		return this.buildPageableOkResponse(res, totalCount, pageable);
	}
	
	public List<WorkTask> findAllWorkTaskOfUser(UserProfile user, String taskDescription,
			String taskCode, Pageable pageable, boolean includeDisabled){
		if(user==null || pageable==null) {
			throw new NullPointerException();
		}
		
		JPAQuery<WorkTask> query = buildFindAllConditionWorkTaskOfUser(user, taskDescription, taskCode, includeDisabled);
			
		ArrayList<OrderSpecifier> orders = QueryDslUtils.convertPageableSortToQueryDslSort(pageable);
		
		if(orders != null && !orders.isEmpty()) {
			OrderSpecifier[] park = new OrderSpecifier[orders.size()];
			query.orderBy(orders.toArray(park));
		}
		
		query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        
        
		return query.fetch();
	}
	
	public long countAllWorkTaskOfUser(UserProfile user, String taskDescription, 
			String taskCode, boolean includeDisabled){
		if(user==null) {
			throw new NullPointerException();
		}
		JPAQuery<WorkTask> query = buildFindAllConditionWorkTaskOfUser(user, taskDescription, taskCode, includeDisabled);
		
		return query.fetchCount();
	}
	
	
	private JPAQuery<WorkTask> buildFindAllConditionWorkTaskOfUser(UserProfile user, String taskDescription, 
			String taskCode, boolean includeDisabled) {
		if(user.getId()==null) {
			throw new NullPointerException();
		}
		
		JPAQuery<WorkTask> query = new JPAQuery<WorkTask>(entityManager);
		
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QCTI.userProfile.id.eq(user.getId()));
		cond.and(QCTI.deleted.isFalse());
		
		if(taskDescription!=null) {
			cond.and(QCTI.taskCode.taskDescription.containsIgnoreCase(taskDescription));
		}
		
		if(taskCode!=null) {
			cond.and(QCTI.taskCode.taskCode.containsIgnoreCase(taskCode));
		}
		
		//make join in exists, check that exists that component 
		cond.and(QTeamComponentTask.teamComponentTask.taskCode.id.eq(QWorkTask.workTask.id));
				
		BooleanExpression flagIsPartOfWorkTask = JPAExpressions.selectOne()
	        .from(QTeamComponentTask.teamComponentTask)
	        .where(cond)
	        .exists();
		
		
		BooleanBuilder workTaskCondition = new BooleanBuilder();
		
		if(!includeDisabled) {
			Date currentDate = DateUtility.getCurrentDateInUTC();
			
			cond.and(QWorkTask.workTask.deactivationDate.isNull().or(
					QWorkTask.workTask.deactivationDate.after(currentDate)));
		}
		
		workTaskCondition.and(QWorkTask.workTask.isEnabledForAllUser.isTrue().or(flagIsPartOfWorkTask));
		
		
		query.select(QWorkTask.workTask)
					.from(QWorkTask.workTask)
					.where(workTaskCondition);
		
		return query;
	}
	
	
	private ImpiegatoSmallDTO buildImpiegatoSmallDTO(UserProfile imp) {
		ImpiegatoSmallDTO dto = ImpiegatoTransformer.mapToSmallDTO(imp);
		
		return dto;
	}

	
	public GenericResponse<GrantedPermissionsDTO> getCurrentUserPermissions(Long taskId){
		WorkTask incarico = this.workTaskService.findById(taskId);
		if(incarico==null) {
			return this.buildNotFoundResponse();
		}
		
		UsersAuthDetails auth = this.currentAuthenticatedUserService.getCurrentAuthenticatedUserAuthDetails();
		if(auth==null) {
			return null;
		}
		Boolean readPermission = 
				this.teamIncaricoChainPermissionEval.checkPermissionWithoutChain(auth, taskId, 
						TeamIncaricoChainPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.READ.name());
		Boolean creationPermission = 
				this.teamIncaricoChainPermissionEval.checkPermissionWithoutChain(auth, taskId, 
				  TeamIncaricoChainPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.CREATE.name());
		Boolean updatePermission = 
			this.teamIncaricoChainPermissionEval.checkPermissionWithoutChain(auth, taskId, 
				TeamIncaricoChainPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.UPDATE.name());
		Boolean deletePermission = 
			this.teamIncaricoChainPermissionEval.checkPermissionWithoutChain(auth, taskId, 
				TeamIncaricoChainPermissionEvaluator.PERMISSION_TO_CHECK, ChainPermissions.DELETE.name());
		
		GrantedPermissionsDTO perm = new GrantedPermissionsDTO();
		perm.setCreationPermission(creationPermission);
		perm.setDeletePermission(deletePermission);
		perm.setReadPermission(readPermission);
		perm.setUpdatePermission(updatePermission);
	
		return this.buildOkResponse(perm);
	}
	
	
	
	/**
	 * 
	 * @param impiegatoId
	 * @param incarico
	 * @return
	 */
	private TeamComponentTask getComponentIfIsOrWasPartOfTheTeam(Integer impiegatoId, WorkTask incarico){
		if(impiegatoId==null || incarico==null) {
			throw new NullPointerException();
		}
		
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QCTI.userProfile.id.eq(impiegatoId));
		condition.and(QCTI.taskCode.taskCode.equalsIgnoreCase(incarico.getTaskCode()));
		
		return this.getFirstResultFromIterable(this.teamComponentTaskRepo.findAll(condition));
	}
	
	public boolean isPartOfTheTeam(UserProfile profile, WorkTask workTask) {
		if(profile==null || workTask==null) {
			throw new NullPointerException();
		}
		
		if(BooleanUtils.isTrue(workTask.getIsEnabledForAllUser())) {
			return true;
		}
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QCTI.taskCode.id.eq(workTask.getId()));
		condition.and(QCTI.userProfile.id.eq(profile.getId()));
		condition.and(QCTI.deleted.isFalse());	
		
		return this.teamComponentTaskRepo.count(condition)>0;
	}
	
	public boolean isPartOfTheTeam(String username, Long taskId) {
		return this.teamComponentTaskRepo.isPartOfTheTeam(username, taskId);
	}
	
	/**
	 * The method will check if the user was not deleted from team
	 * @param username
	 * @param codiceIncarico
	 * @return
	 */
	private TeamComponentTask getByUsernameAndIncaricoCode(String username, 
			Long taskId) {
		if(username==null || taskId==null) {
			throw new NullPointerException();
		}
		
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QCTI.taskCode.id.eq(taskId));
		condition.and(QCTI.userProfile.usersAuthDetails.username.equalsIgnoreCase(username));
		condition.and(QCTI.deleted.isFalse());
		
		Iterator<TeamComponentTask> it = this.teamComponentTaskRepo.findAll(condition).iterator();
		
		if(it.hasNext()) {
			return it.next();
		}
		return null;
	}
	
	
	public boolean isSpecialPartOfTheTeam(String username, Long taskId) {
		TeamComponentTask park = this.getByUsernameAndIncaricoCode(username, taskId);
		if(park==null) {
			return false;
		}
		TeamRole ruoloUserProfile = park.getTeamRole();
		
		if(StringUtils.equals(ruoloUserProfile.getRole(), TeamRoleEnum.QA_REVIEWER.name()) || 
		   StringUtils.equals(ruoloUserProfile.getRole(), TeamRoleEnum.DELIVERY_MANAGER.name()) || 
		   StringUtils.equals(ruoloUserProfile.getRole(), TeamRoleEnum.PROJECT_MANAGER.name()) || 
		   StringUtils.equals(ruoloUserProfile.getRole(), TeamRoleEnum.ACCOUNT_MANAGER.name()) ) {
			return true;
		}
		
		return false;
	}


	private boolean isASpecialRole(TeamRole ruoloUserProfile) {
		return this.isASpecialRole(ruoloUserProfile.getRole());
	}
	
	private boolean isASpecialRole(String role) {
		return this.teamComponentTaskRepo.isASpecialRole(role);
	}



	private BooleanBuilder buildFindAllEmployeeOfTaskCondition(WorkTask task) {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QCTI.taskCode.id.eq(task.getId()));
		
		return cond;
	}

	public List<TeamComponentTask> findAllEmployeeOfTask(WorkTask task, Pageable pageable) {
		if(task==null || pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = buildFindAllEmployeeOfTaskCondition(task);
		
		return this.iterableToList(
					this.teamComponentTaskRepo.findAll(cond, pageable)
				);
	}

	public long countAllEmployeeOfTask(WorkTask task) {
		if(task==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = buildFindAllEmployeeOfTaskCondition(task);
		
		return this.teamComponentTaskRepo.count(cond);
	}
	
	
}
