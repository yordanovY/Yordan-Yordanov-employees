package objects;

import java.util.Date;

public class WorkPairs{

	private long employee1;
	private long employee2;
	private long projectID;
	private int daysTogether;
	private Date dateFrom;
	private Date dateTo;

	/**
	 * Data holding information for employee pairings on projects
	 * DATE			USER				NOTE:
	 * 2022.12.01	Yordan Yordanov		Created
	 */
	public WorkPairs(long employee1, long employee2, long projectID, int daysTogether, Date latestFrom,
			Date earliestTo) {
		super();
		this.employee1 = employee1;
		this.employee2 = employee2;
		this.projectID = projectID;
		this.daysTogether = daysTogether;
		this.dateFrom = latestFrom;
		this.dateTo = earliestTo;
	}

	public long getEmployee1() {
		return employee1;
	}

	public long getEmployee2() {
		return employee2;
	}

	public long getProjectID() {
		return projectID;
	}

	public long getDaysTogether() {
		return daysTogether;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

}
