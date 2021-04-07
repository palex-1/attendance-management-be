package it.palex.attendanceManagement.data.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.data.entities.converters.OneLineStringSanitizerConverter;
import it.palex.attendanceManagement.data.entities.converters.ToUpperCaseConverter;
import it.palex.attendanceManagement.data.entities.core.SupportedLangI18n;
import it.palex.attendanceManagement.data.entities.enumTypes.SexEnum;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author palex
 */
@Entity
@Table(name = "user_profile")
public class UserProfile extends AuditableEntity implements DatabaseCheckableEntity {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    
    public static final int MAX_NAME_SIZE = 50;

    @NotNull
    @Size(max = MAX_NAME_SIZE)
    @Column(name = "name")
    private String name;

    public static final int MAX_SURNAME_SIZE = 50;

    @NotNull
    @Size(max = MAX_SURNAME_SIZE)
    @Column(name = "surname")
    private String surname;
    
    public static final int MAX_SEX_SIZE = 1;
    
    @NotNull
    @Basic(optional = false)
    @Size(max = MAX_SEX_SIZE)
    @Column(name = "sex")
    private String sex;
    
    public static final boolean TERMS_AND_CONDITION_ACCEPTED_DEFAULT = true;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "terms_and_condition_accepted")
    private Boolean termsAndConditionAccepted = TERMS_AND_CONDITION_ACCEPTED_DEFAULT;
    
    public static final int MAX_FISCAL_CODE_SIZE = 30;
    
    @Size(max = MAX_FISCAL_CODE_SIZE)
    @Column(name = "fiscal_code")
    private String fiscalCode;
    
    @NotNull
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;
        
    @Basic(optional = false)
    @Column(name = "date_of_employment")
    @Temporal(TemporalType.DATE)
    private Date dateOfEmployment;
        
    public static final int CONTACT_MAX_VAL = UserContacts.MAX_CVALUE_SIZE;
    
    @Size(max = CONTACT_MAX_VAL)
    @Column(name = "email")
    private String email;
    
    @Size(max = CONTACT_MAX_VAL)
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @JoinColumn(name = "supported_lang_i18n", referencedColumnName = "id")
    @ManyToOne
    private SupportedLangI18n preferredLang;
    
    @JoinColumn(name = "company", referencedColumnName = "id")
    @ManyToOne
    private Company company;
    
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private UsersAuthDetails usersAuthDetails;
    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userProfile")
    private UserProfileContractInfo userProfileContractInfo;
    
    @PrePersist
    @PreUpdate
    public void sanitizeBeforeUpdate() {
    	this.fiscalCode = ToUpperCaseConverter.convertToDatabaseColumn(this.fiscalCode);
    	this.name = OneLineStringSanitizerConverter.convertToDatabaseColumn(this.name);
    	this.surname = OneLineStringSanitizerConverter.convertToDatabaseColumn(this.surname);
    }
    
    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidName(this.name) && isValidSurname(this.surname)
				&& isValidSex(this.sex) && isValidTermsAndConditionsAccepted(this.termsAndConditionAccepted)
				&& isValidFiscalCode(this.fiscalCode) && isValidBirthDate(this.birthDate)
				&& isValidDateOfEmployment(this.dateOfEmployment) 
				&& isValidUserProfileContractInfo(this.userProfileContractInfo)
				&& isValidEmail(this.email) && isValidPhoneNumber(this.phoneNumber)
				&& isValidPreferredLang(this.preferredLang) && isValidCompany(this.company)
				&& isValidUsersAuthDetails(this.usersAuthDetails);
		
		return isValid;
	}


	public String whyCannotBeInsertedInDatabase() {
    	String why = "name:"+this.name+"surname:"+this.surname
					+", sex:"+this.sex+", termsAndConditionAccepted:"+this.termsAndConditionAccepted
					+", fiscalCode:"+this.fiscalCode+", birthDate:"+this.birthDate
					+", dateOfEmployment:"+this.dateOfEmployment
					+", userProfileContractInfo:"+this.userProfileContractInfo
					+", email:"+this.email+", phoneNumber:"+this.phoneNumber
					+", preferredLang:"+this.preferredLang+", company:"+this.company
					+", usersAuthDetails:"+this.usersAuthDetails;
    	
    	return why;
    }
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static boolean isValidName(String name) {
   	 if (name == null) {
            return false;
        }
        if (name.length() > MAX_NAME_SIZE) {
            return false;
        }
        return true;
	}
	
	
	
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public static boolean isValidSurname(String surname) {
    	if (surname == null) {
            return false;
        }
        if (surname.length() > MAX_SURNAME_SIZE) {
            return false;
        }
        return true;
	}
	
	
	

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public static boolean isValidSex(String sex) {
    	if (sex == null) {
            return false;
        }
        if (sex.length() > MAX_SEX_SIZE) {
            return false;
        }
        return SexEnum.isValid(sex);
	}
	
	
	public Boolean getTermsAndConditionAccepted() {
		return termsAndConditionAccepted;
	}

	public void setTermsAndConditionAccepted(Boolean termsAndConditionAccepted) {
		this.termsAndConditionAccepted = termsAndConditionAccepted;
	}
	
	public static boolean isValidTermsAndConditionsAccepted(Boolean termsAndConditionAccepted) {
		return termsAndConditionAccepted!=null;
	}
	
	

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	public static boolean isValidFiscalCode(String fiscalCode) {
		if(fiscalCode==null) {
			return true;
		}
		if(fiscalCode.length()>MAX_FISCAL_CODE_SIZE) {
			return false;
		}
		return true;
	}
	 
	 
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public static boolean isValidBirthDate(Date birthDate) {
		return birthDate!=null;
	}
	

	public Date getDateOfEmployment() {
		return dateOfEmployment;
	}

	public void setDateOfEmployment(Date dateOfEmployment) {
		this.dateOfEmployment = dateOfEmployment;
	}
	
	public static boolean isValidDateOfEmployment(Date dateOfEmployment) {
		return true;
	}
	
	public UserProfileContractInfo getUserProfileContractInfo() {
		return userProfileContractInfo;
	}

	public void setUserProfileContractInfo(UserProfileContractInfo userProfileContractInfo) {
		this.userProfileContractInfo = userProfileContractInfo;
	}

	public static boolean isValidUserProfileContractInfo(UserProfileContractInfo contractInfo) {
    	return true;
	}
	
	public UsersAuthDetails getUsersAuthDetails() {
		return usersAuthDetails;
	}

	public void setUsersAuthDetails(UsersAuthDetails usersAuthDetails) {
		this.usersAuthDetails = usersAuthDetails;
	}

	public static boolean isValidUsersAuthDetails(UsersAuthDetails usersAuthDetails) {
		return usersAuthDetails!=null;
	}
	
	
	public SupportedLangI18n getPreferredLang() {
		return preferredLang;
	}

	public void setPreferredLang(SupportedLangI18n preferredLang) {
		this.preferredLang = preferredLang;
	}

	public static boolean isValidPreferredLang(SupportedLangI18n preferredLang) {
		return true;
	}
	
	public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    
    public static boolean isValidCompany(Company company) {
		return true;
	}
    
    
    
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static boolean isValidEmail(String email) {
		if(email==null) {
			return true;
		}
		if(email.length()>CONTACT_MAX_VAL) {
			return false;
		}
		return true;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		if(phoneNumber==null) {
			return true;
		}
		if(phoneNumber.length()>CONTACT_MAX_VAL) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProfile other = (UserProfile) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserProfile [id=" + id + "]";
	}

	
    
}
