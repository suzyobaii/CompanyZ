package corelink;

import java.sql.Date;

public class Employee {
    private int empid;
    private String firstName;
    private String lastName;
    private String ssn;
    private Date dob;
    private Date hireDate;
    private double baseSalary;
    private String status;

    // getters
    public int getEmpid() { return empid; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getSSN() { return ssn; }
    public Date getDob() { return dob; }
    public Date getHireDate() { return hireDate; }
    public double getBaseSalary() { return baseSalary; }
    public String getStatus() { return status; }


    // setters
    public void setEmpid(int empid) { this.empid = empid; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setSSN(String ssn) { this.ssn = ssn; }
    public void setDOB(Date dob) { this.dob = dob; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Employee Details\n=============================================\n" +
                "\nEmployee ID: " + empid +
                "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName +
                "\nDate of Birth: " + dob +
                "\nHire Date: " + hireDate +
                "\nBase Salary: $" + baseSalary +
                "\nStatus: " + status;
    }
}
