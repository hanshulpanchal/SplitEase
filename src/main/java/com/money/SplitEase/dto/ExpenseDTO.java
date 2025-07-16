package com.money.SplitEase.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private Long payerId;
    private String payerName;
    private Long groupId;
    private String groupName;
}