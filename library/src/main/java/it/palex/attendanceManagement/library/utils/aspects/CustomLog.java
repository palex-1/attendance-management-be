package it.palex.attendanceManagement.library.utils.aspects;

public abstract class CustomLog {

	public static final String PHASE_START = "START";
	public static final String PHASE_END = "END";
	public static final String PHASE_END_FAILED = "END_FAILED ";
	
	protected String aspectUUID;
	protected String target;
	protected String phase;


	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getPhase() {
		return phase;
	}
	
	public String getAspectUUID() {
		return aspectUUID;
	}
	
	public void setAspectUUID(String aspectUUID) {
		this.aspectUUID = aspectUUID;
	}
	
	
	
}
