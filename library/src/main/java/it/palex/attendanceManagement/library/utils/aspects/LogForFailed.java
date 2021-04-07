package it.palex.attendanceManagement.library.utils.aspects;

public class LogForFailed extends CustomLog {

	private long executionTimeMilliseconds;
	
	
	public LogForFailed() {
		this.phase = CustomLog.PHASE_END_FAILED;
	}
	
	public LogForFailed(String aspectOperationId, long executionTimeMilliseconds, String target) {
		this.aspectUUID = aspectOperationId;
		this.executionTimeMilliseconds = executionTimeMilliseconds;
		this.target = target;
	}

	public long getExecutionTimeMilliseconds() {
		return executionTimeMilliseconds;
	}
	public void setExecutionTimeMilliseconds(long executionTimeMilliseconds) {
		this.executionTimeMilliseconds = executionTimeMilliseconds;
	}

	@Override
	public String toString() {
		return "LogForFailed [executionTimeMilliseconds=" + executionTimeMilliseconds + ", aspectUUID=" + aspectUUID
				+ ", target=" + target + ", phase=" + phase + "]";
	}
	
}
