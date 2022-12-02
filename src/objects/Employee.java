package objects;

import java.util.ArrayList;

public class Employee {

	private long employeeID;
	private ArrayList<ProjectEntry> projects = new ArrayList<>();

	/**
	 * Data holding information for every employee
	 * DATE			USER				NOTE:
	 * 2022.12.01	Yordan Yordanov		Created
	 */
	public Employee(long employeeID) {
		super();
		this.employeeID = employeeID;
	}

	public long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(long employeeID) {
		this.employeeID = employeeID;
	}

	public ArrayList<ProjectEntry> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<ProjectEntry> projects) {
		this.projects = projects;
	}

	public void addToProject(ProjectEntry e) {
		projects.add(e);
	}

}
