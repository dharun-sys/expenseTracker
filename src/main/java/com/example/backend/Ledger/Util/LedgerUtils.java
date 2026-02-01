package com.example.backend.Ledger.Util;

import com.example.backend.Ledger.DTO.PostingEntry;
import com.example.backend.Ledger.DTO.PostingLine;
import com.example.backend.Ledger.Model.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class LedgerUtils {

    public static PostingEntry createTransferEntry(UUID fromAccountId, UUID toAccountId,
                                                 Long amount, String referenceId) {
        PostingEntry entry = new PostingEntry();
        entry.setType(JournalEntryType.TRANSFER);
        entry.setReferenceId(referenceId);
        entry.setStatus(JournalEntryStatus.DRAFT);
        entry.setOccurredAt(Instant.now());

        PostingLine debitLine = new PostingLine(fromAccountId, amount, EntrySide.DEBIT);
        PostingLine creditLine = new PostingLine(toAccountId, amount, EntrySide.CREDIT);

        entry.setLines(List.of(debitLine, creditLine));
        return entry;
    }

    public static PostingEntry createDepositEntry(UUID accountId, Long amount, String referenceId) {
        PostingEntry entry = new PostingEntry();
        entry.setType(JournalEntryType.DEPOSIT);
        entry.setReferenceId(referenceId);
        entry.setStatus(JournalEntryStatus.DRAFT);
        entry.setOccurredAt(Instant.now());

        // Assuming a system asset account for deposits
        UUID systemAccountId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        PostingLine debitLine = new PostingLine(systemAccountId, amount, EntrySide.DEBIT);
        PostingLine creditLine = new PostingLine(accountId, amount, EntrySide.CREDIT);

        entry.setLines(List.of(debitLine, creditLine));
        return entry;
    }

    public static PostingEntry createWithdrawalEntry(UUID accountId, Long amount, String referenceId) {
        PostingEntry entry = new PostingEntry();
        entry.setType(JournalEntryType.WITHDRAWAL);
        entry.setReferenceId(referenceId);
        entry.setStatus(JournalEntryStatus.DRAFT);
        entry.setOccurredAt(Instant.now());

        // Assuming a system asset account for withdrawals
        UUID systemAccountId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        PostingLine debitLine = new PostingLine(accountId, amount, EntrySide.DEBIT);
        PostingLine creditLine = new PostingLine(systemAccountId, amount, EntrySide.CREDIT);

        entry.setLines(List.of(debitLine, creditLine));
        return entry;
    }

    public static String generateReferenceId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String formatAmount(Long amountInCents) {
        if (amountInCents == null) return "0.00";
        return String.format("%.2f", amountInCents / 100.0);
    }

    public static Long parseAmount(String formattedAmount) {
        if (formattedAmount == null || formattedAmount.trim().isEmpty()) {
            return 0L;
        }
        return Math.round(Double.parseDouble(formattedAmount) * 100);
    }

    public static boolean isValidAmount(Long amount) {
        return amount != null && amount > 0;
    }
}
