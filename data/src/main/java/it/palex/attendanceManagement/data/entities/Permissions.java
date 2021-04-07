package it.palex.attendanceManagement.data.entities;

import java.io.Serializable; 
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import it.palex.attendanceManagement.data.entities.auth.Authorities;
import it.palex.attendanceManagement.data.entities.auth.PermissionGroup;

/**
 *
 * @author palex
 */
@Entity
@Table(name = "permissions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permissions.findAll", query = "SELECT p FROM Permissions p"),
    @NamedQuery(name = "Permissions.findById", query = "SELECT p FROM Permissions p WHERE p.id = :id"),
    @NamedQuery(name = "Permissions.findByAuthority", query = "SELECT p FROM Permissions p WHERE p.authority = :authority")})
public class Permissions implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "authority")
    private String authority;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkAuthority")
    private Collection<PermissionGroup> permissionGroupCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "authority")
    private Collection<Authorities> authoritiesCollection;

    public Permissions() {
    }

    public Permissions(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @XmlTransient
    public Collection<PermissionGroup> getPermissionGroupCollection() {
        return permissionGroupCollection;
    }

    public void setPermissionGroupCollection(Collection<PermissionGroup> permissionGroupCollection) {
        this.permissionGroupCollection = permissionGroupCollection;
    }

    @XmlTransient
    public Collection<Authorities> getAuthoritiesCollection() {
        return authoritiesCollection;
    }

    public void setAuthoritiesCollection(Collection<Authorities> authoritiesCollection) {
        this.authoritiesCollection = authoritiesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (authority != null ? authority.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permissions)) {
            return false;
        }
        Permissions other = (Permissions) object;
        if ((this.authority == null && other.authority != null) || (this.authority != null && !this.authority.equals(other.authority))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.Permissions[ authority=" + authority + " ]";
    }
    
}
