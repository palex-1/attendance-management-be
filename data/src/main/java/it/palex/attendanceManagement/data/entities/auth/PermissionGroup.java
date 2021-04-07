package it.palex.attendanceManagement.data.entities.auth;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import it.palex.attendanceManagement.data.entities.Permissions;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "permission_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PermissionGroup.findAll", query = "SELECT p FROM PermissionGroup p"),
    @NamedQuery(name = "PermissionGroup.findById", query = "SELECT p FROM PermissionGroup p WHERE p.id = :id"),
    @NamedQuery(name = "PermissionGroup.findByName", query = "SELECT p FROM PermissionGroup p WHERE p.name = :name")})
public class PermissionGroup implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @JoinColumn(name = "name", referencedColumnName = "name")
    @ManyToOne(optional = false)
    private PermissionGroupLabel name;
    
    @JoinColumn(name = "fk_authority", referencedColumnName = "authority")
    @ManyToOne(optional = false)
    private Permissions fkAuthority;

    public PermissionGroup() {
    }

    public PermissionGroup(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PermissionGroupLabel getName() {
        return name;
    }

    public void setName(PermissionGroupLabel name) {
        this.name = name;
    }

    public Permissions getFkAuthority() {
        return fkAuthority;
    }

    public void setFkAuthority(Permissions fkAuthority) {
        this.fkAuthority = fkAuthority;
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
        if (!(object instanceof PermissionGroup)) {
            return false;
        }
        PermissionGroup other = (PermissionGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.PermissionGroup[ id=" + id + " ]";
    }
    
}
