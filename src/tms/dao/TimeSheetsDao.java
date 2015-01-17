package tms.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import tms.beans.HoursOnADay;
import tms.beans.TaskBean;
import tms.beans.TimeSheetBean;
import tms.beans.TimeSheetEntryBean;
import tms.beans.WeekBean;
import tms.util.ConnectionManager;
import tms.util.DateUtils;


public class TimeSheetsDao {

	private static final String APPROVED = "A";
	private static final String SUBMITTED = "S";
	private static final String REJECTED = "R";
	private static final String NEW = "N";
	
	//get timesheet entries for given week and employee
	public static TimeSheetBean getTimeSheet(int weekId, int empId){
		TimeSheetBean timeSheet = new TimeSheetBean();
		timeSheet.setEmpId(empId);
		WeekBean week = WeekDao.getWeek(weekId);
		timeSheet.setWeek(week);
		
		// get timesheet id
		Connection con = null;
		con = ConnectionManager.getConnection();
		PreparedStatement stmt;
		String query ;
		ResultSet rs;
		try {
			query = "select timesheet_id, status from timesheet where emp_id = ? and week_id = ?"; 
			stmt = con.prepareStatement(query);
			stmt.setInt(1, empId);
			stmt.setInt(2, weekId);
			rs = stmt.executeQuery();
			while (rs.next()){
				timeSheet.setStatus(rs.getString(2));
				timeSheet.setId(rs.getInt(1));
			}
			
			List<TimeSheetEntryBean> entries = getTimeSheetsEntries(timeSheet.getId());
			timeSheet.setEntries(entries);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeSheet;
	}
	
	// to retrieve the timesheets submitted by emps to the manager
	private static List<TimeSheetEntryBean> getTimeSheetsEntries(Integer timesheetId) {
		List<TimeSheetEntryBean> entries = new ArrayList<TimeSheetEntryBean>();	
		Connection con = null;
		con = ConnectionManager.getConnection();
		PreparedStatement stmt;
		try {
			String query = "select e.task_id, task_name, day1_hrs, day2_hrs, day3_hrs, day4_hrs, day5_hrs, day6_hrs, day7_hrs" +
					" from ts_tasks t, timesheet_entries e " +
					"where t.task_id = e.task_id and timesheet_id = ?";
			
			stmt = con.prepareStatement(query);
			stmt.setInt(1, timesheetId);
			ResultSet rs = stmt.executeQuery();
			rs = stmt.executeQuery();
			TimeSheetEntryBean bean;
			while (rs.next()){
				int hours[] = new int[7];
				bean = new TimeSheetEntryBean();
				bean.setTaskName(rs.getString("task_name"));
				bean.setTaskId(rs.getInt(1));
				bean.setHours(hours);
				for(int i=0;i<7;i++){
					hours[i] = rs.getInt(i+3);
				}
				entries.add(bean);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entries;
	}
	
	//get timesheet entries for given task and employee
	public static List<TimeSheetEntryBean> getTimeSheetsEntries(int taskId, int empId){
		List<TimeSheetEntryBean> entries = new ArrayList<TimeSheetEntryBean>();	
		Connection con = null;
		con = ConnectionManager.getConnection();
		PreparedStatement stmt;
		try {
			String query = "select task_id, t.timesheet_id, week_id , day1_hrs, day2_hrs, day3_hrs, day4_hrs, day5_hrs, day6_hrs, day7_hrs from timesheet_entries e, timesheet t " +
					"where t.timesheet_id = e.timesheet_id and t.emp_id = ? and e.task_id =? " ;
			stmt = con.prepareStatement(query);
			stmt.setInt(1, empId);
			stmt.setInt(2, taskId);
			ResultSet rs = stmt.executeQuery();
			TimeSheetEntryBean bean;
			while (rs.next()){
				int[] hours = new int[7];
				bean = new TimeSheetEntryBean();
				bean.setHours(hours);
				bean.setTaskId(rs.getInt("task_id"));
				bean.setWeekId(rs.getInt(3));
				for(int i=0;i<7;i++){
					hours[i] = rs.getInt(i+4);
				}
				entries.add(bean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entries;
	}
	
	// returns the timesheets submitted by subordinates for a given week to the manager 
	public static List<TimeSheetBean> getTimeSheetsToApprove(int weekId, int managerId){
		Connection con = null;
		con = ConnectionManager.getConnection();
		PreparedStatement stmt;
		String query ;
		ResultSet rs;
		List<TimeSheetBean> timesheets = new ArrayList<TimeSheetBean>();
		try {
			query = "select t.timesheet_id, t.emp_id, t.status from ts_emp e, timesheet t where e.emp_id = t.emp_id and manager_id = ? and t.status = 'S' and t.week_id = ?"; 
			stmt = con.prepareStatement(query);
			stmt.setInt(1, managerId);
			stmt.setInt(2, weekId);
			rs = stmt.executeQuery();
			TimeSheetBean timeSheet ;
			List<TimeSheetEntryBean> entries;
			while (rs.next()){
				timeSheet = new TimeSheetBean();
				timeSheet.setId(rs.getInt(1));
				timeSheet.setEmpId(rs.getInt(2));
				timeSheet.setStatus(rs.getString(3));
				entries = getTimeSheetsEntries(timeSheet.getId());
				timeSheet.setEntries(entries);
				timesheets.add(timeSheet);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return timesheets;
	}

	
	// checks whether a timesheet entry exists for a given emp, week, and task
	public static boolean timeSheetExists(int empId, int weekId, int taskId){
		String query = "select * from ts_work_log where emp_id = ? and task_id = ? and week_id = ? ";
		Connection con = null;
		con = ConnectionManager.getConnection();
		PreparedStatement stmt;
		try {
			stmt = con.prepareStatement(query);
			stmt.setInt(1, empId);
			stmt.setInt(2, taskId);
			stmt.setInt(3, weekId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean updateTimeSheetEntry(int timeSheetId, TimeSheetEntryBean entry) {
		Connection con = null;
		try {
				
			con = ConnectionManager.getConnection();	
			String updateTimeSheet = null;
			updateTimeSheet = "update timesheet_entries set day1_hrs = ?, day2_hrs = ?, day3_hrs = ?, day4_hrs = ?, day5_hrs = ?, day6_hrs = ?, day7_hrs = ? " +
					"where task_id = ?  and timesheet_id = ?" ;
			PreparedStatement  stmt = con.prepareStatement(updateTimeSheet);
			
			stmt.setInt(8,entry.getTaskId());
			stmt.setInt(9,timeSheetId);
			
			int hours[] = entry.getHours();
			
			for(int i=0;i<7;i++){
				stmt.setInt(1+i, hours[i]);
			}				
			int rows = stmt.executeUpdate();
			if(rows == 1){
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		finally{
			ConnectionManager.closeConnection(con);
		}
		return false;
	}

	public static List<Date> getEligibleDays(WeekBean week, TaskBean task){
		List<Date> eligibleDays = new LinkedList<Date>();
		
		// compare the task start and end dates with week start and end dates to show only the days eligible for a task in given week
		Date startDate , endDate;
		
		if(task.getStartDate().compareTo(week.getStartDate()) <= 0 )
			startDate = week.getStartDate();
		else 
			startDate = task.getStartDate();
		
		if(week.getEndDate().compareTo(task.getEndDate()) <= 0)
			endDate = week.getEndDate();
		else
			endDate = task.getEndDate();
		
		System.out.println(startDate);
		System.out.println(endDate);
		return DateUtils.getDatesInBetweend(startDate,endDate);
	}

	//status {'A'- Approved , 'N'-New, 'S'- Submitted, 'R'-Rejected}
	public static boolean updateTimesheet(int timesheetId, String status) {
		Connection con = null;
		try {		
			con = ConnectionManager.getConnection();	
			String updateTimeSheet = null;
			updateTimeSheet = "update timesheet set status = ? where timesheet_id = ?" ;
			PreparedStatement  stmt = con.prepareStatement(updateTimeSheet);
			stmt.setString(1,status);
			stmt.setInt(2,timesheetId);
			int rows = stmt.executeUpdate();
			if(rows > 0){
				return true;
			}				
		} catch (Exception e) {
			System.out.println(e);
		}
		finally{
			ConnectionManager.closeConnection(con);
		}
		return false;
	}
	
	public static boolean createTimeSheet(TimeSheetBean timeSheet) {
		
		if(timeSheet.getEntries().size() == 0)
			throw new IllegalArgumentException("Please add entries in the timesheet");
		
		Connection con = null;
		try {
			con = ConnectionManager.getConnection();
			String insertTimeSheet = null;
			String generatedColumns[] = { "TIMESHEET_ID" };
			insertTimeSheet = "insert into timesheet(week_id, timesheet_id, emp_id, status )values (?,ts_app_seq.nextval,?,'N')" ;
			PreparedStatement  stmt = con.prepareStatement(insertTimeSheet,generatedColumns);
			
			stmt.setInt(1,timeSheet.getWeek().getWeekId());
			stmt.setInt(2,timeSheet.getEmpId());

			stmt.executeUpdate();
			ResultSet generatedKeys = stmt.getGeneratedKeys();
			if (generatedKeys.next()) {
                timeSheet.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }	
			
			// insert all entries in the timesheet
			for(TimeSheetEntryBean entry : timeSheet.getEntries()){
				createTimeSheetEntry(timeSheet.getId(), entry);
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
		finally{
			ConnectionManager.closeConnection(con);
		}
		return true;
	}

	private static boolean createTimeSheetEntry(Integer timeSheetId,
			TimeSheetEntryBean entry) {
		Connection con = null;
		try {
			con = ConnectionManager.getConnection();
			String insertEntry = null;
			insertEntry = "insert into timesheet_entries(task_id, timesheet_id, day1_hrs, day2_hrs, day3_hrs, day4_hrs, day5_hrs, day6_hrs, day7_hrs)values (?,?,?,?,?,?,?,?,?)" ;
			PreparedStatement  stmt = con.prepareStatement(insertEntry);
			
			stmt.setInt(1,entry.getTaskId());
			stmt.setInt(2,timeSheetId);
			
			int days[] = entry.getHours();
			
			for(int i=0;i<7;i++){
				stmt.setInt(3+i, days[i]);
			}
							
			int rows = stmt.executeUpdate();
			if(rows == 1){
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		finally{
			ConnectionManager.closeConnection(con);
		}
		return false;
	}

	public static boolean updateTimeSheet(TimeSheetBean timeSheet) {
		if(timeSheet.getEntries().size() == 0)
			throw new IllegalArgumentException("Please add entries in the timesheet");
		
		// insert all entries in the timesheet
		for(TimeSheetEntryBean entry : timeSheet.getEntries()){
			updateTimeSheetEntry(timeSheet.getId(), entry);
		}
		
		return false;
	}

	public static boolean submitTimeSheet(Integer timesheetId) {
		return updateTimesheet(timesheetId,SUBMITTED);
	}
	
	public static String getStatus(int timesheetId){

		Connection con = null;
		con = ConnectionManager.getConnection();
		PreparedStatement stmt;
		String query ;
		ResultSet rs;
		String status = null;
		try {
			query = "select status from timesheet where timesheet_id = ?"; 
			stmt = con.prepareStatement(query);
			stmt.setInt(1, timesheetId);
			rs = stmt.executeQuery();
			while (rs.next()){
				status = rs.getString(1);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		finally{
			ConnectionManager.closeConnection(con);
		}
		return status;
	}
		
	public static boolean deleteTimeSheetEntry(int timesheetId, int taskId) {
		// delete a timesheet entry if timesheet is not submitted / approved.
		String status = getStatus(timesheetId);
		if(APPROVED == status || SUBMITTED == status)
			throw new IllegalArgumentException("Can't delete now");
		Connection con = null;
		try {	
			con = ConnectionManager.getConnection();	
			String deleteEntry= "delete timesheet_entries where task_id = ?  and timesheet_id = ?" ;
			PreparedStatement  stmt = con.prepareStatement(deleteEntry);
			stmt.setInt(1,taskId);
			stmt.setInt(2,timesheetId);				
			int rows = stmt.executeUpdate();
			if(rows == 1){
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		finally{
			ConnectionManager.closeConnection(con);
		}
		return false;
	}
}
