package it.palex.attendanceManagement.data.dto.tasks;

import java.util.Date;

import it.palex.attendanceManagement.data.dto.impiegato.ImpiegatoTinyDTO;
import it.palex.attendanceManagement.library.rest.dtos.DTO;

/**
 * @author Alessandro Pagliaro
 *
 */
public class IncaricoDetailsOutDTO implements DTO {

	private static final long serialVersionUID = -5428199905922618452L;

	private WorkTaskDTO task;

	private ImpiegatoTinyDTO projectManager;
	private ImpiegatoTinyDTO deliveryManager;
	private ImpiegatoTinyDTO accountManager;
	private ImpiegatoTinyDTO qaReviewer;

	public ImpiegatoTinyDTO getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(ImpiegatoTinyDTO projectManager) {
		this.projectManager = projectManager;
	}

	public ImpiegatoTinyDTO getDeliveryManager() {
		return deliveryManager;
	}

	public void setDeliveryManager(ImpiegatoTinyDTO deliveryManager) {
		this.deliveryManager = deliveryManager;
	}

	public ImpiegatoTinyDTO getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(ImpiegatoTinyDTO accountManager) {
		this.accountManager = accountManager;
	}

	public ImpiegatoTinyDTO getQaReviewer() {
		return qaReviewer;
	}

	public void setQaReviewer(ImpiegatoTinyDTO qaReviewer) {
		this.qaReviewer = qaReviewer;
	}

	public WorkTaskDTO getTask() {
		return task;
	}

	public void setTask(WorkTaskDTO task) {
		this.task = task;
	}

	@Override
	public String toString() {
		return "IncaricoDetailsOutDTO [task=" + task + ", projectManager=" + projectManager + ", deliveryManager="
				+ deliveryManager + ", accountManager=" + accountManager + ", qaReviewer=" + qaReviewer + "]";
	}

}
