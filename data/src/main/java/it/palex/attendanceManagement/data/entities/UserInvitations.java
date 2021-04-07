package it.palex.attendanceManagement.data.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author palex
 */
@Entity
@Table(name = "user_invitations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserInvitations.findAll", query = "SELECT u FROM UserInvitations u"),
    @NamedQuery(name = "UserInvitations.findById", query = "SELECT u FROM UserInvitations u WHERE u.id = :id"),
    @NamedQuery(name = "UserInvitations.findByNome", query = "SELECT u FROM UserInvitations u WHERE u.nome = :nome"),
    @NamedQuery(name = "UserInvitations.findByCognome", query = "SELECT u FROM UserInvitations u WHERE u.cognome = :cognome"),
    @NamedQuery(name = "UserInvitations.findByEmail", query = "SELECT u FROM UserInvitations u WHERE u.email = :email"),
    @NamedQuery(name = "UserInvitations.findByToken", query = "SELECT u FROM UserInvitations u WHERE u.token = :token")})
public class UserInvitations implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nome")
    private String nome;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "cognome")
    private String cognome;
    
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "token")
    private String token;

    public UserInvitations() {
    }

    public UserInvitations(Integer id) {
        this.id = id;
    }

    public UserInvitations(Integer id, String nome, String cognome, String email, String token) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
        if (!(object instanceof UserInvitations)) {
            return false;
        }
        UserInvitations other = (UserInvitations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.UserInvitations[ id=" + id + " ]";
    }
    
}
