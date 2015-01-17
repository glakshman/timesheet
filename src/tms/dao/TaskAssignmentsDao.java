package tms.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;

import tms.beans.TaskAssignmentBean;
import tms.util.ConnectionManager;


public class TaskAssignmentsDao {
	// creates a task
	public static void insertTaskAssignment(TaskAssignmentBean bean){
		Connection con = null;
		try {
				con = ConnectionManager.getConnection();
				String insertEmp = "insert into ts_tasks_assingment(task_id,emp_id,created_by) values (?,?,?)" ;
				PreparedStatement  stmt = con.prepareStatement(insertEmp);
				
				stmt.setInt(1, bean.getTaskId());
				stmt.setInt(2, bean.getEmpId());
				stmt.setInt(3, bean.getCreatedBy());

					// step4 execute query
					int rows = stmt.executeUpdate();
					System.out.println("rows inserted "+rows);
		} catch (Exception e) {
			System.out.println(e);
		}
		finally{
			ConnectionManager.closeConnection(con);
		}
	}
	
}
