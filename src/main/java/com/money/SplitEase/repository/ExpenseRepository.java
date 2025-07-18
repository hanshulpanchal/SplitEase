package com.money.SplitEase.repository;


import com.money.SplitEase.model.Expense;
import com.money.SplitEase.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    /**
     * Find all expenses by a specific group.
     */
    List<Expense> findByGroupId(Long groupId);

    /**
     * Find all expenses paid by a specific user.
     */
    List<Expense> findByPayerId(Long payerId);

    /**
     * Find expenses by group within a date range.
     */
    List<Expense> findByGroupIdAndDateBetween(Long groupId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find expenses by payer with amount greater than or equal to the given threshold.
     */
    List<Expense> findByPayerIdAndAmountGreaterThanEqual(Long payerId, BigDecimal amount);

    /**
     * Find a single expense by ID (safe optional return).
     */
    Optional<Expense> findById(Long id);
}
