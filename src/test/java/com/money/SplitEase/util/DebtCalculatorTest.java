package com.money.SplitEase.util;

import com.money.SplitEase.model.Expense;
import com.money.SplitEase.model.Group;
import com.money.SplitEase.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DebtCalculatorTest {

    private User userA;
    private User userB;
    private User userC;
    private Group group;

    @BeforeEach
    void setUp() {
        userA = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        userB = User.builder().id(2L).username("Bob").email("bob@example.com").build();
        userC = User.builder().id(3L).username("Charlie").email("charlie@example.com").build();

        group = Group.builder()
                .id(1L)
                .name("Trip Group")
                .members(Set.of(userA, userB, userC))
                .expenses(new HashSet<>())
                .build();
    }

    @Test
    void testEqualSplitExpense() {
        Expense expense = Expense.builder()
                .id(1L)
                .description("Dinner")
                .amount(BigDecimal.valueOf(900))
                .date(LocalDateTime.now())
                .payer(userA)
                .group(group)
                .build();

        group.getExpenses().add(expense);

        Map<String, Map<String, Double>> debts = DebtCalculator.calculateDebts(group);

        assertNotNull(debts.get("Bob"));
        assertNotNull(debts.get("Charlie"));

        assertEquals(300.0, debts.get("Bob").get("Alice"));
        assertEquals(300.0, debts.get("Charlie").get("Alice"));
        assertNull(debts.get("Alice")); // Alice shouldn't owe anyone
    }

    @Test
    void testMultipleExpensesDifferentPayers() {
        group.getExpenses().addAll(List.of(
                Expense.builder()
                        .id(1L)
                        .description("Dinner")
                        .amount(BigDecimal.valueOf(600))
                        .date(LocalDateTime.now())
                        .payer(userA)
                        .group(group)
                        .build(),
                Expense.builder()
                        .id(2L)
                        .description("Snacks")
                        .amount(BigDecimal.valueOf(300))
                        .date(LocalDateTime.now())
                        .payer(userB)
                        .group(group)
                        .build()
        ));

        Map<String, Map<String, Double>> debts = DebtCalculator.calculateDebts(group);

        assertEquals(300.0, debts.get("Charlie").get("Alice")); // ✅ Correct
    }


    @Test
    void testNoExpenses() {
        Map<String, Map<String, Double>> debts = DebtCalculator.calculateDebts(group);
        assertTrue(debts.isEmpty());
    }
}
