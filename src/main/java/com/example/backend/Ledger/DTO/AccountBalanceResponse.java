package com.example.backend.Ledger.DTO;

import com.example.backend.Ledger.Util.LedgerUtils;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceResponse {
    private UUID accountId;
    private Long balance;
    private String balanceFormatted;

    public AccountBalanceResponse(UUID accountId, Long balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.balanceFormatted = formatBalance(balance);
    }

    private String formatBalance(Long amount) {
        return LedgerUtils.formatAmount(amount);  // possible bug ?
    }
}
