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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.palex.attendanceManagement.data.entities.converters.FileNameSanitizeConverter;
import it.palex.attendanceManagement.data.entities.generic.AuditableEntity;
import it.palex.attendanceManagement.data.entities.generic.DatabaseCheckableEntity;
import it.palex.attendanceManagement.library.fileManager.FileManagerType;

/**
 *
 * @author Alessandro Pagliaro
 */
@Entity
@Table(name = "document")
@NamedQueries({ @NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document d"),
		@NamedQuery(name = "Document.findById", query = "SELECT d FROM Document d WHERE d.id = :id") })
public class Document extends AuditableEntity implements Serializable, DatabaseCheckableEntity {

	private static final long serialVersionUID = -6825791686613981702L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	public static final int MIN_FULL_NAME_SIZE = 1;
	public static final int MAX_FULL_NAME_SIZE = 255;

	@Basic(optional = false)
	@NotNull
	@Size(min = MIN_FULL_NAME_SIZE, max = MAX_FULL_NAME_SIZE)
	@Column(name = "full_file_name")
	private String fullFileName;

	public static final int MIN_FILE_PATH_SIZE = 1;
	public static final int MAX_FILE_PATH_SIZE = 4096;

	@Basic(optional = false)
	@NotNull
	@Size(min = MIN_FILE_PATH_SIZE, max = MAX_FILE_PATH_SIZE)
	@Column(name = "file_path")
	private String filePath;

	public static final int MAX_DESCRIPTION_SIZE = 500;

	@Basic(optional = true)
	@Size(max = MAX_DESCRIPTION_SIZE)
	@Column(name = "description")
	private String description;

	public static final int MIN_FILE_MANAGER_SIZE = 1;
	public static final int MAX_FILE_MANAGER_SIZE = 50;

	@Basic(optional = false)
	@NotNull
	@Size(min = MIN_FILE_MANAGER_SIZE, max = MAX_FILE_MANAGER_SIZE)
	@Column(name = "file_manager")
	private String fileManager;

	@Basic(optional = false)
	@NotNull
	@Column(name = "file_size")
	private Long fileSize;

	public Document() {
	}
	
	@PrePersist
    @PreUpdate
    public void sanitizeBeforeUpdate() {
    	this.fullFileName = FileNameSanitizeConverter.convertToDatabaseColumn(this.fullFileName);
    }

	@Override
	public boolean canBeInsertedInDatabase() {
		boolean isValid = isValidFullFileName(fullFileName) && isValidFilePath(this.filePath)
				&& isValidDescription(this.description) && isValidFileSize(this.fileSize);

		return isValid;
	}
	
    @Override
	public String whyCannotBeInsertedInDatabase() {
		final String why = "fullFileName:"+this.fullFileName+", filePath:"+ this.filePath+", description:"+ this.description
				+", fileSize:"+ this.fileSize;
		
		return why;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullFileName() {
		return fullFileName;
	}

	public void setFullFileName(String fullFileName) {
		this.fullFileName = fullFileName;
	}

	public static boolean isValidFullFileName(String fullFileName) {
		if (fullFileName == null) {
			return false;
		}
		if (fullFileName.length() < MIN_FULL_NAME_SIZE || fullFileName.length() > MAX_FULL_NAME_SIZE) {
			return false;
		}
		return true;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public static boolean isValidFilePath(String filePath) {
		if (filePath == null) {
			return false;
		}
		if (filePath.length() < MIN_FILE_PATH_SIZE || filePath.length() > MAX_FILE_PATH_SIZE) {
			return false;
		}
		return true;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static boolean isValidDescription(String description) {
		if (description == null) {
			return true;
		}
		if (description.length() > MAX_DESCRIPTION_SIZE) {
			return false;
		}
		return true;
	}

	public String getFileManager() {
		return fileManager;
	}

	public void setFileManager(String fileManager) {
		this.fileManager = fileManager;
	}

	public static boolean isValidFileManager(String fileManager) {
		if (fileManager == null) {
			return false;
		}
		if (fileManager.length() < MAX_FILE_MANAGER_SIZE || fileManager.length() > MAX_FILE_MANAGER_SIZE) {
			return false;
		}
		return FileManagerType.isValid(fileManager);
	}

	public static boolean isValidUploadedByFkUserId(UserProfile uploadedByFkUserId) {
		return uploadedByFkUserId != null;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public static boolean isValidFileSize(Long fileSize) {
		if (fileSize == null) {
			return false;
		}
		return fileSize.longValue() >= 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Document [id=" + id + "]";
	}

}
