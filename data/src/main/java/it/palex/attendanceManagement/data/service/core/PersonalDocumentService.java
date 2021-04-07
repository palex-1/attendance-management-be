package it.palex.attendanceManagement.data.service.core;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.core.PersonalDocument;
import it.palex.attendanceManagement.data.entities.core.PersonalDocumentType;
import it.palex.attendanceManagement.data.entities.core.QPersonalDocument;
import it.palex.attendanceManagement.data.entities.core.QPersonalDocumentType;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.core.PersonalDocumentRepository;
import it.palex.attendanceManagement.library.service.BasicGenericService;

@Service
public class PersonalDocumentService implements BasicGenericService {

	private final QPersonalDocument QPD = QPersonalDocument.personalDocument;
	
	@Autowired
	private PersonalDocumentRepository personalDocumentRepository;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public PersonalDocument saveOrUpdate(PersonalDocument document) {
		if(document==null) {
			throw new NullPointerException();
		}
		if(!document.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(document);
		}
		return this.personalDocumentRepository.save(document);
	}
	
	public PersonalDocument findById(Long id) {
		if(id==null) {
			return null;
		}
		
		return this.getFromOptional(
						this.personalDocumentRepository.findById(id)
					);
	}
	
	/**
	 * Use this method to check that the document belongs to user
	 * @param id
	 * @param profile
	 * @return
	 */
	public PersonalDocument findByIdAndUserProfile(Long id, UserProfile profile) {
		if(id==null || profile==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QPD.id.eq(id));
		cond.and(QPD.userProfile.id.eq(profile.getId()));
				
		return this.getFirstResultFromIterable(
					this.personalDocumentRepository.findAll(cond)
				);
	}
	
	
	public boolean existDocumentTypeForUser(UserProfile profile, Integer documentTypeId) {
		if(profile==null || documentTypeId==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QPD.userProfile.id.eq(profile.getId()));
		cond.and(QPD.personalDocumentType.id.eq(documentTypeId));
		
		return this.personalDocumentRepository.count(cond)>0;
	}
	
	public List<PersonalDocumentType> findAllDocumentNotYetUploadedByUser(UserProfile profile){
		if(profile==null) {
			throw new NullPointerException();
		}
		JPAQuery<PersonalDocumentType> query = new JPAQuery<PersonalDocumentType>(entityManager);
		
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QPD.userProfile.id.eq(profile.getId()));		
		cond.and(QPersonalDocumentType.personalDocumentType.id.eq(
								QPersonalDocument.personalDocument.personalDocumentType.id));
		
		BooleanExpression flagIsPartOfWorkTask = JPAExpressions.selectOne()
	        .from(QPersonalDocument.personalDocument)
	        .where(cond)
	        .notExists();
		
		query.select(QPersonalDocumentType.personalDocumentType)
					.from(QPersonalDocumentType.personalDocumentType)
					.where(flagIsPartOfWorkTask);
		
		return query.fetch();
	}
	
	public List<PersonalDocument> findDocumentsOfUser(UserProfile profile, String documentTypeToContains, Pageable pageable){
		if(profile==null || pageable==null) {
			throw new NullPointerException();
		}
		
		BooleanBuilder cond = buildFindAllDocumentOfUserCondition(profile, documentTypeToContains);
		
		List<PersonalDocument> res = this.iterableToList(
					this.personalDocumentRepository.findAll(cond, pageable)
				);
		
		return res;
	}
	
	private BooleanBuilder buildFindAllDocumentOfUserCondition(UserProfile profile, String documentTypeToContains) {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QPD.userProfile.id.eq(profile.getId()));
		
		if(documentTypeToContains!=null) {
			cond.and(QPD.personalDocumentType.type.equalsIgnoreCase(documentTypeToContains));
		}
		
		return cond;
	}

	public long countDocumentsOfUser(UserProfile profile, String documentTypeToContains){
		if(profile==null) {
			throw new NullPointerException();
		}
		BooleanBuilder cond = buildFindAllDocumentOfUserCondition(profile, documentTypeToContains);
		
		return this.personalDocumentRepository.count(cond);
	}

	public void delete(PersonalDocument document) {
		if(document==null) {
			throw new NullPointerException();
		}
		
		this.personalDocumentRepository.delete(document);
	}
	
}
