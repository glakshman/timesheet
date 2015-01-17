package tms.test;

import java.util.List;

import tms.beans.EmpBean;
import tms.beans.TimeSheetBean;
import tms.beans.TimeSheetEntryBean;
import tms.beans.WeekBean;
import tms.dao.EmpDao;
import tms.dao.TimeSheetsDao;
import tms.dao.WeekDao;
import tms.util.DateUtils;

public class TestTimesheet1 {

	public static void main(String[] args) {
		
		// getting timesheet entries and total number of hours for a task submitted by an employee
		int taskId = 30;
		EmpBean emp = EmpDao.getEmpBean("lokesh@abc.com");
		List<TimeSheetEntryBean> timesheetEntries = TimeSheetsDao.getTimeSheetsEntries(taskId, emp.getEmpId());
		WeekBean week = null;
		int totalHours = 0;
		for(TimeSheetEntryBean entry : timesheetEntries){
			week = WeekDao.getWeek(entry.getWeekId());
			System.out.println(" hours worked during the week  "+week.getStartDate() + " to "+week.getEndDate());
			System.out.println(DateUtils.getDatesInBetweend(week.getStartDate(), week.getEndDate()));
			int hours[] = entry.getHours();
			for(int hour : hours){
				System.out.print("   "+ hour);
				totalHours += hour ;
			}
		}
		//getting total number of hours worked for a task by an employee
		System.out.println("\ntotal Hours : "+totalHours);
	
		// getting timesheets submitted by subordinates
		int managerId = 1;
		int weekId = 7;
		List<TimeSheetBean> timesheets = TimeSheetsDao.getTimeSheetsToApprove(weekId, managerId);
		week = WeekDao.getWeek(weekId);
		System.out.println("Time sheets for current week " + week.getStartDate() +" - "+week.getEndDate());
		for (TimeSheetBean timesheet : timesheets){
			System.out.println(" Timesheet id "+ timesheet.getId());
			List<TimeSheetEntryBean> timeSheetEntries = timesheet.getEntries();
			System.out.println(DateUtils.getDatesInBetweend(week.getStartDate(), week.getEndDate()));
			for(TimeSheetEntryBean entry : timeSheetEntries){
				System.out.print(" "+entry.getTaskName());
				for(int i=0;i<7;i++){
					System.out.print("  "+entry.getHours()[i]);
				}
				System.out.println();
			}

		}
	}
}
