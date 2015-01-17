package tms.beans;

import java.sql.Date;
import java.util.List;

//weekly view of timesheet entries for an employee

public class TimeSheetBean {
	Integer empId;
	WeekBean week;
	public WeekBean getWeek() {
		return week;
	}
	public void setWeek(WeekBean week) {
		this.week = week;
	}
	int id;
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	String status;
	List<TimeSheetEntryBean> entries;
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<TimeSheetEntryBean> getEntries() {
		return entries;
	}
	public void setEntries(List<TimeSheetEntryBean> entries) {
		this.entries = entries;
	}
}
