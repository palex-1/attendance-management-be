package it.palex.attendanceManagement.commons.messaging.notification;

import java.math.BigInteger;

public class FcmNotificationSendResult {
	
	private BigInteger multicast_id;
	private Integer success;
	private Integer failure;
	private Integer canonical_ids;
	private ResultsFCM[] results;
	
	
	public BigInteger getMulticast_id() {
		return multicast_id;
	}
	public void setMulticast_id(BigInteger multicast_id) {
		this.multicast_id = multicast_id;
	}
	
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	
	public Integer getFailure() {
		return failure;
	}
	public void setFailure(Integer failure) {
		this.failure = failure;
	}
	
	public Integer getCanonical_ids() {
		return canonical_ids;
	}
	public void setCanonical_ids(Integer canonical_ids) {
		this.canonical_ids = canonical_ids;
	}
	public ResultsFCM[] getResults() {
		return results;
	}
	public void setResults(ResultsFCM[] results) {
		this.results = results;
	}
	
	
}
