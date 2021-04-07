package it.palex.attendanceManagement.data.service.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;

import it.palex.attendanceManagement.data.entities.GlobalConfigurations;
import it.palex.attendanceManagement.data.entities.QGlobalConfigurations;
import it.palex.attendanceManagement.data.entities.enumTypes.GlobalConfigurationSettingsTuple;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.exceptions.InvalidConfigurationException;
import it.palex.attendanceManagement.data.repository.configuration.GlobalConfigurationsRepository;
import it.palex.attendanceManagement.data.utils.QueryDslUtils;
import it.palex.attendanceManagement.library.service.BasicGenericService;
import it.palex.attendanceManagement.library.utils.IterableUtils;
import it.palex.attendanceManagement.library.utils.StringUtility;

@Service
public class GlobalConfigurationsService implements BasicGenericService {

	private final QGlobalConfigurations QGC = QGlobalConfigurations.globalConfigurations;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private GlobalConfigurationsRepository globalConfRepo;
	
	public GlobalConfigurations saveOrUpdate(GlobalConfigurations publicKeys) {
		if(publicKeys==null) {
			throw new NullPointerException();
		}
		if(!publicKeys.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(publicKeys);
		}
		
		return this.globalConfRepo.save(publicKeys);
	}
	
	public List<GlobalConfigurations> findAllByArea(String area) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QGC.settingArea.equalsIgnoreCase(area));
		
		Iterable<GlobalConfigurations> it = this.globalConfRepo.findAll(condition);
		
		return IterableUtils.iterableToList(it);
	}
	
	public GlobalConfigurations findByAreaAndKey(String area, String key) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QGC.settingArea.equalsIgnoreCase(area));
		condition.and(QGC.settingKey.equalsIgnoreCase(key));
		
		Iterator<GlobalConfigurations> it = this.globalConfRepo.findAll(condition).iterator();
		
		if(!it.hasNext()) {
			return null;
		}
		
		return it.next();
	}
	
	public String getConfigValue(String area, String key) {
		BooleanBuilder condition = new BooleanBuilder();
		condition.and(QGC.settingArea.equalsIgnoreCase(area));
		condition.and(QGC.settingKey.equalsIgnoreCase(key));
		
		Iterator<GlobalConfigurations> it = this.globalConfRepo.findAll(condition).iterator();
		
		if(!it.hasNext()) {
			return null;
		}
		
		return it.next().getSettingValue();
		
	}

	public boolean isEnabledUniqueContactForUserConfiguration() {
		String uniqueConfig = this.getConfigValue(
				GlobalConfigurationSettingsTuple.REGISTRATION_SETTING.AREA_NAME, 
				GlobalConfigurationSettingsTuple.REGISTRATION_SETTING.UNIQUE_CONTACTS);
				
		if (StringUtility.isEmptyOrWhitespace(uniqueConfig)) {
			throw new InvalidConfigurationException("Invalid configuration of area:"
							+ GlobalConfigurationSettingsTuple.REGISTRATION_SETTING.AREA_NAME+"\n "
							+ "key:"+GlobalConfigurationSettingsTuple.REGISTRATION_SETTING.UNIQUE_CONTACTS);
		}
				
		return BooleanUtils.toBoolean(StringUtils.trim(uniqueConfig));
	}

	/**
	 * 
	 * @param areaName
	 * @param key
	 * @return
	 * @throws InvalidConfigurationException if no configuration with areaName and key was found
	 */
	public GlobalConfigurations findByAreaAndKeyAndAssertNotNull(String areaName, String key) {
		GlobalConfigurations config = this.findByAreaAndKey(areaName, key);
		
		if(config==null) {
			throw new InvalidConfigurationException("Not found config --> area="+areaName+","+ "key="+key);
		}
		
		return config;
		
	}

	public Pair<List<String>, Long> findAllAreasAndCount(String area, boolean includeNotVisible, Pageable pageable){
		JPAQuery<String> query = new JPAQuery<>(em);
		
		BooleanBuilder cond = new BooleanBuilder();
		if(area!=null) {
			cond.and(QGC.settingArea.containsIgnoreCase(area));
		}		
		if(!includeNotVisible) {
			cond.and(QGC.visible.isTrue());	
		}
		
		query.from(QGlobalConfigurations.globalConfigurations)
		.select(QGlobalConfigurations.globalConfigurations.settingArea)
		.where(cond)
		.distinct();
		
		long totalCount = query.fetchCount();
				
		query = query.offset(pageable.getPageNumber() * pageable.getPageSize());
		query = query.limit(pageable.getPageSize());

		
		ArrayList<OrderSpecifier> orderby = QueryDslUtils.sortToQueryDslOrderSpecifier(pageable.getSort());
		
		if(orderby != null && !orderby.isEmpty()) {
			OrderSpecifier[] park = new OrderSpecifier[orderby.size()];
			query.orderBy(orderby.toArray(park));
		}
		
		List<String> list = query.fetch();
				
		return Pair.of(list, totalCount);
	}

	public Pair<List<GlobalConfigurations>, Long> findAllByAreaAndCount(String area, String key, boolean includeNotVisible, Pageable pageable) {
		if(area==null || pageable==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QGC.settingArea.equalsIgnoreCase(area));
		
		if(key!=null) {
			cond.and(QGC.settingKey.containsIgnoreCase(key));
		}
		if(!includeNotVisible) {
			cond.and(QGC.visible.isTrue());	
		}
		
		long totalCount = this.globalConfRepo.count(cond);
		
		List<GlobalConfigurations> list = this.iterableToList(
					this.globalConfRepo.findAll(cond, pageable)
				);
		
		return Pair.of(list, totalCount);
	}

	/**
	 * 
	 * @param area
	 * @return the count of deleted configurations
	 */
	public long deleteArea(String area) {
		if(area==null) {
			throw new NullPointerException();
		}
		JPADeleteClause delete = new JPADeleteClause(em, QGlobalConfigurations.globalConfigurations);
		delete.where(QGlobalConfigurations.globalConfigurations.settingArea.equalsIgnoreCase(area));
		
		return delete.execute();
	}

	
	public void delete(GlobalConfigurations config) {
		if(config==null) {
			throw new NullPointerException();
		}
		this.globalConfRepo.delete(config);
	}

	public GlobalConfigurations findById(Integer id) {
		if(id==null) {
			return null;
		}
		return this.getFromOptional(
					this.globalConfRepo.findById(id)
				);
	}
	
	
}
