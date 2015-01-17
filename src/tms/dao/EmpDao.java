package tms.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tms.beans.EmpBean;
import tms.util.ConnectionManager;

public class EmpDao {
	public static boolean insertEmp(EmpBean emp){
		Connection con = null;
		int rowsInserted = 0;
		try {

				con = ConnectionManager.getConnection();
				String insertEmp = "insert into ts_emp(emp_id,ename,manager_id,email_id) values (ts_app_seq.nextval,:ename,:manager_id,:email_id)" ;
				PreparedStatement  stmt = con.prepareStatement(insertEmp);
				stmt.setString(1, emp.getEmpName());
				stmt.setInt(2,emp.getManagerId() );
				stmt.setString(3, emp.getEmailId());
					// step4 execute query
					rowsInserted = stmt.executeUpdate();
					if(rowsInserted ==1 )
						return true;
					System.out.println("rows inserted "+rowsInserted);
		} catch (Exception e) {
			System.out.println(e);
		}
		finally{
			ConnectionManager.closeConnection(con);
		}
		return false;
	}
	
	public static EmpBean getEmpBean(String emailId){
		EmpBean empBean = null;
		Connection con = null;
		con = ConnectionManager.getConnection();
		PreparedStatement stmt;
		try {
			String query = "select * from ts_emp where email_id = ?";
			stmt = con.prepareStatement(query);
			stmt.setString(1, emailId);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				empBean = new EmpBean();
				empBean.setEmpId(rs.getInt("emp_id"));
				empBean.setEmpName(rs.getString("ename"));
				empBean.setEmailId(rs.getString("email_id"));
				empBean.setManagerId(rs.getInt("manager_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return empBean;
	}
}
