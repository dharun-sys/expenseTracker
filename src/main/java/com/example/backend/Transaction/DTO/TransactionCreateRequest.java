package com.example.backend.Transaction.DTO;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class TransactionCreateRequest {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID debitAccountId;

    @NotNull
    private UUID creditAccountId;

    @NotNull
    private Long categoryId;

    @NotNull
    private BigDecimal amount;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getDebitAccountId() {
        return debitAccountId;
    }

    public void setDebitAccountId(UUID debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public UUID getCreditAccountId() {
        return creditAccountId;
    }

    public void setCreditAccountId(UUID creditAccountId) {
        this.creditAccountId = creditAccountId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
