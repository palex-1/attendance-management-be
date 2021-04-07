package it.palex.attendanceManagement.data.entities.generic;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * The type Abstract jpa entity.
 */
@MappedSuperclass
@EntityListeners({DeletableEntityAuditListener.class})
public abstract class DeletableEntity extends AuditableEntity {

	private static final long serialVersionUID = 6566966650992435768L;
	
	@Basic(optional = false)
	@NotNull
    @Column(name = "deleted")
    private Boolean deleted;


    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

}
