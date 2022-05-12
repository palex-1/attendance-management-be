package it.palex.attendanceManagement.data.dto.tasks;

import java.util.Date;

public class TaskExpensesDTO {

    private Long id;
    private String title;
    private String description;
    private String expenseType;
    private Date day;
    private Float amount;
    private WorkTaskMinimalDTO workTask;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public WorkTaskMinimalDTO getWorkTask() {
        return workTask;
    }

    public void setWorkTask(WorkTaskMinimalDTO workTask) {
        this.workTask = workTask;
    }

}
