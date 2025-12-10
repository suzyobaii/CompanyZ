package corelink;

public class PayrollService {
    private PayrollDAO PayrollDAO;

    public PayrollService() {
        this.PayrollDAO = new PayrollDAO();
    }

    public void printPayHistory(int empId) {
        PayrollDAO.printPayHistory(empId);
    }

    public void printTotalPayByJobTitle(String monthYear) {
        PayrollDAO.printTotalPayByJobTitle(monthYear);
    }

    public void printTotalPayByDivision(String monthYear) {
        PayrollDAO.printTotalPayByDivision(monthYear);
    }
}
