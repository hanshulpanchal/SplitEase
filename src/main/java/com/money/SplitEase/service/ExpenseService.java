package com.money.SplitEase.service;

import com.money.SplitEase.model.Expense;
import com.money.SplitEase.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    public List<Expense> getAllExpenses(Pageable pageable) {
        return expenseRepository.findAll(); // You can add pagination support later
    }

    public Optional<Expense> updateExpense(Long id, Expense updatedExpense) {
        return expenseRepository.findById(id).map(existing -> {
            existing.setDescription(updatedExpense.getDescription());
            existing.setAmount(updatedExpense.getAmount());
            existing.setDate(updatedExpense.getDate());
            existing.setPayer(updatedExpense.getPayer());
            existing.setGroup(updatedExpense.getGroup());
            return expenseRepository.save(existing);
        });
    }

    public void deleteExpense(Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
        }
    }

    public List<Expense> getExpensesByGroupId(Long groupId) {
        return expenseRepository.findByGroup_Id(groupId);
    }

    public List<Expense> getExpensesByPayerId(Long payerId) {
        return expenseRepository.findByPayerId(payerId);
    }

    public List<Expense> getExpensesByGroupIdAndDateRange(Long groupId, LocalDateTime start, LocalDateTime end) {
        return expenseRepository.findByGroup_IdAndDateBetween(groupId, start, end);
    }

    public List<Expense> getExpensesByPayerAndMinAmount(Long payerId, BigDecimal minAmount) {
        return expenseRepository.findByPayerIdAndAmountGreaterThanEqual(payerId, minAmount);
    }

    public Page<Expense> getExpensesByDate(LocalDate date, Pageable pageable) {
        return expenseRepository.findAll(pageable);
    }
}
