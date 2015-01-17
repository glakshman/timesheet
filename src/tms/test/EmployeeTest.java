package tms.test;
import java.text.ParseException;
import java.util.List;

import tms.beans.EmpBean;
import tms.beans.TaskAssignmentBean;
import tms.beans.TaskBean;
import tms.dao.EmpDao;
import tms.dao.TaskAssignmentsDao;
import tms.dao.TaskDao;
import tms.util.DateUtils;


public class EmployeeTest {

	
	public static void main(String[] args) {
	
	// assuming that first employee(empId = 1) is already populated.
	// manager can create employees, tasks, and assign tasks for employees
		
		int empId = 1;
		EmpBean empBean = new EmpBean();
		String emailId = "lokesh@abc.com";
		empBean.setEmailId(emailId);
		empBean.setEmpName("Lokesh");
		empBean.setManagerId(1);
//		if(EmpDao.insertEmp(empBean))
//			System.out.println("inserted successfully");
//		
		empBean = EmpDao.getEmpBean(emailId);
		System.out.println(empBean);
		
		// creating 3 tasks and assign them to the the emp "Lokesh"
		// task       startDate       endDate 
		createTasks("Bug 123 ", "10-01-2015","15-01-2015",empId,empBean.getEmpId());
		createTasks("Coding ", "10-01-2015","20-01-2015",empId,empBean.getEmpId());
		createTasks("Bug 200 ", "15-01-2015","20-01-2015",empId,empBean.getEmpId());

	//get tasks created by empId  = 1 and assign them to emp = Lokesh
		List<TaskBean> tasks = TaskDao.getTasksCreatedBy(empId);
		TaskAssignmentBean taskAssingmentBean;
		for(TaskBean task : tasks){
			System.out.println("assingning task "+task.getTaskName());
			taskAssingmentBean = new TaskAssignmentBean();
			taskAssingmentBean.setCreatedBy(empId);
			taskAssingmentBean.setTaskId(task.getTaskId());
			taskAssingmentBean.setEmpId(empBean.getEmpId());
			TaskAssignmentsDao.insertTaskAssignment(taskAssingmentBean);
		}
	}

	// dummy method to intialize tasks and assignments
	private static void createTasks(String taskName, String startDate,
			String endDate, int createdBy, int createdTo) {
		TaskBean taskBean =  new TaskBean();
		taskBean.setTaskName(taskName);
		try {
			taskBean.setStartDate(DateUtils.getDate(startDate));
			taskBean.setEndDate(DateUtils.getDate(endDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		taskBean.setCreatedBy(createdBy);
		if(TaskDao.insertTask(taskBean))
			System.out.println("task created successfully");
	}

}
