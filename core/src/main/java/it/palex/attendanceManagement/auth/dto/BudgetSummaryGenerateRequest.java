package it.palex.attendanceManagement.auth.dto;

import java.util.Date;

public class BudgetSummaryGenerateRequest {
    private Date dateFrom;
    private Date dateTo;
    private Integer binsForTheRange;
    private Boolean addExpensesReport;
    private Boolean addHumanExpensesReport;

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getBinsForTheRange() {
        return binsForTheRange;
    }

    public void setBinsForTheRange(Integer binsForTheRange) {
        this.binsForTheRange = binsForTheRange;
    }

    public Boolean getAddExpensesReport() {
        return addExpensesReport;
    }

    public void setAddExpensesReport(Boolean addExpensesReport) {
        this.addExpensesReport = addExpensesReport;
    }

    public Boolean getAddHumanExpensesReport() {
        return addHumanExpensesReport;
    }

    public void setAddHumanExpensesReport(Boolean addHumanExpensesReport) {
        this.addHumanExpensesReport = addHumanExpensesReport;
    }
}
