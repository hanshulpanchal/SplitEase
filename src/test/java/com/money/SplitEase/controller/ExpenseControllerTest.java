package com.money.SplitEase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.SplitEase.model.Expense;
import com.money.SplitEase.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(ExpenseControllerTest.TestConfig.class)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExpenseController expenseController;

    @Mock
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExpense() throws Exception {
        Expense mockExpense = Expense.builder()
                .id(1L)
                .description("Lunch")
                .amount(new BigDecimal("250.00"))
                .date(LocalDateTime.now())
                .build();

        when(expenseService.createExpense(mockExpense)).thenReturn(mockExpense);

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockExpense)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Lunch"))
                .andExpect(jsonPath("$.amount").value(250.00));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ExpenseController expenseController(ExpenseService expenseService) {
            return new ExpenseController(expenseService);
        }
    }
}
