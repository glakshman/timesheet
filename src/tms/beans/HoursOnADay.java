package tms.beans;
import java.sql.Date;


public class HoursOnADay {
	private Date date;
	private int hours;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
}
