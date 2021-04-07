package it.palex.attendanceManagement.data.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.enumTypes.CompanyBranchType;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author palex
 */
@Entity
@Table(name = "office")
public class Office extends AuditableEntity implements DatabaseCheckableEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;

	public static final int MIN_OFFICE_NAME_SIZE = 1;
	public static final int MAX_OFFICE_NAME_SIZE = 100;

	@Basic(optional = false)
	@NotNull
	@Size(min = MIN_OFFICE_NAME_SIZE, max = MAX_OFFICE_NAME_SIZE)
	@Column(name = "office_name")
	private String officeName;

	public static final int MIN_STREET_SIZE = 1;
	public static final int MAX_STREET_SIZE = 200;

	@Basic(optional = false)
	@NotNull
	@Size(min = MIN_STREET_SIZE, max = MAX_STREET_SIZE)
	@Column(name = "street")
	private String street;

	public static final int MIN_CITY_SIZE = 1;
	public static final int MAX_CITY_SIZE = 200;

	@Basic(optional = false)
	@NotNull
	@Size(min = MIN_CITY_SIZE, max = MAX_CITY_SIZE)
	@Column(name = "city")
	private String city;

	public static final int MIN_PROVINCE_SIZE = 1;
	public static final int MAX_PROVINCE_SIZE = 200;

	@Basic(optional = false)
	@NotNull
	@Size(min = MIN_PROVINCE_SIZE, max = MAX_PROVINCE_SIZE)
	@Column(name = "province")
	private String province;

	public static final int MIN_NATION_SIZE = 1;
	public static final int MAX_NATION_SIZE = 100;

	@Basic(optional = false)
	@NotNull
	@Size(min = MIN_NATION_SIZE, max = MAX_NATION_SIZE)
	@Column(name = "nation")
	private String nation;

	public static final int MIN_ZIP_CODE_SIZE = 1;
	public static final int MAX_ZIP_CODE_SIZE = 5;

	@Basic(optional = false)
	@NotNull
	@Size(min = MIN_ZIP_CODE_SIZE, max = MAX_ZIP_CODE_SIZE)
	@Column(name = "zip_code")
	private String zipCode;

	@Basic(optional = false)
	@NotNull
	@Column(name = "company_branch_type")
	private String companyBranchType;

	
	public Office() {
	}

	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidOfficeName(this.officeName) && isValidStreet(this.street)
				&& isValidZipCode(this.zipCode) && isValidStreet(this.street) && isValidNation(this.nation)
				&& isValidProvince(this.province) && isValidCity(this.city);

		return isValid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public static boolean isValidOfficeName(String officeName) {
		if (officeName == null) {
			return false;
		}
		if (officeName.length() < MIN_OFFICE_NAME_SIZE || officeName.length() > MAX_OFFICE_NAME_SIZE) {
			return false;
		}
		return true;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public static boolean isValidStreet(String street) {
		if (street == null) {
			return false;
		}
		if (street.length() < MIN_STREET_SIZE || street.length() > MAX_STREET_SIZE) {
			return false;
		}
		return true;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public static boolean isValidCity(String city) {
		if (city == null) {
			return false;
		}
		if (city.length() < MIN_CITY_SIZE || city.length() > MAX_CITY_SIZE) {
			return false;
		}
		return true;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public static boolean isValidProvince(String province) {
		if (province == null) {
			return false;
		}
		if (province.length() < MIN_PROVINCE_SIZE || province.length() > MAX_PROVINCE_SIZE) {
			return false;
		}
		return true;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public static boolean isValidNation(String nation) {
		if (nation == null) {
			return false;
		}
		if (nation.length() < MIN_NATION_SIZE || nation.length() > MAX_NATION_SIZE) {
			return false;
		}
		return true;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public static boolean isValidZipCode(String zipCode) {
		if (zipCode == null) {
			return false;
		}
		if (zipCode.length() < MIN_ZIP_CODE_SIZE || zipCode.length() > MAX_ZIP_CODE_SIZE) {
			return false;
		}
		return true;
	}

	public String getCompanyBranchType() {
		return companyBranchType;
	}

	public void setCompanyBranchType(String companyBranchType) {
		this.companyBranchType = companyBranchType;
	}

	public static boolean isValidCompanyBranchType(String companyBranchType) {
		if (companyBranchType == null) {
			return false;
		}
		return true;
	}

	public static boolean isValidTipoSede(String tipoSede) {
		if (tipoSede == null) {
			return false;
		}
		return CompanyBranchType.isValid(tipoSede);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Office)) {
			return false;
		}
		Office other = (Office) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.palex.attendanceManagement.data.entities.Office[ id=" + id + " ]";
	}
}
