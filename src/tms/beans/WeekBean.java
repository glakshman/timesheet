package tms.beans;
import java.sql.Date;


public class WeekBean {
	private int weekId;
	private Date startDate;
	private Date endDate;
	public int getWeekId() {
		return weekId;
	}
	public void setWeekId(int weekId) {
		this.weekId = weekId;
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
	@Override
	public String toString() {
		return "WeekBean [weekId=" + weekId + ", startDate=" + startDate
				+ ", endDate=" + endDate + "]";
	}
	
	
	
}
