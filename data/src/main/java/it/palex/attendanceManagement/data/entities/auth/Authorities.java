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
@Table(name = "authorities")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Authorities.findAll", query = "SELECT a FROM Authorities a"),
    @NamedQuery(name = "Authorities.findById", query = "SELECT a FROM Authorities a WHERE a.id = :id")})
public class Authorities implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @JoinColumn(name = "authority", referencedColumnName = "authority")
    @ManyToOne(optional = false)
    private Permissions authority;
    
    @JoinColumn(name = "fk_id_users_auth_details", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UsersAuthDetails fkIdUsersAuthDetails;

    public Authorities() {
    }

    public Authorities(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Permissions getAuthority() {
        return authority;
    }

    public void setAuthority(Permissions authority) {
        this.authority = authority;
    }

    public UsersAuthDetails getFkIdUsersAuthDetails() {
        return fkIdUsersAuthDetails;
    }

    public void setFkIdUsersAuthDetails(UsersAuthDetails fkIdUsersAuthDetails) {
        this.fkIdUsersAuthDetails = fkIdUsersAuthDetails;
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
        if (!(object instanceof Authorities)) {
            return false;
        }
        Authorities other = (Authorities) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.Authorities[ id=" + id + " ]";
    }
    
}
