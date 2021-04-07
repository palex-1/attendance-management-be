package it.palex.attendanceManagement.library.utils.aspects;

public class LogForStart extends CustomLog {

	
	private Object inputParams;
	
	public LogForStart() {
		super();
		this.phase = LogForStart.PHASE_START;
	}
	
	public Object getInputParams() {
		return inputParams;
	}
	
	public void setInputParams(Object inputParams) {
		this.inputParams = inputParams;
	}

	@Override
	public String toString() {
		return "LogForStart [inputParams=" + inputParams + ", aspectUUID=" + aspectUUID + ", target=" + target
				+ ", phase=" + phase + "]";
	}

}
