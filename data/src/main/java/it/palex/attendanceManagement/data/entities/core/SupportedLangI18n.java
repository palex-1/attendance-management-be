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

import it.palex.attendanceManagement.data.entities.UserProfile;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "supported_lang_i18n")
public class SupportedLangI18n implements Serializable, DatabaseCheckableEntity {
	
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    public static final int MIN_LANG_SIZE = 1;
    public static final int MAX_LANG_SIZE = 10;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = MIN_LANG_SIZE, max = MAX_LANG_SIZE)
    @Column(name = "lang", unique = true)
    private String lang;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lang")
    private List<MessageTemplate> messageTemplateList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preferredLang")
    private List<UserProfile> userProfileList;

    
    public SupportedLangI18n() {
    }


	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidLang(this.lang);
		
		return isValid;
	}

    @Override
	public String whyCannotBeInsertedInDatabase() {
		final String why = "lang:"+this.lang;
		
		return why;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public static boolean isValidLang(String lang) {
    	if(lang==null) {
    		return false;
    	}
    	if(lang.length()  < MIN_LANG_SIZE || lang.length() > MAX_LANG_SIZE) {
    		return false;
    	}
    	return true;
    }
    
    @XmlTransient
    public List<MessageTemplate> getMessageTemplateList() {
        return messageTemplateList;
    }

    public void setMessageTemplateList(List<MessageTemplate> messageTemplateList) {
        this.messageTemplateList = messageTemplateList;
    }

    @XmlTransient
    public List<UserProfile> getApplicationUserList() {
        return userProfileList;
    }

    public void setApplicationUserList(List<UserProfile> applicationUserList) {
        this.userProfileList = applicationUserList;
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
        if (!(object instanceof SupportedLangI18n)) {
            return false;
        }
        SupportedLangI18n other = (SupportedLangI18n) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "SupportedLangI18n [id=" + id + ", lang=" + lang + ", messageTemplateList=" + messageTemplateList + "]";
	}

   
}

