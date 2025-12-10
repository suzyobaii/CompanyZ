package corelink;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GUIMain extends JFrame {

    private static final EmployeeService employeeService = new EmployeeService();
    private static final PayrollService payrollService = new PayrollService();
    private static final ReportsService reportsService = new ReportsService();
    private static final AddressDAO addressDAO = new AddressDAO();

    private JTextArea outputArea;
    private JPanel loginPanel;
    private JPanel hrPanel;
    private JPanel employeePanel;

    public GUIMain() {
        setTitle("Company Z Employee Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- CENTERED BOLD TITLE LABEL ---
        JLabel titleLabel = new JLabel("Company Z Employee Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // --- OUTPUT AREA ---
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.SOUTH);

        // --- LOGIN PANEL BELOW TITLE ---
        loginPanel = createLoginPanel();
        add(loginPanel, BorderLayout.CENTER);  // moved so title stays on top

        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton hrButton = new JButton("HR Admin Login");
        JButton empButton = new JButton("Employee Login");
        JButton exitButton = new JButton("Exit");

        hrButton.addActionListener(e -> hrLogin());
        empButton.addActionListener(e -> employeeLogin());
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(hrButton);
        panel.add(empButton);
        panel.add(exitButton);

        return panel;
    }

    private void hrLogin() {
        JPanel loginForm = new JPanel(new GridLayout(3, 2));
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton cancelBtn = new JButton("Cancel");

        loginForm.add(new JLabel("Username:"));
        loginForm.add(userField);
        loginForm.add(new JLabel("Password:"));
        loginForm.add(passField);
        loginForm.add(loginBtn);
        loginForm.add(cancelBtn);

        JDialog dialog = new JDialog(this, "HR Login", true);
        dialog.setContentPane(loginForm);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        loginBtn.addActionListener(e -> {
            if ("admin".equals(userField.getText()) && "admin123".equals(new String(passField.getPassword()))) {
                dialog.dispose();
                showHRPanel();
            } else {
                JOptionPane.showMessageDialog(dialog, "Invalid credentials");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void employeeLogin() {
        JPanel loginForm = new JPanel(new GridLayout(3, 2));
        JTextField empIdField = new JTextField();
        JTextField lastNameField = new JTextField();
        JButton loginBtn = new JButton("Login");
        JButton cancelBtn = new JButton("Cancel");

        loginForm.add(new JLabel("Employee ID:"));
        loginForm.add(empIdField);
        loginForm.add(new JLabel("Last Name:"));
        loginForm.add(lastNameField);
        loginForm.add(loginBtn);
        loginForm.add(cancelBtn);

        JDialog dialog = new JDialog(this, "Employee Login", true);
        dialog.setContentPane(loginForm);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        loginBtn.addActionListener(e -> {
            try {
                int empid = Integer.parseInt(empIdField.getText());
                String lastName = lastNameField.getText();
                List<Employee> list = employeeService.searchEmployees(null, lastName, null, empid);
                if (!list.isEmpty()) {
                    dialog.dispose();
                    showEmployeePanel(empid);
                } else {
                    JOptionPane.showMessageDialog(dialog, "No matching employee found");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid Employee ID");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void showHRPanel() {
        remove(loginPanel);
        hrPanel = new JPanel(new GridLayout(6, 1));
        JButton searchBtn = new JButton("Search Employee");
        JButton addBtn = new JButton("Add New Employee");
        JButton updateBtn = new JButton("Update Employee Basic Data");
        JButton salaryBtn = new JButton("Increase Salary by % in Range");
        JButton reportsBtn = new JButton("Reports");
        JButton logoutBtn = new JButton("Logout");

        searchBtn.addActionListener(e -> hrSearchEmployee());
        addBtn.addActionListener(e -> hrAddEmployee());
        updateBtn.addActionListener(e -> hrUpdateEmployeeBasic());
        salaryBtn.addActionListener(e -> hrIncreaseSalary());
        reportsBtn.addActionListener(e -> hrReportsMenu());
        logoutBtn.addActionListener(e -> logout());

        hrPanel.add(searchBtn);
        hrPanel.add(addBtn);
        hrPanel.add(updateBtn);
        hrPanel.add(salaryBtn);
        hrPanel.add(reportsBtn);
        hrPanel.add(logoutBtn);

        add(hrPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    private void showEmployeePanel(int empid) {
        remove(loginPanel);
        employeePanel = new JPanel(new GridLayout(3, 1));
        JButton viewDataBtn = new JButton("View My Personal Data");
        JButton payHistoryBtn = new JButton("View My Pay Statement History");
        JButton logoutBtn = new JButton("Logout");

        viewDataBtn.addActionListener(e -> viewMyData(empid));
        payHistoryBtn.addActionListener(e -> {
            outputArea.setText("");
            payrollService.printPayHistory(empid);
        });
        logoutBtn.addActionListener(e -> logout());

        employeePanel.add(viewDataBtn);
        employeePanel.add(payHistoryBtn);
        employeePanel.add(logoutBtn);

        add(employeePanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    private void logout() {
        if (hrPanel != null) {
            remove(hrPanel);
        }
        if (employeePanel != null) {
            remove(employeePanel);
        }
        add(loginPanel, BorderLayout.NORTH);
        outputArea.setText("");
        revalidate();
        repaint();
    }

    private void hrSearchEmployee() {
        JPanel form = new JPanel(new GridLayout(5, 2));
        JTextField fnField = new JTextField();
        JTextField lnField = new JTextField();
        JTextField ssnField = new JTextField();
        JTextField empIdField = new JTextField();
        JButton searchBtn = new JButton("Search");

        form.add(new JLabel("First Name:"));
        form.add(fnField);
        form.add(new JLabel("Last Name:"));
        form.add(lnField);
        form.add(new JLabel("SSN:"));
        form.add(ssnField);
        form.add(new JLabel("EmpID:"));
        form.add(empIdField);
        form.add(searchBtn);

        JDialog dialog = new JDialog(this, "Search Employee", true);
        dialog.setContentPane(form);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        searchBtn.addActionListener(e -> {
            String fn = fnField.getText().isEmpty() ? null : fnField.getText();
            String ln = lnField.getText().isEmpty() ? null : lnField.getText();
            String ssn = ssnField.getText().isEmpty() ? null : ssnField.getText();
            Integer empid = empIdField.getText().isEmpty() ? null : Integer.parseInt(empIdField.getText());
            List<Employee> results = employeeService.searchEmployees(fn, ln, ssn, empid);
            outputArea.setText("");
            if (results.isEmpty()) {
                outputArea.append("No employees found.\n");
            } else {
                for (Employee emp : results) {
                    outputArea.append(emp.toString() + "\n");
                }
            }
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private void hrAddEmployee() {
        JPanel form = new JPanel(new GridLayout(14, 2));
        JTextField fnField = new JTextField();
        JTextField lnField = new JTextField();
        JTextField ssnField = new JTextField();
        JTextField dobField = new JTextField();
        JTextField hireField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField streetField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField zipField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField raceField = new JTextField();
        JTextField phoneField = new JTextField();
        JButton addBtn = new JButton("Add");

        form.add(new JLabel("First Name:"));
        form.add(fnField);
        form.add(new JLabel("Last Name:"));
        form.add(lnField);
        form.add(new JLabel("SSN:"));
        form.add(ssnField);
        form.add(new JLabel("DOB (YYYY-MM-DD):"));
        form.add(dobField);
        form.add(new JLabel("Hire Date (YYYY-MM-DD):"));
        form.add(hireField);
        form.add(new JLabel("Base Salary:"));
        form.add(salaryField);
        form.add(new JLabel("Street:"));
        form.add(streetField);
        form.add(new JLabel("City ID (1 to 20):"));
        form.add(cityField);
        form.add(new JLabel("State ID (1 to 50):"));
        form.add(stateField);
        form.add(new JLabel("Zip Code:"));
        form.add(zipField);
        form.add(new JLabel("Gender:"));
        form.add(genderField);
        form.add(new JLabel("Race:"));
        form.add(raceField);
        form.add(new JLabel("Phone Number:"));
        form.add(phoneField);
        form.add(addBtn);

        JDialog dialog = new JDialog(this, "Add Employee", true);
        dialog.setContentPane(form);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        addBtn.addActionListener(e -> {
            try {
                int newEmpid = employeeService.addEmployee(
                        fnField.getText(),
                        lnField.getText(),
                        ssnField.getText(),
                        Date.valueOf(dobField.getText()),
                        Date.valueOf(hireField.getText()),
                        Double.parseDouble(salaryField.getText()),
                        streetField.getText(),
                        Integer.valueOf(cityField.getText()),
                        Integer.valueOf(stateField.getText()),
                        zipField.getText(),
                        genderField.getText(),
                        raceField.getText(),
                        phoneField.getText()
                );
                if (newEmpid > 0) {
                    outputArea.append("New employee created with empid = " + newEmpid + "\n");
                } else {
                    outputArea.append("Failed to create employee.\n");
                }
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void hrUpdateEmployeeBasic() {
        JPanel form = new JPanel(new GridLayout(4, 2));
        JTextField empIdField = new JTextField();
        JTextField lnField = new JTextField();
        JTextField salaryField = new JTextField();
        JButton updateBtn = new JButton("Update");

        form.add(new JLabel("EmpID:"));
        form.add(empIdField);
        form.add(new JLabel("New Last Name:"));
        form.add(lnField);
        form.add(new JLabel("New Salary:"));
        form.add(salaryField);
        form.add(updateBtn);

        JDialog dialog = new JDialog(this, "Update Employee", true);
        dialog.setContentPane(form);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        updateBtn.addActionListener(e -> {
            try {
                employeeService.updateEmployeeBasic(
                        Integer.parseInt(empIdField.getText()),
                        lnField.getText(),
                        Double.parseDouble(salaryField.getText())
                );
                outputArea.append("Employee updated.\n");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void hrIncreaseSalary() {
        JPanel form = new JPanel(new GridLayout(4, 2));
        JTextField percentField = new JTextField();
        JTextField minField = new JTextField();
        JTextField maxField = new JTextField();
        JButton increaseBtn = new JButton("Increase");

        form.add(new JLabel("Percent:"));
        form.add(percentField);
        form.add(new JLabel("Min Salary:"));
        form.add(minField);
        form.add(new JLabel("Max Salary:"));
        form.add(maxField);
        form.add(increaseBtn);

        JDialog dialog = new JDialog(this, "Increase Salary", true);
        dialog.setContentPane(form);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        increaseBtn.addActionListener(e -> {
            try {
                employeeService.increaseSalaryByRange(
                        Double.parseDouble(percentField.getText()),
                        Double.parseDouble(minField.getText()),
                        Double.parseDouble(maxField.getText())
                );
                outputArea.append("Salaries updated.\n");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    //Will add back updateEmployeeAddress but I just want to run without errors first
    private void hrAddressMenu() {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JButton addBtn = new JButton("Add Address");
        JButton updateBtn = new JButton("Update Address");
        JButton viewBtn = new JButton("View Address");
        JButton backBtn = new JButton("Back");

        addBtn.addActionListener(e -> addEmployeeAddress());
        viewBtn.addActionListener(e -> viewEmployeeAddress());
        backBtn.addActionListener(e -> {
        }); // Placeholder, as it's modal

        panel.add(addBtn);
        panel.add(viewBtn);
        panel.add(backBtn);

        JDialog dialog = new JDialog(this, "Address Menu", true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addEmployeeAddress() {
        JPanel form = new JPanel(new GridLayout(10, 2));
        JTextField empIdField = new JTextField();
        JTextField streetField = new JTextField();
        JTextField cityIdField = new JTextField();
        JTextField stateIdField = new JTextField();
        JTextField zipField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField raceField = new JTextField();
        JTextField dobField = new JTextField();
        JTextField phoneField = new JTextField();
        JButton addBtn = new JButton("Add");

        form.add(new JLabel("EmpID:"));
        form.add(empIdField);
        form.add(new JLabel("Street:"));
        form.add(streetField);
        form.add(new JLabel("City ID:"));
        form.add(cityIdField);
        form.add(new JLabel("State ID:"));
        form.add(stateIdField);
        form.add(new JLabel("Zip:"));
        form.add(zipField);
        form.add(new JLabel("Gender:"));
        form.add(genderField);
        form.add(new JLabel("Race:"));
        form.add(raceField);
        form.add(new JLabel("DOB (YYYY-MM-DD):"));
        form.add(dobField);
        form.add(new JLabel("Phone:"));
        form.add(phoneField);
        form.add(addBtn);

        JDialog dialog = new JDialog(this, "Add Address", true);
        dialog.setContentPane(form);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        addBtn.addActionListener(e -> {
            try {
                boolean success = addressDAO.addAddress(
                        Integer.parseInt(empIdField.getText()),
                        streetField.getText(),
                        Integer.parseInt(cityIdField.getText()),
                        Integer.parseInt(stateIdField.getText()),
                        zipField.getText(),
                        genderField.getText(),
                        raceField.getText(),
                        Date.valueOf(dobField.getText()),
                        phoneField.getText()
                );
                outputArea.append(success ? "Address added.\n" : "Failed to add address.\n");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }
    
 private void viewEmployeeAddress() {
        JPanel form = new JPanel(new GridLayout(2, 2));
        JTextField empIdField = new JTextField();
        JButton viewBtn = new JButton("View");

        form.add(new JLabel("EmpID:"));
        form.add(empIdField);
        form.add(viewBtn);

        JDialog dialog = new JDialog(this, "View Address", true);
        dialog.setContentPane(form);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        viewBtn.addActionListener(e -> {
            try {
                Address addr = addressDAO.getAddressByEmpId(Integer.parseInt(empIdField.getText()));
                if (addr != null) {
                    outputArea.setText(addr.toString());
                } else {
                    outputArea.setText("Address not found.\n");
                }
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }
    
    private void hrReportsMenu() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JButton report1Btn = new JButton("Total Pay by Job Title");
        JButton report2Btn = new JButton("Total Pay by Division");
        JButton report3Btn = new JButton("Employees Hired in Range");
        JButton backBtn = new JButton("Back");

        report1Btn.addActionListener(e -> {
            String month = JOptionPane.showInputDialog("Enter month (YYYY-MM):");
            if (month != null) {
                payrollService.printTotalPayByJobTitle(month);
            }
        });
        report2Btn.addActionListener(e -> {
            String month = JOptionPane.showInputDialog("Enter month (YYYY-MM):");
            if (month != null) {
                payrollService.printTotalPayByDivision(month);
            }
        });
        report3Btn.addActionListener(e -> {
            String start = JOptionPane.showInputDialog("Start date (YYYY-MM-DD):");
            String end = JOptionPane.showInputDialog("End date (YYYY-MM-DD):");
            if (start != null && end != null) {
                reportsService.listEmployeesByHireDate(start, end);
            }
        });
        backBtn.addActionListener(e -> {
            // Close the dialog
        });

        panel.add(report1Btn);
        panel.add(report2Btn);
        panel.add(report3Btn);
        panel.add(backBtn);

        JDialog dialog = new JDialog(this, "Reports", true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void viewMyData(int empid) {
        List<Employee> list = employeeService.searchEmployees(null, null, null, empid);
        outputArea.setText("");
        if (list.isEmpty()) {
            outputArea.append("No data found.\n");
        } else {
            outputArea.append(list.get(0).toString() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUIMain());
    }
}