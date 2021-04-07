package it.palex.attendanceManagement.data.entities.auth;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Alessandro Pagliaro
 * 
 * 30 mag 2020
 */
@Entity
@Table(name = "permission_group_label")
public class PermissionGroupLabel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	
	public static final int PERMISSION_GROUP_MAX_SIZE = 50;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "name")
	private String name;
	

	public PermissionGroupLabel() {
	}

	public PermissionGroupLabel(Integer id) {
		this.id = id;
	}

	public PermissionGroupLabel(Integer id, String name) {
		this.id = id;
		this.name = name;
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


	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PermissionGroupLabel)) {
			return false;
		}
		PermissionGroupLabel other = (PermissionGroupLabel) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "it.palex.attendanceManagement.data.entities.PermissionGroupLabel[ id=" + id + " ]";
	}

}
