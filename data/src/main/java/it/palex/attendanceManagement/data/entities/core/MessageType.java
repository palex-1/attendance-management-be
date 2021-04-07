package it.palex.attendanceManagement.data.entities.core;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import it.palex.attendanceManagement.data.entities.enumTypes.MessageTypeEnum;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "message_type")
public class MessageType implements Serializable, DatabaseCheckableEntity {
	
	private static final long serialVersionUID = -7624189898727767464L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    public static final int MIN_MTYPE_SIZE = 1;
    public static final int MAX_MTYPE_SIZE = 30;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_MTYPE_SIZE, max = MAX_MTYPE_SIZE)
    @Column(name = "m_type")
    private String mType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mType")
    private List<MessageTemplate> messageTemplateList;
    
    public MessageType() {
    }
    

	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidMType(this.mType);
		
		return isValid;
	}
	
    @Override
	public String whyCannotBeInsertedInDatabase() {
		final String why = "mType:"+this.mType;
		
		return why;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMType() {
        return mType;
    }

    public void setMType(String mType) {
        this.mType = mType;
    }
    
    public static boolean isValidMType(String mType) {
    	if(mType==null) {
    		return false;
    	}
    	if( mType.length() < MIN_MTYPE_SIZE ||  mType.length() > MAX_MTYPE_SIZE){
    		return false;
    	}
    	
    	return MessageTypeEnum.isValid(mType);
    }
    
    @XmlTransient
    public List<MessageTemplate> getMessageTemplateList() {
        return messageTemplateList;
    }

    public void setMessageTemplateList(List<MessageTemplate> messageTemplateList) {
        this.messageTemplateList = messageTemplateList;
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
        if (!(object instanceof MessageType)) {
            return false;
        }
        MessageType other = (MessageType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


	@Override
	public String toString() {
		return "MessageType [id=" + id + "]";
	}
}
