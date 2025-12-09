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
    private String phone; 

    public Employee(int empid, String firstName, String lastName, String ssn,
                    Date dob, Date hireDate, double baseSalary, String status, String phone) {
        this.empid = empid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.dob = dob;
        this.hireDate = hireDate;
        this.baseSalary = baseSalary;
        this.status = status;
        this.phone = phone;
    }

    // getters
    public int getEmpid() { return empid; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public double getBaseSalary() { return baseSalary; }
    public String getStatus() { return status; }
    public Date getDob() { return dob; }
    public Date getHireDate() { return hireDate; }
    public String getPhone() { return phone; }

    // setters
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "Employee{" +
                "empid=" + empid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", baseSalary=" + baseSalary +
                ", status='" + status + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
