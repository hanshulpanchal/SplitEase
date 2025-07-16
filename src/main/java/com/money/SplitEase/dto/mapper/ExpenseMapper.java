package com.money.SplitEase.dto.mapper;

import com.money.SplitEase.dto.ExpenseDTO;
import com.money.SplitEase.model.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    public ExpenseDTO toDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setPayerId(expense.getPayer().getId());
        dto.setPayerName(expense.getPayer().getUsername());
        dto.setGroupId(expense.getGroup().getId());
        dto.setGroupName(expense.getGroup().getName());
        return dto;
    }

    public Expense toEntity(ExpenseDTO dto) {
        Expense expense = new Expense();
        expense.setId(dto.getId());
        expense.setDescription(dto.getDescription());
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        return expense;
    }
}
