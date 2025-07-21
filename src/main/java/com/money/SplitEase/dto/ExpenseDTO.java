package com.money.SplitEase.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Data
public class ExpenseDTO {
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private Long payerId;
    private Long groupId;
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
