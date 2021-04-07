package it.palex.attendanceManagement.data.entities.auth;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "reset_username_request")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResetUsernameRequest.findAll", query = "SELECT r FROM ResetUsernameRequest r"),
    @NamedQuery(name = "ResetUsernameRequest.findByIp", query = "SELECT r FROM ResetUsernameRequest r WHERE r.resetUsernameRequestPK.ip = :ip"),
    @NamedQuery(name = "ResetUsernameRequest.findByUserAgent", query = "SELECT r FROM ResetUsernameRequest r WHERE r.resetUsernameRequestPK.userAgent = :userAgent"),
    @NamedQuery(name = "ResetUsernameRequest.findByCreationDate", query = "SELECT r FROM ResetUsernameRequest r WHERE r.resetUsernameRequestPK.creationDate = :creationDate"),
    @NamedQuery(name = "ResetUsernameRequest.findByHashedUsername", query = "SELECT r FROM ResetUsernameRequest r WHERE r.resetUsernameRequestPK.hashedUsername = :hashedUsername"),
    @NamedQuery(name = "ResetUsernameRequest.findByNewHashedUsername", query = "SELECT r FROM ResetUsernameRequest r WHERE r.newHashedUsername = :newHashedUsername"),
    @NamedQuery(name = "ResetUsernameRequest.findByRequestToken", query = "SELECT r FROM ResetUsernameRequest r WHERE r.requestToken = :requestToken"),
    @NamedQuery(name = "ResetUsernameRequest.findByResetEmailSentSuccessfully", query = "SELECT r FROM ResetUsernameRequest r WHERE r.resetEmailSentSuccessfully = :resetEmailSentSuccessfully")})
public class ResetUsernameRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected ResetUsernameRequestPK resetUsernameRequestPK;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "new_hashed_username")
    private String newHashedUsername;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "request_token")
    private String requestToken;
    
    @Column(name = "reset_email_sent_successfully")
    private Boolean resetEmailSentSuccessfully;

    public ResetUsernameRequest() {
    }

    public ResetUsernameRequest(ResetUsernameRequestPK resetUsernameRequestPK) {
        this.resetUsernameRequestPK = resetUsernameRequestPK;
    }

    public ResetUsernameRequest(ResetUsernameRequestPK resetUsernameRequestPK, String newHashedUsername, String requestToken) {
        this.resetUsernameRequestPK = resetUsernameRequestPK;
        this.newHashedUsername = newHashedUsername;
        this.requestToken = requestToken;
    }

    public ResetUsernameRequest(String ip, String userAgent, Date creationDate, String hashedUsername) {
        this.resetUsernameRequestPK = new ResetUsernameRequestPK(ip, userAgent, creationDate, hashedUsername);
    }

    public ResetUsernameRequestPK getResetUsernameRequestPK() {
        return resetUsernameRequestPK;
    }

    public void setResetUsernameRequestPK(ResetUsernameRequestPK resetUsernameRequestPK) {
        this.resetUsernameRequestPK = resetUsernameRequestPK;
    }

    public String getNewHashedUsername() {
        return newHashedUsername;
    }

    public void setNewHashedUsername(String newHashedUsername) {
        this.newHashedUsername = newHashedUsername;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public Boolean getResetEmailSentSuccessfully() {
        return resetEmailSentSuccessfully;
    }

    public void setResetEmailSentSuccessfully(Boolean resetEmailSentSuccessfully) {
        this.resetEmailSentSuccessfully = resetEmailSentSuccessfully;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resetUsernameRequestPK != null ? resetUsernameRequestPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResetUsernameRequest)) {
            return false;
        }
        ResetUsernameRequest other = (ResetUsernameRequest) object;
        if ((this.resetUsernameRequestPK == null && other.resetUsernameRequestPK != null) || (this.resetUsernameRequestPK != null && !this.resetUsernameRequestPK.equals(other.resetUsernameRequestPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.ResetUsernameRequest[ resetUsernameRequestPK=" + resetUsernameRequestPK + " ]";
    }
    
}
