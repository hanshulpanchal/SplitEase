package com.money.SplitEase.controller;

import com.money.SplitEase.model.Expense;
import com.money.SplitEase.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense) {
        // Assuming expense.getGroup() is not null and group has an ID set
        log.info("Creating new expense for group: {}",
                expense.getGroup() != null ? expense.getGroup().getId() : "null");
        Expense created = expenseService.createExpense(expense);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<Expense>> getAllExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) LocalDate date
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Expense> expenses = (date != null) ?
                expenseService.getExpensesByDate(date, pageable) :
                (Page<Expense>) expenseService.getAllExpenses(pageable);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Expense>> getExpensesByGroupId(@PathVariable Long groupId) {
        return ResponseEntity.ok(expenseService.getExpensesByGroupId(groupId));
    }

    @GetMapping("/payer/{payerId}")
    public ResponseEntity<List<Expense>> getExpensesByPayerId(@PathVariable Long payerId) {
        return ResponseEntity.ok(expenseService.getExpensesByPayerId(payerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @Valid @RequestBody Expense expense) {
        log.info("Updating expense with ID: {}", id);
        return expenseService.updateExpense(id, expense)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        log.warn("Deleting expense with ID: {}", id);
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
