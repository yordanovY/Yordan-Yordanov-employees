package process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import data.StringConstants;
import objects.Employee;
import objects.ProjectEntry;
import objects.WorkPairs;

public class Process_CSV {

	HashMap<Long, Employee> employees = new HashMap<Long, Employee>();
	ArrayList<WorkPairs> workPairs = new ArrayList<WorkPairs>();

	/**
	 * Read and parse CSV files to constructs collection of employees, project entries and employee pairs
	 * DATE			USER				NOTE:
	 * 2022.12.01	Yordan Yordanov		Created
	 */
	public void processCSV(File csvFile, String timeFormat) throws NumberFormatException, IOException {
		// StringConstants textlinez = new StringConstants();
		employees.clear();
		workPairs.clear();
		if (!csvFile.getName().endsWith(StringConstants.fileExtension)) {
			System.out.println("Improper file format");
		}
		FileInputStream fileStream = new FileInputStream(csvFile.getAbsoluteFile());
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fileStream));

		String strLine;
		// Read File Line By Line
		while ((strLine = bufferReader.readLine()) != null) {
			String[] parts = strLine.split(StringConstants.COMMA);
			//if file has more than 4 entries then the data is incorrect, remove whitespaces for the purpose of parsing
			if (parts.length == 4) {
				long employeeID = Long
						.parseLong(parts[0].replace(StringConstants.WHITE_SPACE, StringConstants.NO_SPACE));
				long projectID = Long
						.parseLong(parts[1].replace(StringConstants.WHITE_SPACE, StringConstants.NO_SPACE));
				String dateFrom = parts[2].replace(StringConstants.WHITE_SPACE, StringConstants.NO_SPACE);
				String dateTo = parts[3].replace(StringConstants.WHITE_SPACE, StringConstants.NO_SPACE);
				if (!employees.containsKey(employeeID)) {
					employees.put(employeeID, new Employee(employeeID));
				}
				employees.get(employeeID).addToProject(new ProjectEntry(projectID, dateFrom, dateTo, timeFormat));

			} else {
				System.out.println("Improper data format in CSV file.");
			}
		}
		fileStream.close();
		ArrayList<Long> traversedEmployees = new ArrayList<Long>();
		//Iterate through the collections to create a employee pairs 
		for (Long employee1 : employees.keySet()) {
			traversedEmployees.add(employee1);
			for (Long employee2 : employees.keySet()) {
				if (employee1 != employee2) {
					for (int pj1 = 0; pj1 < employees.get(employee1).getProjects().size(); pj1++) {
						for (int pj2 = 0; pj2 < employees.get(employee2).getProjects().size(); pj2++) {
							if (traversedEmployees.contains(employee2)) {
								break;
							}
							if (employees.get(employee1).getProjects().get(pj1).getProjectID() == employees
									.get(employee2).getProjects().get(pj2).getProjectID()) {
								// Choosing dateFrom for working on the same project
								SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat);
								long timeDiff;
								int daysDiff;
								try {
									Date dateFromEmployee1 = dateFormat
											.parse(employees.get(employee1).getProjects().get(pj1).getDateFrom());
									Date dateFromEmployee2 = dateFormat
											.parse(employees.get(employee2).getProjects().get(pj2).getDateFrom());
									Date dateToEmployee1 = dateFormat
											.parse(employees.get(employee1).getProjects().get(pj1).getDateTo());
									Date dateToEmployee2 = dateFormat
											.parse(employees.get(employee2).getProjects().get(pj2).getDateTo());
									Date latestFrom = null;
									Date earliestTo = null;
									// Date From
									if (dateFromEmployee1.compareTo(dateFromEmployee2) > 0) {
										latestFrom = dateFromEmployee1;
									} else if (dateFromEmployee1.compareTo(dateFromEmployee2) < 0) {
										latestFrom = dateFromEmployee2;
									} else {
										latestFrom = dateFromEmployee2;
									}
									// Date To
									if (dateToEmployee1.compareTo(dateToEmployee2) > 0) {
										earliestTo = dateToEmployee1;
									} else if (dateToEmployee1.compareTo(dateToEmployee2) < 0) {
										earliestTo = dateToEmployee2;
									} else {
										earliestTo = dateToEmployee2;
									}
									if (latestFrom.compareTo(earliestTo) > 0) {
										System.out.println("No overlap");
									} else {
										timeDiff = Math.abs(earliestTo.getTime() - latestFrom.getTime());
										daysDiff = (int) TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
										workPairs.add(new WorkPairs(employees.get(employee1).getEmployeeID(),
												employees.get(employee2).getEmployeeID(),
												employees.get(employee1).getProjects().get(pj1).getProjectID(),
												daysDiff, latestFrom, earliestTo));
									}
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
						}
					}

				}
			}
		}
	}

	public HashMap<Long, Employee> getEmployees() {
		return employees;
	}

	public ArrayList<WorkPairs> getWorkPairs() {
		return workPairs;
	}

}
