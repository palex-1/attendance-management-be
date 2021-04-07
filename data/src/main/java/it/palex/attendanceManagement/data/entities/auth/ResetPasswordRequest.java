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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;

/**
 *
 * @author palex
 */
@Entity
@Table(name = "reset_password_request")
@NamedQueries({
    @NamedQuery(name = "ResetPasswordRequest.findByIp", query = "SELECT r FROM ResetPasswordRequest r WHERE r.resetPasswordRequestPK.ip = :ip"),
    @NamedQuery(name = "ResetPasswordRequest.findByUsername", query = "SELECT r FROM ResetPasswordRequest r WHERE r.resetPasswordRequestPK.username = :username"),
    @NamedQuery(name = "ResetPasswordRequest.findByRequestToken", query = "SELECT r FROM ResetPasswordRequest r WHERE r.requestToken = :requestToken"),
    @NamedQuery(name = "ResetPasswordRequest.findByResetEmailSentSuccessfully", query = "SELECT r FROM ResetPasswordRequest r WHERE r.resetEmailSentSuccessfully = :resetEmailSentSuccessfully")
    
    
    , @NamedQuery(name = "ResetPasswordRequest.findByUserUsernameAndDate", query = "SELECT r FROM ResetPasswordRequest r WHERE "
    		+ "r.resetPasswordRequestPK.username = :username AND r.resetPasswordRequestPK.creationDate = :creationDate")

    
    , @NamedQuery(name = "ResetPasswordRequest.findByKey", 
    					query = "SELECT r FROM ResetPasswordRequest r WHERE r.resetPasswordRequestPK.userAgent = :userAgent AND "
    							+ "r.resetPasswordRequestPK.creationDate = :creationDate AND r.resetPasswordRequestPK.ip = :ip AND "
    							+ "r.resetPasswordRequestPK.username = :username")
    
    , @NamedQuery(name = "ResetPasswordRequest.deleteAllPasswordResetRequestBeforeDateOfusername", 
						query = "DELETE FROM ResetPasswordRequest r WHERE r.resetPasswordRequestPK.creationDate < :beforeDate AND "
								+ "r.resetPasswordRequestPK.username = :username") //strettamente minore ALTRIMENTI SI annulla anche l'ultimo inserito
    
    , @NamedQuery(name = "ResetPasswordRequest.countResetPasswordRequestOfIpInTimeRange", 
				query = "SELECT COUNT(r) FROM ResetPasswordRequest r WHERE r.resetPasswordRequestPK.ip = :ip AND "
						+ "r.resetPasswordRequestPK.creationDate >= :from AND r.resetPasswordRequestPK.creationDate <= :to")
    
    , @NamedQuery(name = "ResetPasswordRequest.countResetPasswordRequestOfUsernameInTimeRange", 
				query = "SELECT COUNT(r) FROM ResetPasswordRequest r WHERE r.resetPasswordRequestPK.username = :username AND "
						+ "r.resetPasswordRequestPK.creationDate >= :from AND r.resetPasswordRequestPK.creationDate <= :to")

    , @NamedQuery(name = "ResetPasswordRequest.deleteAllResetPasswordRequestBefore", 
					query = "DELETE FROM ResetPasswordRequest r WHERE r.resetPasswordRequestPK.creationDate <= :beforeDate")   

    , @NamedQuery(name = "ResetPasswordRequest.deleteAllResetPasswordExpiredBefore", 
	query = "DELETE FROM ResetPasswordRequest r WHERE r.expirationDate <= :beforeDate")   

})
public class ResetPasswordRequest implements Serializable, DatabaseCheckableEntity {
	
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected ResetPasswordRequestPK resetPasswordRequestPK;
    
    public static final int REQUEST_TOKEN_MIN_SIZE = 32;
    public static final int REQUEST_TOKEN_MAX_SIZE = 128;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = REQUEST_TOKEN_MIN_SIZE, max = REQUEST_TOKEN_MAX_SIZE)
    @Column(name = "request_token")
    private String requestToken;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "reset_email_sent_successfully")
    private Boolean resetEmailSentSuccessfully;

    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
    
    
    public ResetPasswordRequest() {
    }

    @Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = this.isValidRequestToken(this.requestToken) && this.resetEmailSentSuccessfully!=null 
				&& this.resetPasswordRequestPK!=null;
		if(isValid){
			return this.resetPasswordRequestPK.canBeInsertedInDatabase();
		}
		return false;
	}
    
	public ResetPasswordRequestPK getResetPasswordRequestPK() {
        return resetPasswordRequestPK;
    }

    public void setResetPasswordRequestPK(ResetPasswordRequestPK resetPasswordRequestPK) {
        this.resetPasswordRequestPK = resetPasswordRequestPK;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
    
    private boolean isValidRequestToken(String requestToken) {
		if(requestToken==null){
			return false;
		}
		if(requestToken.length() < REQUEST_TOKEN_MIN_SIZE || requestToken.length() > REQUEST_TOKEN_MAX_SIZE){
			return false;
		}
		return true;
	}

    public Boolean getResetEmailSentSuccessfully() {
        return resetEmailSentSuccessfully;
    }

    public void setResetEmailSentSuccessfully(Boolean resetEmailSentSuccessfully) {
        this.resetEmailSentSuccessfully = resetEmailSentSuccessfully;
    }

    
    public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (resetPasswordRequestPK != null ? resetPasswordRequestPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResetPasswordRequest)) {
            return false;
        }
        ResetPasswordRequest other = (ResetPasswordRequest) object;
        if ((this.resetPasswordRequestPK == null && other.resetPasswordRequestPK != null) || (this.resetPasswordRequestPK != null && !this.resetPasswordRequestPK.equals(other.resetPasswordRequestPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.palex.gestionePresenze.data.entities.ResetPasswordRequest[ resetPasswordRequestPK=" + resetPasswordRequestPK + " ]";
    }
    
}
