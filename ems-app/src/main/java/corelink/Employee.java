public class Employee {
    private int empid;
    private String firstName;
    private String lastName;
    private String ssn;
    private String dob;
    private String hireDate;
    private double baseSalary;
    private String status;

    public Employee(int empid, String firstName, String lastName, String ssn,
                    String dob, String hireDate, double baseSalary, String status) {
        this.empid = empid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.dob = dob;
        this.hireDate = hireDate;
        this.baseSalary = baseSalary;
        this.status = status;
    }

    public int getEmpid() {
        return empid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empid=" + empid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", baseSalary=" + baseSalary +
                ", status='" + status + '\'' +
                '}';
    }
}
