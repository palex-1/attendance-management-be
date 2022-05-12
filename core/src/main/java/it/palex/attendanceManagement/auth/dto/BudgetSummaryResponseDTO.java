package it.palex.attendanceManagement.auth.dto;

import java.util.List;

public class BudgetSummaryResponseDTO {

    private List<Double> humanCostSummary;
    private List<Double> expensesCostSummary;
    private Double totalBudget;
    private List<TemporalRangeDTO> temporalRangeBinSummary;
    private long taskCreationDays;

    public long getTaskCreationDays() {
        return taskCreationDays;
    }

    public void setTaskCreationDays(long taskCreationDays) {
        this.taskCreationDays = taskCreationDays;
    }

    public List<Double> getHumanCostSummary() {
        return humanCostSummary;
    }

    public void setHumanCostSummary(List<Double> humanCostSummary) {
        this.humanCostSummary = humanCostSummary;
    }

    public List<Double> getExpensesCostSummary() {
        return expensesCostSummary;
    }

    public void setExpensesCostSummary(List<Double> expensesCostSummary) {
        this.expensesCostSummary = expensesCostSummary;
    }

    public Double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(Double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public List<TemporalRangeDTO> getTemporalRangeBinSummary() {
        return temporalRangeBinSummary;
    }

    public void setTemporalRangeBinSummary(List<TemporalRangeDTO> temporalRangeBinSummary) {
        this.temporalRangeBinSummary = temporalRangeBinSummary;
    }
}
