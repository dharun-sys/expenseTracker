package com.example.backend.Ledger.DTO;

import com.example.backend.Ledger.Model.EntrySide;
import com.example.backend.Ledger.Util.LedgerUtils;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerHistoryResponse {
    private UUID transactionId;
    private UUID accountId;
    private String referenceId;
    private String type;
    private Long amount;
    private String amountFormatted;
    private EntrySide side;
    private Instant occurredAt;
    private String description;

    public LedgerHistoryResponse(UUID transactionId, UUID accountId, String referenceId,
                               String type, Long amount, EntrySide side, Instant occurredAt) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.referenceId = referenceId;
        this.type = type;
        this.amount = amount;
        this.amountFormatted = formatAmount(amount);
        this.side = side;
        this.occurredAt = occurredAt;
    }

    private String formatAmount(Long amount) {
        return LedgerUtils.formatAmount(amount); // possible bugg ?
    }
}
