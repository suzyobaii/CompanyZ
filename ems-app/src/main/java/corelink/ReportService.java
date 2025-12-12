package corelink;

public class ReportService {
    private final ReportDAO reportDAO = new ReportDAO();

    public String getPayHistory(int empId) {
        return reportDAO.getPayHistory(empId);
    }

    public String getMonthlyTotalByJobTitle(String monthYear) {
        return reportDAO.getMonthlyTotalByJobTitle(monthYear);
    }

    public String getMonthlyTotalByDivision(String monthYear) {
        return reportDAO.getMonthlyTotalByDivision(monthYear);
    }

    public String getEmployeesHiredBetween(String start, String end) {
        return reportDAO.getEmployeesHiredBetween(start, end);
    }
}
