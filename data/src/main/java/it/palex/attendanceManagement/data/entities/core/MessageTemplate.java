package it.palex.attendanceManagement.data.entities.core;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "message_template")
public class MessageTemplate implements Serializable, DatabaseCheckableEntity {
	
	private static final long serialVersionUID = 885200871894677393L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    public static final int MIN_SUBJECT_SIZE = 1;
    public static final int MAX_SUBJECT_SIZE = 997;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_SUBJECT_SIZE, max = MAX_SUBJECT_SIZE)
    @Column(name = "subject")
    private String subject;
    
    public static final int MIN_MESSAGE_SIZE = 1;
    public static final int MAX_MESSAGE_SIZE = 2147483647;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_MESSAGE_SIZE, max = MAX_MESSAGE_SIZE)
    @Column(name = "message")
    private String message;
    
    @JoinColumn(name = "lang", referencedColumnName = "lang")
    @ManyToOne(optional = false)
    private SupportedLangI18n lang;
    
    @JoinColumn(name = "m_type", referencedColumnName = "m_type")
    @ManyToOne(optional = false)
    private MessageType mType;

    public MessageTemplate() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidMType(this.mType) && isValidMessage(this.message)
				&& isValidLang(this.lang) && isValidSubject(this.subject);
	
		return isValid;
	}

    @Override
	public String whyCannotBeInsertedInDatabase() {
		final String why = "mType:"+this.mType+", message:"+ this.message+", lang:"+ this.lang+", subject:"+ this.subject;
		
		return why;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public static boolean isValidMessage(String message) {
    	if(message==null) {
    		return false;
    	}
    	if( message.length() < MIN_MESSAGE_SIZE ||  message.length() > MAX_MESSAGE_SIZE){
    		return false;
    	}
    	
    	return true;
    }
    
    public SupportedLangI18n getLang() {
		return lang;
	}

	public void setLang(SupportedLangI18n lang) {
		this.lang = lang;
	}

	public MessageType getmType() {
		return mType;
	}

	public void setmType(MessageType mType) {
		this.mType = mType;
	}

	public static boolean isValidLang(SupportedLangI18n lang) {
    	return lang!=null;
    }

    public MessageType getMType() {
        return mType;
    }
    
    public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	private static boolean isValidSubject(String subject) {
    	if(subject==null) {
    		return false;
    	}
    	if( subject.length() < MIN_SUBJECT_SIZE ||  subject.length() > MAX_SUBJECT_SIZE){
    		return false;
    	}
    	
    	return true;
	}
	
    public void setMType(MessageType mType) {
        this.mType = mType;
    }
    
    public static boolean isValidMType(MessageType mType) {
    	return mType!=null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MessageTemplate)) {
            return false;
        }
        MessageTemplate other = (MessageTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "MessageTemplate [id=" + id + "]";
	}

    

}

