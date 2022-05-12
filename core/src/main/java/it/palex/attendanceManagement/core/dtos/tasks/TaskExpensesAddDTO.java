package it.palex.attendanceManagement.core.dtos.tasks;


import it.palex.attendanceManagement.library.rest.dtos.DTO;

import java.util.Date;

public class TaskExpensesAddDTO implements DTO {

    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private String expenseType;
    private Date day;
    private Float amount;

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
