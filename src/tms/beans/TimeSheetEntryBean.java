package tms.beans;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class TimeSheetEntryBean {
	String taskName;
	int taskId;
	int weekId;
	public int getWeekId() {
		return weekId;
	}

	public void setWeekId(int weekId) {
		this.weekId = weekId;
	}

	// for storing the hours submitted on each day
	int[] hours;
	
	public String getTaskName() {
		return taskName;
	}
	
	public void setHours(int[] hours) {
		this.hours = hours;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	public int[] getHours() {
		return hours;
	}
}
