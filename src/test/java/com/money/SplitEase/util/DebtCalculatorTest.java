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

    private DebtCalculator debtCalculator;

    private User userA;
    private User userB;
    private User userC;
    private Group group;

    @BeforeEach
    void setUp() {
        debtCalculator = new DebtCalculator();

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
    void testEqualSplitExpense() throws InterruptedException {
        Expense expense = Expense.builder()
                .id(1L)
                .description("Dinner")
                .amount(BigDecimal.valueOf(900))
                .date(LocalDateTime.now())
                .payer(userA)
                .group(group)
                .build();

        group.getExpenses().add(expense);

        Map<User, Map<User, BigDecimal>> debts = Map.of();
        debtCalculator.calculateDebts(group).wait();

        assertNotNull(debts.get(userB));
        assertNotNull(debts.get(userC));
        assertEquals(0, debts.get(userB).get(userA).compareTo(BigDecimal.valueOf(300)));
        assertEquals(0, debts.get(userC).get(userA).compareTo(BigDecimal.valueOf(300)));
        assertTrue(debts.get(userA) == null || debts.get(userA).isEmpty());
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

        Map<User, Map<User, BigDecimal>> debts = (Map<User, Map<User, BigDecimal>>) debtCalculator.calculateDebts(group);

        assertEquals(0, debts.get(userB).get(userA).compareTo(BigDecimal.valueOf(150)));
        assertEquals(0, debts.get(userC).get(userA).compareTo(BigDecimal.valueOf(150)));
        assertEquals(0, debts.get(userC).get(userB).compareTo(BigDecimal.valueOf(150)));
    }

    @Test
    void testNoExpenses() {
        Map<User, Map<User, BigDecimal>> debts = (Map<User, Map<User, BigDecimal>>) debtCalculator.calculateDebts(group);
        assertTrue(debts.isEmpty());
    }
}
