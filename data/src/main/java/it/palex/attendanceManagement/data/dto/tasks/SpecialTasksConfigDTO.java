package it.palex.attendanceManagement.data.dto.tasks;

public class SpecialTasksConfigDTO {

	private String taskCode;
	private String hexColor;
	private String name;

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getHexColor() {
		return hexColor;
	}

	public void setHexColor(String hexColor) {
		this.hexColor = hexColor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SpecialTasksConfigDTO [taskCode=" + taskCode + ", hexColor=" + hexColor + ", name=" + name + "]";
	}

}
