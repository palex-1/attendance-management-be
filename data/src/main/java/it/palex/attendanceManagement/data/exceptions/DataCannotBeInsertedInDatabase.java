package it.palex.attendanceManagement.data.exceptions;

import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

public class DataCannotBeInsertedInDatabase extends RuntimeException {

	 private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DataCannotBeInsertedInDatabase.class);

	 
	private static final long serialVersionUID = 10003747591774180L;


	public DataCannotBeInsertedInDatabase(DatabaseCheckableEntity entity) {
		super(getWhyMessage(entity));
	}
	
	private static String getWhyMessage(DatabaseCheckableEntity entity) {
		if(entity==null) {
			return "entity is null";
		}
		try {
			return entity.whyCannotBeInsertedInDatabase();
		}catch(Exception e) {
			LOGGER.error("", e);
			return "";
		}
		
	}
	
	public DataCannotBeInsertedInDatabase() {
        super();
    }

    public DataCannotBeInsertedInDatabase(String message) {
        super(message);
    }

    public DataCannotBeInsertedInDatabase(String message, Throwable cause) {
        super(message, cause);
    }

    public DataCannotBeInsertedInDatabase(Throwable cause) {
        super(cause);
    }

    protected DataCannotBeInsertedInDatabase(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
