package tms.beans;
import java.sql.Date;


public class TaskBean {
	private int taskId;
	private String taskName;
	private String descritpion;
	private Date startDate;
	private Date endDate;
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getDescritpion() {
		return descritpion;
	}
	public void setDescritpion(String descritpion) {
		this.descritpion = descritpion;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	private int createdBy;
	@Override
	public String toString() {
		return "TaskBean [taskId=" + taskId + ", taskName=" + taskName
				+ ", descritpion=" + descritpion + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", createdBy=" + createdBy + "]";
	}
}
