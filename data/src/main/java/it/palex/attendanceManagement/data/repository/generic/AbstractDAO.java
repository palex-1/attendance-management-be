package it.palex.attendanceManagement.data.repository.generic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public abstract class AbstractDAO<T> {

	@SuppressWarnings("unused")
	private Class<T> entityClass;

    public abstract EntityManager getEntityManager();
    
    public AbstractDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
        
    /**
     * 
     * @param query
     * @return a list of the results
     * @throws IllegalStateException - if called for a Java Persistence query language UPDATE or DELETE statement
	 * @throws QueryTimeoutException - if the query execution exceeds the query timeout value set and only the statement is rolled back
	 * @throws TransactionRequiredException - if a lock mode has been set and there is no transaction
	 * @throws PessimisticLockException - if pessimistic locking fails and the transaction is rolled back
	 * @throws LockTimeoutException - if pessimistic locking fails and only the statement is rolled back
	 * @throws PersistenceException - if the query execution exceeds the query timeout value set and the transaction is rolled back
     */
    public List<T> find(TypedQuery<T> query) {
        return query.getResultList();
    }
        
    /**
     * 
     * @param entity
     * @throws IllegalArgumentException - if the instance is not an entity or is a detached entity
     * @throws TransactionRequiredException - if invoked on a container-managed entity manager of type PersistenceContextType.TRANSACTION and there is no transaction
     * @throws EntityNotFoundException - if the entity no longer exists in the database
     */
    public void refresh(T entity){
        getEntityManager().refresh(getEntityManager().merge(entity));
    }
        
    /**
     * @param query
     * @return the number of entities updated, deleted or their count 
     * @throws ConstraintViolationException - if some entities are linked to the deleted, cascade delete not works, caused by caching
     * @throws IllegalStateException - if called for a Java Persistence query language SELECT statement or for a criteria query
     * @throws TransactionRequiredException - if there is no transaction
     * @throws QueryTimeoutException - if the statement execution exceeds the query timeout value set and only the statement is rolled back
     * @throws PersistenceException - if the query execution exceeds the query timeout value set and the transaction is rolled back
     */
    public int executeDeleteOrUpdateQuery(Query query){
    	int num = query.executeUpdate();
    	return num;
    }
        
    /**
     * 
     * @param query select count query
     * @return the count of entities
     * @throws NoResultException - if there is no result
     * @throws NonUniqueResultException - if more than one result
     * @throws IllegalStateException - if called for a Java Persistence query language UPDATE or DELETE statement
     * @throws QueryTimeoutException - if the query execution exceeds the query timeout value set and only the statement is rolled back
     * @throws TransactionRequiredException - if a lock mode has been set and there is no transaction
     * @throws PessimisticLockException - if pessimistic locking fails and the transaction is rolled back
     * @throws LockTimeoutException - if pessimistic locking fails and only the statement is rolled back
     * @throws PersistenceException - if the query execution exceeds the query timeout value set and the transaction is rolled back
     */
    public long executeCountQuery(TypedQuery<Long> query){
    	long num = query.getSingleResult();
    	
		return num;
    }
    
    
    
    
    protected Character objectToChar(Object o) {
    	if(o==null) {
			return null;
		}
		if(! (o instanceof Character)) {
			throw new IllegalArgumentException("Not a Character "+o.getClass());
		}
		return (Character)o;
    }
    
    protected String objectToString(Object o) {
		if(o==null) {
			return null;
		}
		if(! (o instanceof String)) {
			throw new IllegalArgumentException("Not a string "+o.getClass());
		}
		return (String)o;
	}
	
    protected Integer objectToInteger(Object o) {
		if(o==null) {
			return null;
		}
		if(! (o instanceof Integer)) {
			throw new IllegalArgumentException("Not a Integer "+o.getClass());
		}
		return (Integer)o;
	}

	protected Double objectToDouble(Object o) {
		if(o==null) {
			return null;
		}
		if(! (o instanceof Double)) {
			throw new IllegalArgumentException("Not a Double "+o.getClass());
		}
		return (Double)o;
	}
    
	protected Long bigDecimalToLong(Object o) {
		if(o==null) {
			return null;
		}
		if(! (o instanceof BigDecimal) && !(o instanceof Long)) {
			throw new IllegalArgumentException("Not a BigDecimal or Long"+o.getClass());
		}
		
		if(o instanceof BigDecimal) {
			BigDecimal park = (BigDecimal) o;
			
			return park.longValue();
		}
		
		return (Long) o;
	}
	
	protected Double bigDecimalToDouble(Object o) {
		if(o==null) {
			return null;
		}
		if(! (o instanceof BigDecimal) && !(o instanceof Double)) {
			throw new IllegalArgumentException("Not a BigDecimal or Double"+o.getClass());
		}
		
		if(o instanceof BigDecimal) {
			BigDecimal park = (BigDecimal) o;
			
			return park.doubleValue();
		}
		
		return (Double) o;
	}
	
	protected Date objectToDate(Object o) {
		if(o==null) {
			return null;
		}
		if(!(o instanceof Date)) {
			throw new IllegalArgumentException("Not a Date "+o.getClass());
		}
		
		return (Date)o;
	}

    
}
