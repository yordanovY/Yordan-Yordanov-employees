package objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import data.StringConstants;

public class ProjectEntry {

	/**
	 * Data holding information for every project associateted with employee
	 * DATE			USER				NOTE:
	 * 2022.12.01	Yordan Yordanov		Created
	 */
	private long projectID;
	private String dateFrom;
	private String dateTo;
	private String timeOnProject;
	private String timeFormat;

	public ProjectEntry(long projectID, String dateFrom, String dateTo, String timeFormat) {
		super();
		this.timeFormat = timeFormat;
		this.projectID = projectID;
		this.dateFrom = dateFrom;
		if (dateTo.equals(StringConstants.NULL)) {
			DateTimeFormatter currentDate = DateTimeFormatter.ofPattern(timeFormat);
			dateTo = currentDate.format(LocalDateTime.now());
		}
		this.dateTo = dateTo;
		try {
			this.timeOnProject = calculateTimeOnProject(dateFrom, dateTo);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public long getProjectID() {
		return projectID;
	}

	public void setProjectID(long projectID) {
		this.projectID = projectID;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String calculateTimeOnProject(String inputString1, String inputString2) throws ParseException {

		SimpleDateFormat myFormat = new SimpleDateFormat(timeFormat);

		Date date1 = myFormat.parse(inputString1);
		Date date2 = myFormat.parse(inputString2);
		long diff = date2.getTime() - date1.getTime();
		return String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

	}

	public String getTimeOnProject() {
		return timeOnProject;
	}

}
