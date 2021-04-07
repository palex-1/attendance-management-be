package it.palex.attendanceManagement.library.utils.aspects;

public class LogForEnd extends CustomLog {

	private long executionTimeMilliseconds;
	private Object outputParams;
	private String operationId;
	
	
	public LogForEnd() {
		this.phase = CustomLog.PHASE_END;
	}
	
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	
	public long getExecutionTimeMilliseconds() {
		return executionTimeMilliseconds;
	}
	public void setExecutionTimeMilliseconds(long executionTimeMilliseconds) {
		this.executionTimeMilliseconds = executionTimeMilliseconds;
	}
	
	public Object getOutputParams() {
		return outputParams;
	}
	public void setOutputParams(Object outputParams) {
		this.outputParams = outputParams;
	}

	@Override
	public String toString() {
		return "LogForEnd [executionTimeMilliseconds=" + executionTimeMilliseconds + ", outputParams=" + outputParams
				+ ", operationId=" + operationId + ", aspectUUID=" + aspectUUID + ", target=" + target + ", phase="
				+ phase + "]";
	}

}
