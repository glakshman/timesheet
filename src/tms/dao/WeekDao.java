package tms.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tms.beans.WeekBean;
import tms.util.ConnectionManager;


public class WeekDao {
	//returns the number of latest week beans including current week to access the start and end dates 
	public static List<WeekBean> getWeekBeans(int numberOfWeeks){
		
		List<WeekBean> weeks = new ArrayList<WeekBean>();
		
		Connection con = null;
		con = ConnectionManager.getConnection();
		PreparedStatement stmt;
		try {
			String query = "select rownum, ts_weeks.* from ts_weeks where rownum < ? order by end_date desc";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, numberOfWeeks);
			ResultSet rs = stmt.executeQuery();
			List<Integer> hrs;
			WeekBean weekBean;
			while (rs.next()){
				weekBean = new WeekBean();
				weekBean.setWeekId(rs.getInt(2));
				weekBean.setStartDate(rs.getDate(3));
				weekBean.setEndDate(rs.getDate(4));
				weeks.add(weekBean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return weeks;
		
	}

	public static WeekBean getWeek(int weekId) {
		
		Connection con = null;
		con = ConnectionManager.getConnection();
		PreparedStatement stmt;
		WeekBean weekBean = new WeekBean();
		try {
			String query = "select week_id,start_date,end_date from ts_weeks where week_id = ?";
			stmt = con.prepareStatement(query);
			stmt.setInt(1, weekId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				weekBean.setWeekId(rs.getInt(1));
				weekBean.setStartDate(rs.getDate(2));
				weekBean.setEndDate(rs.getDate(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return weekBean;
	}
}
