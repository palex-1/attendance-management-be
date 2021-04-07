package it.palex.attendanceManagement.data.service.user;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import it.palex.attendanceManagement.data.entities.QUserContacts;
import it.palex.attendanceManagement.data.entities.UserContacts;
import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.enumTypes.ContactTypeEnum;
import it.palex.attendanceManagement.data.exceptions.DataCannotBeInsertedInDatabase;
import it.palex.attendanceManagement.data.repository.auth.UserContactsRepository;
import it.palex.attendanceManagement.library.service.GenericService;
import it.palex.attendanceManagement.library.utils.IterableUtils;

/**
 * 
 * @author Alessandro Pagliaro
 *
 */
@Service
public class UserContactsService implements GenericService {

	private final QUserContacts QUC = QUserContacts.userContacts;
	
	@Autowired
	private UserContactsRepository userContactRepo;
	
	public UserContacts saveOrUpdate(UserContacts userContact) {
		if(userContact==null) {
			throw new NullPointerException();
		}
		if(!userContact.canBeInsertedInDatabase()) {
			throw new DataCannotBeInsertedInDatabase(userContact);
		}
		
		return this.userContactRepo.save(userContact);
	}
	
	public List<UserContacts> findAllContactOfUser(Integer userAuthDetailsID){
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.fkIdUsersAuthDetails.id.eq(userAuthDetailsID));
		
		Iterable<UserContacts> it = this.userContactRepo.findAll(cond);
		
		return IterableUtils.iterableToList(it);
	}
	
	public UserContacts findByKey(Integer userAuthDetailsID, ContactTypeEnum type) {
		String typeName = type==null ? null : type.name();
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.fkIdUsersAuthDetails.id.eq(userAuthDetailsID));
		cond.and(QUC.userContactType.equalsIgnoreCase(typeName));
		
		return this.getFirstResultFromIterable(this.userContactRepo.findAll(cond));
	}
	
	

	
	
	public UserContacts getUserEmail(UsersAuthDetails fkIdUsersAuthDetails) {
		if(fkIdUsersAuthDetails==null) {
			throw new NullPointerException("null fkIdUsersAuthDetails in getUserEmail");
		}

		return getContactOfUser(fkIdUsersAuthDetails, ContactTypeEnum.EMAIL_ADDRESS);
	}
	
	
	public UserContacts getUserPhoneNumber(UsersAuthDetails fkIdUsersAuthDetails) {
		if(fkIdUsersAuthDetails==null) {
			throw new NullPointerException("null fkIdUsersAuthDetails in getUserPhoneNumber");
		}
		
		return getContactOfUser(fkIdUsersAuthDetails, ContactTypeEnum.PHONE_NUMBER);
	}
	
	
	private UserContacts getContactOfUser(UsersAuthDetails fkIdUsersAuthDetails, ContactTypeEnum contactType) {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.fkIdUsersAuthDetails.id.eq(fkIdUsersAuthDetails.getId()));
		cond.and(QUC.userContactType.equalsIgnoreCase(contactType.name()));
		
		Iterator<UserContacts> it = this.userContactRepo.findAll(cond).iterator();
		
		if(it.hasNext()) {
			return it.next();
		}
		
		return null;
	}
	
	
	public boolean isConfirmedAnPhoneNumberForUser(UsersAuthDetails fkIdUsersAuthDetails) {
		if(fkIdUsersAuthDetails==null) {
			throw new NullPointerException("null fkIdUsersAuthDetails in isConfirmedAnPhoneNumberForUser");
		}
		
		UserContacts userContact = this.getUserPhoneNumber(fkIdUsersAuthDetails);
		if(userContact!=null && userContact.getVerified()==true) {
			return true;
		}
		
		return false;
	}

	
	public boolean isConfirmedAnEmailForUser(UsersAuthDetails fkIdUsersAuthDetails) {
		if(fkIdUsersAuthDetails==null) {
			throw new NullPointerException("null fkIdUsersAuthDetails in isConfirmedAnEmailForUser");
		}
		UserContacts userContact = this.getUserEmail(fkIdUsersAuthDetails);
		if(userContact!=null && userContact.getVerified()==true) {
			return true;
		}
		
		return false;
	}
	
	public UserContacts getUserContactByTokenAndUserId(Integer userId, String token) {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.verificationToken.eq(token));
		cond.and(QUC.fkIdUsersAuthDetails.id.eq(userId));
		
		Iterator<UserContacts> it = this.userContactRepo.findAll(cond).iterator();
		
		if(it.hasNext()) {
			return it.next();
		}
		
		return null;
	}
	
	public UserContacts getUserContactByToken(String token) {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.verificationToken.eq(token));
		
		Iterator<UserContacts> it = this.userContactRepo.findAll(cond).iterator();
		
		if(it.hasNext()) {
			return it.next();
		}
		
		return null;
	}

	public void update(UserContacts userContact) {
		if(userContact==null) {
			throw new NullPointerException("null UserContacts in update");
		}
		
		if(!userContact.canBeInsertedInDatabase()) {
			throw new  DataCannotBeInsertedInDatabase(userContact);
		}
		
		this.userContactRepo.save(userContact);
		
	}

	public UserContacts getUserContactByTokenAndUserIdAndType(Integer userId, String token, ContactTypeEnum contactType) {
		String typeName = contactType==null ? null : contactType.name();
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.userContactType.equalsIgnoreCase(typeName));
		cond.and(QUC.verificationToken.eq(token));
		cond.and(QUC.fkIdUsersAuthDetails.id.eq(userId));
		
		Iterator<UserContacts> it = this.userContactRepo.findAll(cond).iterator();
		
		if(it.hasNext()) {
			return it.next();
		}
		
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	public void remove(UserContacts contact) {
		if(contact==null) {
			return;
		}
		//do not make this.userContactRepo.delete(contact)
		this.userContactRepo.deleteByIdCustom(contact.getId());
	}

	public UserContacts getByUserIdAndType(Integer userId, ContactTypeEnum contactType) {
		String typeName = contactType==null ? null : contactType.name();
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.userContactType.equalsIgnoreCase(typeName));
		cond.and(QUC.fkIdUsersAuthDetails.id.eq(userId));
		
		Iterator<UserContacts> it = this.userContactRepo.findAll(cond).iterator();
		
		if(it.hasNext()) {
			return it.next();
		}
		
		return null;
	}

	public boolean isRegisteredAnEmailForUser(UsersAuthDetails user) {
		if(user==null || user.getId()==null) {
			return false;
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.userContactType.equalsIgnoreCase(ContactTypeEnum.EMAIL_ADDRESS.name()));
		cond.and(QUC.fkIdUsersAuthDetails.id.eq(user.getId()));
		
		return this.userContactRepo.count(cond) > 0;
	}

	public List<UserContacts> findByToken(String token) {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.verificationToken.eq(token));
		
		return IterableUtils.iterableToList(this.userContactRepo.findAll(cond));
	}

	public List<UserContacts> findByTokenAndType(String token, String name) {
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.verificationToken.eq(token));
		cond.and(QUC.userContactType.equalsIgnoreCase(name));
		
		return IterableUtils.iterableToList(this.userContactRepo.findAll(cond));
	}

	public int deleteAllExpiredTokens(Date date) {
		return this.userContactRepo.deleteExpiredTokensBefore(date);
	}

	public boolean checkExistancefindByValueIgnoreCase(String value, ContactTypeEnum type) {
		if(value==null || type==null) {
			throw new NullPointerException("null value at checkExistancefindByValueIgnoreCase. value:"+value+" "
					+ "type:"+type);
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.userContactType.equalsIgnoreCase(type.name()));
		cond.and(QUC.cValue.equalsIgnoreCase(value));
		
		return this.userContactRepo.count(cond)>0;
	}

	public boolean checkExistancefindByValueIgnoreCaseExcludingUser(String value, ContactTypeEnum type,
			UserProfile user) {
		if(value==null || type==null) {
			throw new NullPointerException("null value at checkExistancefindByValueIgnoreCase. value:"+value+" "
					+ "type:"+type+", user:"+user);
		}
		BooleanBuilder cond = new BooleanBuilder();
		cond.and(QUC.userContactType.equalsIgnoreCase(type.name()));
		cond.and(QUC.cValue.equalsIgnoreCase(value));
		cond.and(QUC.fkIdUsersAuthDetails.id.ne(user.getId()));
		
		return this.userContactRepo.count(cond)>0;
	}
	
}
