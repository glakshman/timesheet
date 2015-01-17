package tms.util;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class DateUtils {
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yy");
	private static TimeUnit tu = TimeUnit.DAYS;

	public static Date getDate(String date) throws ParseException{
		java.util.Date d = (java.util.Date) sdf.parse(date);
		return new java.sql.Date(d.getTime());
	}
	
	// returns the sql date objects for the days between startDate and endDate 
	public static List<Date> getDatesInBetweend(Date startDate, Date endDate) {
		
		int days = getDaysBetween(startDate,endDate);
		if(days < 0){
			return null;
		}
		
		Calendar cal = Calendar.getInstance(); 
		System.out.println(days);
		cal.setTimeInMillis(startDate.getTime());
		
		List<Date> datesList = new ArrayList<Date>();
		datesList.add(startDate);
		
		for(int i=0;i<days;i++){
			cal.add(Calendar.DATE, 1);
			Date date = new java.sql.Date(cal.getTimeInMillis());
			datesList.add(date);
		}
		return datesList;
		
	}

	// to calculate the number of days in between two date objects
	public static int getDaysBetween(Date startDate, Date endDate) {
		return (int) TimeUnit.MILLISECONDS.toDays(endDate.getTime() - startDate.getTime());
	}
}
