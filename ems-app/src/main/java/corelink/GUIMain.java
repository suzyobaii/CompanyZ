package corelink;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class GUIMain extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private EmployeeService employeeService;
    private PayrollService payrollService;
    private AddressDAO addressDAO;
    private Reports reports;

    // Panels for different screens
    private LoginPanel loginPanel;
    private HRMenuPanel hrMenuPanel;
    private EmployeeMenuPanel employeeMenuPanel;
    private SearchEmployeePanel searchEmployeePanel;
    private AddEmployeePanel addEmployeePanel;
    private UpdateEmployeePanel updateEmployeePanel;
    private AddressMenuPanel addressMenuPanel;
    private ReportsMenuPanel reportsMenuPanel;

    // Current user state
    private boolean isHR = false;
    private Integer currentEmpId = null;

    public GUIMain() {
        employeeService = new EmployeeService();
        payrollService = new PayrollService();
        addressDAO = new AddressDAO();
        reports = new Reports();

        setTitle("Employee Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        loginPanel = new LoginPanel();
        hrMenuPanel = new HRMenuPanel();
        employeeMenuPanel = new EmployeeMenuPanel();
        searchEmployeePanel = new SearchEmployeePanel();
        addEmployeePanel = new AddEmployeePanel();
        updateEmployeePanel = new UpdateEmployeePanel();
        addressMenuPanel = new AddressMenuPanel();
        reportsMenuPanel = new ReportsMenuPanel();

        // Add panels to main panel
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(hrMenuPanel, "HRMenu");
        mainPanel.add(employeeMenuPanel, "EmployeeMenu");
        mainPanel.add(searchEmployeePanel, "SearchEmployee");
        mainPanel.add(addEmployeePanel, "AddEmployee");
        mainPanel.add(updateEmployeePanel, "UpdateEmployee");
        mainPanel.add(addressMenuPanel, "AddressMenu");
        mainPanel.add(reportsMenuPanel, "ReportsMenu");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    // Login Panel
    private class LoginPanel extends JPanel {
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JComboBox<String> roleCombo;
        private JButton loginButton;

        public LoginPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            gbc.gridx = 0; gbc.gridy = 0;
            add(new JLabel("Username:"), gbc);
            gbc.gridx = 1;
            usernameField = new JTextField(20);
            add(usernameField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            add(new JLabel("Password:"), gbc);
            gbc.gridx = 1;
            passwordField = new JPasswordField(20);
            add(passwordField, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            add(new JLabel("Role:"), gbc);
            gbc.gridx = 1;
            roleCombo = new JComboBox<>(new String[]{"HR", "Employee"});
            add(roleCombo, gbc);

            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            loginButton = new JButton("Login");
            add(loginButton, gbc);

            loginButton.addActionListener(e -> {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String role = (String) roleCombo.getSelectedItem();

                if ("HR".equals(role)) {
                    if ("admin".equals(username) && "admin123".equals(password)) {
                        isHR = true;
                        cardLayout.show(mainPanel, "HRMenu");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid HR credentials.");
                    }
                } else if ("Employee".equals(role)) {
                    try {
                        int empid = Integer.parseInt(username);
                        List<Employee> list = employeeService.searchEmployees(null, password, null, empid);
                        if (!list.isEmpty()) {
                            currentEmpId = empid;
                            cardLayout.show(mainPanel, "EmployeeMenu");
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid employee credentials.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid Employee ID.");
                    }
                }
            });
        }
    }

    // HR Menu Panel
    private class HRMenuPanel extends JPanel {
        public HRMenuPanel() {
            setLayout(new GridLayout(8, 1, 10, 10));
            JButton searchBtn = new JButton("Search Employee");
            JButton addBtn = new JButton("Add New Employee");
            JButton updateBtn = new JButton("Update Employee Basic Data");
            JButton salaryBtn = new JButton("Increase Salary by % in Range");
            JButton deleteBtn = new JButton("Delete Employee");
            JButton addressBtn = new JButton("Address / Demographics");
            JButton reportsBtn = new JButton("Reports");
            JButton logoutBtn = new JButton("Logout");

            add(searchBtn);
            add(addBtn);
            add(updateBtn);
            add(salaryBtn);
            add(deleteBtn);
            add(addressBtn);
            add(reportsBtn);
            add(logoutBtn);

            searchBtn.addActionListener(e -> cardLayout.show(mainPanel, "SearchEmployee"));
            addBtn.addActionListener(e -> cardLayout.show(mainPanel, "AddEmployee"));
            updateBtn.addActionListener(e -> cardLayout.show(mainPanel, "UpdateEmployee"));
            salaryBtn.addActionListener(e -> {
                // Inline salary increase
                String percentStr = JOptionPane.showInputDialog("Percent increase (e.g., 3.2):");
                String minStr = JOptionPane.showInputDialog("Min salary:");
                String maxStr = JOptionPane.showInputDialog("Max salary:");
                try {
                    double percent = Double.parseDouble(percentStr);
                    double min = Double.parseDouble(minStr);
                    double max = Double.parseDouble(maxStr);
                    employeeService.increaseSalaryByRange(percent, min, max);
                    JOptionPane.showMessageDialog(this, "Salary increased.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });
            deleteBtn.addActionListener(e -> {
                String empidStr = JOptionPane.showInputDialog("EmpID to delete:");
                try {
                    int empid = Integer.parseInt(empidStr);
                    employeeService.deleteEmployee(empid);
                    JOptionPane.showMessageDialog(this, "Employee deleted.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });
            addressBtn.addActionListener(e -> cardLayout.show(mainPanel, "AddressMenu"));
            reportsBtn.addActionListener(e -> cardLayout.show(mainPanel, "ReportsMenu"));
            logoutBtn.addActionListener(e -> {
                isHR = false;
                cardLayout.show(mainPanel, "Login");
            });
        }
    }

    // Employee Menu Panel
    private class EmployeeMenuPanel extends JPanel {
        public EmployeeMenuPanel() {
            setLayout(new GridLayout(3, 1, 10, 10));
            JButton viewDataBtn = new JButton("View My Personal Data");
            JButton payHistoryBtn = new JButton("View My Pay Statement History");
            JButton logoutBtn = new JButton("Logout");

            add(viewDataBtn);
            add(payHistoryBtn);
            add(logoutBtn);

            viewDataBtn.addActionListener(e -> {
                List<Employee> list = employeeService.searchEmployees(null, null, null, currentEmpId);
                if (!list.isEmpty()) {
                    JOptionPane.showMessageDialog(this, list.get(0).toString());
                } else {
                    JOptionPane.showMessageDialog(this, "No data found.");
                }
            });
            payHistoryBtn.addActionListener(e -> {
                payrollService.printPayHistory(currentEmpId);
                JOptionPane.showMessageDialog(this, "Check console for pay history.");
            });
            logoutBtn.addActionListener(e -> {
                currentEmpId = null;
                cardLayout.show(mainPanel, "Login");
            });
        }
    }

    // Search Employee Panel
    private class SearchEmployeePanel extends JPanel {
        private JTextField firstNameField, lastNameField, ssnField, empIdField;
        private JButton searchBtn, backBtn;
        private JTextArea resultsArea;

        public SearchEmployeePanel() {
            setLayout(new BorderLayout());
            JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
            inputPanel.add(new JLabel("First Name:"));
            firstNameField = new JTextField();
            inputPanel.add(firstNameField);
            inputPanel.add(new JLabel("Last Name:"));
            lastNameField = new JTextField();
            inputPanel.add(lastNameField);
            inputPanel.add(new JLabel("SSN:"));
            ssnField = new JTextField();
            inputPanel.add(ssnField);
            inputPanel.add(new JLabel("EmpID:"));
            empIdField = new JTextField();
            inputPanel.add(empIdField);
            searchBtn = new JButton("Search");
            inputPanel.add(searchBtn);
            backBtn = new JButton("Back");
            inputPanel.add(backBtn);

            resultsArea = new JTextArea(10, 50);
            resultsArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(resultsArea);

            add(inputPanel, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);

            searchBtn.addActionListener(e -> {
                String fn = firstNameField.getText().trim();
                String ln = lastNameField.getText().trim();
                String ssn = ssnField.getText().trim();
                Integer empid = null;
                if (!empIdField.getText().trim().isEmpty()) {
                    try {
                        empid = Integer.parseInt(empIdField.getText().trim());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid EmpID.");
                        return;
                    }
                }
                List<Employee> results = employeeService.searchEmployees(
                    fn.isEmpty() ? null : fn, ln.isEmpty() ? null : ln,
                    ssn.isEmpty() ? null : ssn, empid
                );
                resultsArea.setText("");
                if (results.isEmpty()) {
                    resultsArea.append("No employees found.\n");
                } else {
                    for (Employee emp : results) {
                        resultsArea.append(emp.toString() + "\n");
                    }
                }
            });
            backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HRMenu"));
        }
    }

    // Add Employee Panel (similar structure; add fields as needed)
    private class AddEmployeePanel extends JPanel {
        // Add JTextFields for all required fields (firstName, lastName, etc.)
        // On submit, call employeeService.addEmployee(...)
        // For brevity, implement similarly to SearchEmployeePanel
        public AddEmployeePanel() {
            // TODO: Implement with fields for firstName, lastName, ssn, dob, hireDate, baseSalary, street, cityId, stateId, zip, gender, identifiedRace, phone
            // Use GridLayout or BoxLayout for inputs
            // On button click: parse inputs, call addEmployee, show success/error
            add(new JLabel("Add Employee - Implement fields here"));
            JButton backBtn = new JButton("Back");
            backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HRMenu"));
            add(backBtn);
        }
    }

    // Update Employee Panel
    private class UpdateEmployeePanel extends JPanel {
        private JTextField empIdField, lastNameField, salaryField;
        private JButton updateBtn, backBtn;

        public UpdateEmployeePanel() {
            setLayout(new GridLayout(4, 2, 5, 5));
            add(new JLabel("EmpID:"));
            empIdField = new JTextField();
            add(empIdField);
            add(new JLabel("New Last Name:"));
            lastNameField = new JTextField();
            add(lastNameField);
            add(new JLabel("New Salary:"));
            salaryField = new JTextField();
            add(salaryField);
            updateBtn = new JButton("Update");
            add(updateBtn);
            backBtn = new JButton("Back");
            add(backBtn);

            updateBtn.addActionListener(e -> {
                try {
                    int empid = Integer.parseInt(empIdField.getText().trim());
                    String lastName = lastNameField.getText().trim();
                    double salary = Double.parseDouble(salaryField.getText().trim());
                    employeeService.updateEmployeeBasic(empid, lastName, salary);
                    JOptionPane.showMessageDialog(this, "Updated.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });
            backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HRMenu"));
        }
    }

    // Address Menu Panel
    private class AddressMenuPanel extends JPanel {
        public AddressMenuPanel() {
            setLayout(new GridLayout(3, 1, 10, 10));
            JButton updateBtn = new JButton("Update Employee Address");
            JButton viewBtn = new JButton("View Employee Address");
            JButton backBtn = new JButton("Back");

            add(updateBtn);
            add(viewBtn);
            add(backBtn);

            updateBtn.addActionListener(e -> {
                // Inline update (or create a sub-panel)
                String empidStr = JOptionPane.showInputDialog("EmpID:");
                // Prompt for each field, then call addressDAO.updateAddress(...)
                JOptionPane.showMessageDialog(this, "Update logic here.");
            });
            viewBtn.addActionListener(e -> {
                String empidStr = JOptionPane.showInputDialog("EmpID:");
                try {
                    int empid = Integer.parseInt(empidStr);
                    Address addr = addressDAO.getAddressByEmpId(empid);
                    JOptionPane.showMessageDialog(this, addr != null ? addr.toString() : "Not found.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });
            backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HRMenu"));
        }
    }

    // Reports Menu Panel
    private class ReportsMenuPanel extends JPanel {
        public ReportsMenuPanel() {
            setLayout(new GridLayout(4, 1, 10, 10));
            JButton jobTitleBtn = new JButton("Total pay for month by job title");
            JButton divisionBtn = new JButton("Total pay for month by division");
            JButton hireBtn = new JButton("Employees hired within a date range");
            JButton backBtn = new JButton("Back");

            add(jobTitleBtn);
            add(divisionBtn);
            add(hireBtn);
            add(backBtn);

            jobTitleBtn.addActionListener(e -> {
                String month = JOptionPane.showInputDialog("Month (YYYY-MM):");
                payrollService.printTotalPayByJobTitle(month);
                JOptionPane.showMessageDialog(this, "Check console.");
            });
            divisionBtn.addActionListener(e -> {
                String month = JOptionPane.showInputDialog("Month (YYYY-MM):");
                payrollService.printTotalPayByDivision(month);
                JOptionPane.showMessageDialog(this, "Check console.");
            });
            hireBtn.addActionListener(e -> {
                String startStr = JOptionPane.showInputDialog("Start Date (YYYY-MM-DD):");
                String endStr = JOptionPane.showInputDialog("End Date (YYYY-MM-DD):");
                try {
                    LocalDate start = LocalDate.parse(startStr);
                    LocalDate end = LocalDate.parse(endStr);
                    reports.printEmployeesHiredBetween(start, end);
                    JOptionPane.showMessageDialog(this, "Check console.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid dates.");
                }
            });
            backBtn.addActionListener(e -> cardLayout.show(mainPanel, "HRMenu"));
        }
    }

    public static void main(String[] args) {
        // Test DB connection
        try (Connection conn = Database.getConnection()) {
            if (conn != null) {
                System.out.println("Database connection successful!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "DB Error: " + e.getMessage());
            return;
        }

        SwingUtilities.invokeLater(() -> {
            new GUIMain().setVisible(true);
        });
    }
}
