package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import data.StringConstants;
import objects.Employee;
import objects.WorkPairs;
import process.Process_CSV;
import process.SortMethod;

public class GUI {
	HashMap<Long, Employee> masterEmployees = null;
	ArrayList<WorkPairs> workpairs = null;

	public void graphicalInterface() {

		JFrame frame;
		JTextField fileField;
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		fileField = new JTextField();
		fileField.setColumns(10);
		fileField.setDragEnabled(true);
		
		// Buttons
		JButton button_selectFile = new JButton(StringConstants.SELECT_FILE);
		JButton button_processData = new JButton(StringConstants.SORT_PAIRS);
		JButton button_displayFullData = new JButton(StringConstants.DISPLAY_DATA);

		// Slider and Formatting
		String formats[] = { StringConstants.YYMMDD, StringConstants.MMDDYY, StringConstants.DDMMYY };
		@SuppressWarnings({ "rawtypes", "unchecked" })
		JComboBox dropDown_format = new JComboBox(formats);
		JLabel label_format = new JLabel("Format:");

		// Element positioning
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout
						.createParallelGroup(Alignment.TRAILING)
						.addComponent(fileField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup().addComponent(button_selectFile)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(button_processData)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(button_displayFullData)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_format)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(dropDown_format, 0, 183, Short.MAX_VALUE)))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addComponent(fileField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(button_selectFile)
								.addComponent(button_processData).addComponent(button_displayFullData)
								.addComponent(dropDown_format, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(label_format))
						.addGap(18).addContainerGap()));

		// Select File and process the information
		button_selectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int option = fileChooser.showOpenDialog(frame);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					fileField.setText(file.getAbsolutePath());
					Process_CSV csv_process = new Process_CSV();
					try {
						csv_process.processCSV(file, dropDown_format.getSelectedItem().toString());
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					masterEmployees = csv_process.getEmployees();
					workpairs = csv_process.getWorkPairs();
				}
			}
		});

		// Display formatted table
		button_displayFullData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int tableSize = 0;
				if (masterEmployees != null) {
					for (long employee_key : masterEmployees.keySet()) {
						for (int i = 0; i < masterEmployees.get(employee_key).getProjects().size(); i++) {
							tableSize++;
						}
					}
					String[] columnNames = { StringConstants.EMP_ID, StringConstants.PROJECT_ID,
							StringConstants.DATE_FROM, StringConstants.DATE_TO };
					Object[][] tableData = new Object[tableSize][4];
					JFrame frm = new JFrame();
					JTable table = new JTable(tableData, columnNames);
					int index = 0;
					try {
						table.repaint();
						for (long employee_key : masterEmployees.keySet()) {
							for (int i = 0; i < masterEmployees.get(employee_key).getProjects().size(); i++) {
								tableData[index][0] = masterEmployees.get(employee_key).getEmployeeID();
								tableData[index][1] = masterEmployees.get(employee_key).getProjects().get(i)
										.getProjectID();
								tableData[index][2] = masterEmployees.get(employee_key).getProjects().get(i)
										.getDateFrom();
								tableData[index][3] = masterEmployees.get(employee_key).getProjects().get(i)
										.getDateTo();
								index++;
							}
						}
						table.repaint();
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
					}
					table.setBounds(30, 40, 200, 300);
					JScrollPane sp = new JScrollPane(table);
					frm.add(sp);
					frm.setSize(500, 400);
					frm.setVisible(true);
				}
			}
		});

		// Display pairs
		button_processData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (workpairs != null) {
					JFrame frm = new JFrame();
					String column[] = { StringConstants.EMP_ID1, StringConstants.EMP_ID2, StringConstants.PROJECT_ID,
							StringConstants.TOTAL_DAYS };
					int index = 0;
					Collections.sort(workpairs, new SortMethod());
					Object[][] workpairData = new Object[workpairs.size()][4];
					try {
						for (int i = 0; i < workpairs.size(); i++) {
							workpairData[index][0] = workpairs.get(i).getEmployee1();
							workpairData[index][1] = workpairs.get(i).getEmployee2();
							workpairData[index][2] = workpairs.get(i).getProjectID();
							workpairData[index][3] = workpairs.get(i).getDaysTogether();
							index++;
						}
						JTable jt = new JTable(workpairData, column);
						jt.setBounds(30, 40, 200, 300);
						JScrollPane sp = new JScrollPane(jt);
						frm.add(sp);
						frm.setSize(500, 400);
						frm.setVisible(true);
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
