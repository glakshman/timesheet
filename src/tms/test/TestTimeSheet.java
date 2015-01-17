package tms.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import tms.beans.EmpBean;
import tms.beans.HoursOnADay;
import tms.beans.TaskBean;
import tms.beans.TimeSheetBean;
import tms.beans.TimeSheetEntryBean;
import tms.beans.WeekBean;
import tms.dao.EmpDao;
import tms.dao.TaskDao;
import tms.dao.TimeSheetsDao;
import tms.dao.WeekDao;
import tms.util.DateUtils;


public class TestTimeSheet {
	public static void main(String[] a){
		
		//getting Emp Id for given employee email_id
		
		EmpBean empBean = EmpDao.getEmpBean("lokesh@abc.com");
		System.out.println(empBean);
		
		//getting last 5 weeks that this employee can choose to submit timesheet
		List<WeekBean> weeks = WeekDao.getWeekBeans(5);
		for(WeekBean week : weeks){
			System.out.println(week);
		}
		
		// creating timesheet for current week
		WeekBean currentWeek = weeks.get(0);
		/*
		Use case :
		The emp trying to create a time sheet for the week 12-JAN to 18-Jan for the below tasks in the format

		tasks assigned for that emp
		taskName      startDate   endDate
		Coding			10-JAN    20-JAN
		Bug 123			12-JAN    15-JAN
		Bug 200     	15-JAN    20-JAN
		
		Creating timesheet for the week 12-JAN to 18-JA
  		Task			Mon	Tue	Wed	Thu	Fri	Sat	Sun
		Coding			4	4	4	4	4		
		Bug 123			4	4	4			
		Bug 200      				4	4			
		
*/		
		// Getting Tasks Eligible for the current week for the emp
		List<TaskBean> tasks = TaskDao.getTasksForTheWeek(empBean.getEmpId(),currentWeek);
		if(tasks.isEmpty()){
			System.out.println("no tasks for this emp ");
			System.out.println(empBean);
			System.out.println(weeks.get(0));
			return;
		}
		else{
			System.out.println("emp can choose the following tasks to fill the timesheet");
			for(TaskBean task : tasks){
				System.out.println(task);
			}
		}
		
		System.out.println("Creating timesheet for the week "+currentWeek);
		
		TimeSheetBean timeSheet = new TimeSheetBean();
		timeSheet.setEmpId(empBean.getEmpId());
		timeSheet.setWeek(currentWeek);
		List<TimeSheetEntryBean> entries = new ArrayList<TimeSheetEntryBean>();
		timeSheet.setEntries(entries);
		
		Date startDate = timeSheet.getWeek().getStartDate();
		Date endDate = timeSheet.getWeek().getEndDate();
		
		for(TaskBean task : tasks) {
		    try {
				List<Date> eligibleDays = TimeSheetsDao.getEligibleDays(currentWeek, task);
				if (eligibleDays != null && eligibleDays.size() >0)
				{
					System.out.println("Setting  hours for the following days for the task "+task.getTaskName());
					System.out.println(eligibleDays);
					
					int hours[] = new int[7];
					int hoursIndex;
					for(int i=0;i<eligibleDays.size();i++){
						
						BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
						System.out.print("Enter Hours for the day : "+eligibleDays.get(i) + " : ");  
						String s = br.readLine();
						if(s != null && !"".equals(s)){
							hoursIndex = DateUtils.getDaysBetween(startDate, eligibleDays.get(i)); 
							hours[hoursIndex] = Integer.parseInt(s);
						}
					}
				
					TimeSheetEntryBean entry = new TimeSheetEntryBean();
					entry.setTaskId(task.getTaskId());
					entry.setHours(hours);
					entries.add(entry);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// creating timesheet for the week 12-JAN to 18-JAN
		
		if(TimeSheetsDao.createTimeSheet(timeSheet))
			System.out.println("created successfully");
		
		// retrieving the time Sheet created for current week
		timeSheet = TimeSheetsDao.getTimeSheet(currentWeek.getWeekId(), empBean.getEmpId());
		WeekBean week = timeSheet.getWeek();
		System.out.println("Time sheet for current week " + week.getStartDate() +" - "+week.getEndDate());
		List<TimeSheetEntryBean> timeSheetEntries = timeSheet.getEntries();
		
		System.out.println(DateUtils.getDatesInBetweend(week.getStartDate(), week.getEndDate()));
		int[] totalHours = new int[7];
		for(TimeSheetEntryBean entry : timeSheetEntries){
			System.out.print(" "+entry.getTaskName());
			for(int i=0;i<7;i++){
				totalHours[i] = totalHours[i] + entry.getHours()[i];
				System.out.print("  "+entry.getHours()[i]);
			}
			System.out.println();
		}
		System.out.print("Total time :");
		for(int i=0;i<7;i++)
			System.out.print(" " +totalHours[i]);
		
		// updating timesheet by changing hours for a task 
		TimeSheetEntryBean entry = timeSheet.getEntries().get(0);
		TaskBean task = TaskDao.getTask(entry.getTaskId());
		int aafd = 10;
		List<Date> eligibleDays = TimeSheetsDao.getEligibleDays(currentWeek, task);
		if (eligibleDays != null && eligibleDays.size() >0)
		{
			System.out.println("Setting  hours for the following days for the task "+task.getTaskName());
			System.out.println(eligibleDays);
			
			int hours[] = entry.getHours();
			int hoursIndex;
			for(int i=0;i<eligibleDays.size();i++){
				
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Enter Hours for the day : "+eligibleDays.get(i) + " : ");  
				String s;
				try {
					s = br.readLine();
					if(s != null && !"".equals(s)){
						hoursIndex = DateUtils.getDaysBetween(startDate, eligibleDays.get(i)); 
						hours[hoursIndex] = Integer.parseInt(s);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//updating the timesheet
		if(TimeSheetsDao.updateTimeSheet(timeSheet))
			System.out.println("\n updated successfully");
		
		// deleting a particular task entry in the timesheet
		if(TimeSheetsDao.deleteTimeSheetEntry(timeSheet.getId(), entry.getTaskId()));
			System.out.println("\n deleted successfully");
				
		//submitting time sheet created for current week
		if(TimeSheetsDao.submitTimeSheet(timeSheet.getId()))
			System.out.println("\n Submitted successfully");
		
	}
}


