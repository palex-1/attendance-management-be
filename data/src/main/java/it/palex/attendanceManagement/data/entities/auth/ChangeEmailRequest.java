package it.palex.attendanceManagement.data.entities.auth;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.UserProfile;

/**
 * @author Alessandro Pagliaro
 *
 */
@Entity
@Table(name = "change_email_request")
@NamedQueries({
    @NamedQuery(name = "ChangeEmailRequest.findAll", query = "SELECT c FROM ChangeEmailRequest c")
    , @NamedQuery(name = "ChangeEmailRequest.findByIp", query = "SELECT c FROM ChangeEmailRequest c WHERE c.ip = :ip")
    , @NamedQuery(name = "ChangeEmailRequest.findByUserAgent", query = "SELECT c FROM ChangeEmailRequest c WHERE c.userAgent = :userAgent")
    , @NamedQuery(name = "ChangeEmailRequest.findByCreationDate", query = "SELECT c FROM ChangeEmailRequest c WHERE c.creationDate = :creationDate")
    , @NamedQuery(name = "ChangeEmailRequest.findByNewEmail", query = "SELECT c FROM ChangeEmailRequest c WHERE c.newEmail = :newEmail")
    , @NamedQuery(name = "ChangeEmailRequest.findByToken", query = "SELECT c FROM ChangeEmailRequest c WHERE c.token = :token")})
public class ChangeEmailRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 39)
    @Column(name = "ip")
    private String ip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "user_agent")
    private String userAgent;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "new_email")
    private String newEmail;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "token")
    private String token;
    @JoinColumn(name = "user_profile", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserProfile userProfile;

    public ChangeEmailRequest() {
    }

    public ChangeEmailRequest(String token) {
        this.token = token;
    }

    public ChangeEmailRequest(String token, String ip, String userAgent, Date creationDate, String newEmail) {
        this.token = token;
        this.ip = ip;
        this.userAgent = userAgent;
        this.creationDate = creationDate;
        this.newEmail = newEmail;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (token != null ? token.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChangeEmailRequest)) {
            return false;
        }
        ChangeEmailRequest other = (ChangeEmailRequest) object;
        if ((this.token == null && other.token != null) || (this.token != null && !this.token.equals(other.token))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.attendanceManagement.data.entities.ChangeEmailRequest[ token=" + token + " ]";
    }
    
}
