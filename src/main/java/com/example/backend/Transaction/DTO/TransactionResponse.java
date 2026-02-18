package com.example.backend.Transaction.DTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TransactionResponse {

    private UUID transactionId;
    private UUID userId;
    private UUID debitAccountId;
    private UUID creditAccountId;
    private String referenceId;
    private String status;
    private BigDecimal amount;
    private Instant createdAt;

    public TransactionResponse(UUID transactionId,
                               UUID userId,
                               UUID debitAccountId,
                               UUID creditAccountId,
                               String referenceId,
                               String status,
                               BigDecimal amount,
                               Instant createdAt) {

        this.transactionId = transactionId;
        this.userId = userId;
        this.debitAccountId = debitAccountId;
        this.creditAccountId = creditAccountId;
        this.referenceId = referenceId;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getDebitAccountId() {
        return debitAccountId;
    }

    public UUID getCreditAccountId() {
        return creditAccountId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
