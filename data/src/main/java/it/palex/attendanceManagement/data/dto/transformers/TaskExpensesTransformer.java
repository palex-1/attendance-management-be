package it.palex.attendanceManagement.data.dto.transformers;

import it.palex.attendanceManagement.data.dto.tasks.TaskExpensesDTO;
import it.palex.attendanceManagement.data.entities.core.TaskExpenses;

import java.util.ArrayList;
import java.util.List;

public class TaskExpensesTransformer {

    public static TaskExpensesDTO mapToDTO(TaskExpenses taskExpense) {
        if(taskExpense==null) {
            return null;
        }
        TaskExpensesDTO park = new TaskExpensesDTO();
        park.setId(taskExpense.getId());
        park.setDay(taskExpense.getDay());
        park.setDescription(taskExpense.getDescription());
        park.setExpenseType(taskExpense.getExpenseType());
        park.setTitle(taskExpense.getTitle());
        park.setAmount(taskExpense.getAmount());
        park.setWorkTask(WorkTaskTransformer.mapToMinimalDTO(taskExpense.getWorkTask()));

        return park;
    }

    public static List<TaskExpensesDTO> mapToDTO(List<TaskExpenses> taskExpenses) {
        if(taskExpenses==null) {
            return null;
        }

        List<TaskExpensesDTO> res = new ArrayList<>(taskExpenses.size());

        for (TaskExpenses taskExpense : taskExpenses) {
            res.add(mapToDTO(taskExpense));
        }

        return res;
    }
}
