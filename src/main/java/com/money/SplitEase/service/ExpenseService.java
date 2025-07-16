package com.money.SplitEase.service;

import com.money.SplitEase.model.Expense;
import com.money.SplitEase.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    /**
     * Save a new expense to the database.
     */
    public Expense createExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    /**
     * Get an expense by its ID.
     */
    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    /**
     * Get all expenses.
     */
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    /**
     * Update an existing expense if it exists.
     */
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

    /**
     * Delete an expense by its ID.
     */
    public void deleteExpense(Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
        }
    }

    /**
     * Get all expenses related to a specific group.
     */
    public List<Expense> getExpensesByGroupId(Long groupId) {
        return expenseRepository.findByGroupId(groupId);
    }

    /**
     * Get all expenses paid by a specific user.
     */
    public List<Expense> getExpensesByPayerId(Long payerId) {
        return expenseRepository.findByPayerId(payerId);
    }

    /**
     * Get all expenses for a group within a date range.
     */
    public List<Expense> getExpensesByGroupIdAndDateRange(Long groupId, LocalDateTime start, LocalDateTime end) {
        return expenseRepository.findByGroupIdAndDateBetween(groupId, start, end);
    }

    /**
     * Get expenses paid by a user above a certain amount.
     */
    public List<Expense> getExpensesByPayerAndMinAmount(Long payerId, BigDecimal minAmount) {
        return expenseRepository.findByPayerIdAndAmountGreaterThanEqual(payerId, minAmount);
    }
}
