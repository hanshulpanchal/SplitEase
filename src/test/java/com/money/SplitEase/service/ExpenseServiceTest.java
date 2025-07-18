package com.money.SplitEase.service;

import com.money.SplitEase.model.Expense;
import com.money.SplitEase.model.Group;
import com.money.SplitEase.model.User;
import com.money.SplitEase.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense sampleExpense;
    private User sampleUser;
    private Group sampleGroup;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleUser = User.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .password("secret")
                .build();

        sampleGroup = Group.builder()
                .id(1L)
                .name("Trip")
                .build();

        sampleExpense = Expense.builder()
                .id(1L)
                .description("Lunch")
                .amount(BigDecimal.valueOf(500))
                .date(LocalDateTime.now())
                .payer(sampleUser)
                .group(sampleGroup)
                .build();
    }

    @Test
    void testCreateExpense() {
        when(expenseRepository.save(any(Expense.class))).thenReturn(sampleExpense);

        Expense saved = expenseService.createExpense(sampleExpense);
        assertNotNull(saved);
        assertEquals("Lunch", saved.getDescription());
    }

    @Test
    void testGetExpenseByIdFound() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(sampleExpense));

        Optional<Expense> found = expenseService.getExpenseById(1L);
        assertTrue(found.isPresent());
        assertEquals("Lunch", found.get().getDescription());
    }

    @Test
    void testGetExpenseByIdNotFound() {
        when(expenseRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Expense> found = expenseService.getExpenseById(99L);
        assertTrue(found.isEmpty());
    }

    @Test
    void testGetAllExpenses() {
        Pageable pageable = PageRequest.of(0, 10);
        when(expenseRepository.findAll()).thenReturn(List.of(sampleExpense));

        List<Expense> all = expenseService.getAllExpenses(pageable);
        assertEquals(1, all.size());
    }

    @Test
    void testUpdateExpense() {
        Expense updatedExpense = Expense.builder()
                .id(1L)
                .description("Dinner")
                .amount(BigDecimal.valueOf(700))
                .date(LocalDateTime.now())
                .payer(sampleUser)
                .group(sampleGroup)
                .build();

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(sampleExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(updatedExpense);

        Optional<Expense> result = expenseService.updateExpense(1L, updatedExpense);
        assertTrue(result.isPresent());
        assertEquals("Dinner", result.get().getDescription());
    }

    @Test
    void testDeleteExpenseWhenExists() {
        when(expenseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(expenseRepository).deleteById(1L);

        expenseService.deleteExpense(1L);
        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteExpenseWhenNotExists() {
        when(expenseRepository.existsById(2L)).thenReturn(false);

        expenseService.deleteExpense(2L);
        verify(expenseRepository, never()).deleteById(2L);
    }

    @Test
    void testGetExpensesByGroupId() {
        when(expenseRepository.findByGroup_Id(1L)).thenReturn(List.of(sampleExpense));

        List<Expense> result = expenseService.getExpensesByGroupId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetExpensesByPayerId() {
        when(expenseRepository.findByPayerId(1L)).thenReturn(List.of(sampleExpense));

        List<Expense> result = expenseService.getExpensesByPayerId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetExpensesByGroupIdAndDateRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        when(expenseRepository.findByGroup_IdAndDateBetween(1L, start, end)).thenReturn(List.of(sampleExpense));

        List<Expense> result = expenseService.getExpensesByGroupIdAndDateRange(1L, start, end);
        assertEquals(1, result.size());
    }

    @Test
    void testGetExpensesByPayerAndMinAmount() {
        when(expenseRepository.findByPayerIdAndAmountGreaterThanEqual(1L, BigDecimal.valueOf(100)))
                .thenReturn(List.of(sampleExpense));

        List<Expense> result = expenseService.getExpensesByPayerAndMinAmount(1L, BigDecimal.valueOf(100));
        assertEquals(1, result.size());
    }
}
