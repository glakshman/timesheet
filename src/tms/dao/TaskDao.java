package tms.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tms.beans.TaskBean;
import tms.beans.TimeSheetBean;
import tms.beans.WeekBean;
import tms.util.ConnectionManager;


public class TaskDao {
	public static boolean insertTask(TaskBean taskBean){
		Connection con = null;
		try {
				con = ConnectionManager.getConnection();
				String insertEmp = "insert into ts_tasks(task_id,task_name,description,start_date,end_date,created_by) values (ts_app_seq.nextval,?,?,?,?,?)" ;
				PreparedStatement  stmt = con.prepareStatement(insertEmp);
				
				stmt.setString(1, taskBean.getTaskName());
				stmt.setString(2, taskBean.getDescritpion());
				stmt.setDate(3, taskBean.getStartDate());
				stmt.setDate(4, taskBean.getEndDate());
				stmt.setInt(5, taskBean.getCreatedBy());

					// step4 execute query
					int rows = stmt.executeUpdate();
					if(rows == 1)
						return true;
					System.out.println("rows inserted "+rows);
		} catch (Exception e) {
			System.out.println(e);
		}
		finally{
			ConnectionManager.closeConnection(con);
		}
		return false;
	}

	// returns the tasks eligible for the given week for the given employee
	// returns the tasks which are starting/ending in that week and which are ongoing in that week for an employee
	public static List<TaskBean> getTasksForTheWeek(int empId, WeekBean week) {
		
		List<TaskBean> tasks = new ArrayList<TaskBean>();
		
		Connection con = null;
		con = ConnectionManager.getConnection();
		PreparedStatement stmt;
		try {
			
			
			String query = "select t.* from ts_tasks t, ts_tasks_assingment a where  a.emp_id = ? and a.task_id = t.task_id " +
					"and ((start_date between ? and ? ) or (end_date between ? and ? ) or ( start_date < ? and ? < end_date))";

			stmt = con.prepareStatement(query);
			
			stmt.setInt(1, empId);
			stmt.setDate(2, week.getStartDate());
			stmt.setDate(3, week.getEndDate());
			
			stmt.setDate(4, week.getStartDate());
			stmt.setDate(5, week.getEndDate());
			
			stmt.setDate(6, week.getStartDate());
			stmt.setDate(7, week.getEndDate());
			
			ResultSet rs = stmt.executeQuery();
			
			TaskBean taskBean ;
			
			while (rs.next()){
				taskBean = new TaskBean();
				taskBean.setTaskId(rs.getInt(1));
				taskBean.setTaskName(rs.getString(2));
				taskBean.setDescritpion(rs.getString(3));
				taskBean.setStartDate(rs.getDate(4));
				taskBean.setEndDate(rs.getDate(5));
				taskBean.setCreatedBy(rs.getInt(6));
				tasks.add(taskBean);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tasks;
	}
	
	 
	public static TaskBean getTask(int taskId) {
			Connection con = null;
			con = ConnectionManager.getConnection();
			PreparedStatement stmt;
			TaskBean taskBean = null;
			try {
				
				String query = "select * from ts_tasks t where task_id = ?"; 
				stmt = con.prepareStatement(query);
				stmt.setInt(1, taskId);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()){
					taskBean = new TaskBean();
					taskBean.setTaskId(rs.getInt(1));
					taskBean.setTaskName(rs.getString(2));
					taskBean.setDescritpion(rs.getString(3));
					taskBean.setStartDate(rs.getDate(4));
					taskBean.setEndDate(rs.getDate(5));
					taskBean.setCreatedBy(rs.getInt(6));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return taskBean;
		}

	public static List<TaskBean> getTasksCreatedBy(int createdBy) {
		List<TaskBean> tasks = new ArrayList<TaskBean>();
		
		Connection con = null;
		con = ConnectionManager.getConnection();
		PreparedStatement stmt;
		try {
			
			
			String query = "select * from ts_tasks t where created_by = ?"; 

			stmt = con.prepareStatement(query);
			
			stmt.setInt(1, createdBy);
			ResultSet rs = stmt.executeQuery();
			
			TaskBean taskBean ;
			
			while (rs.next()){
				taskBean = new TaskBean();
				taskBean.setTaskId(rs.getInt(1));
				taskBean.setTaskName(rs.getString(2));
				taskBean.setDescritpion(rs.getString(3));
				taskBean.setStartDate(rs.getDate(4));
				taskBean.setEndDate(rs.getDate(5));
				taskBean.setCreatedBy(rs.getInt(6));
				tasks.add(taskBean);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tasks;
	}
		
}
