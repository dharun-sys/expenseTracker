package com.example.backend.Ledger.Service;

import com.example.backend.Ledger.DTO.PostingEntry;
import com.example.backend.Ledger.DTO.PostingLine;
import com.example.backend.Ledger.Model.JournalEntry;
import com.example.backend.Ledger.Model.JournalLines;
import com.example.backend.Ledger.Model.JournalEntryStatus;
import com.example.backend.Ledger.Model.JournalEntryType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface LedgerService {

    /**
     * Creates a new journal entry with the provided posting lines
     * Validates double-entry accounting rules before saving
     */
    JournalEntry addEntry(PostingEntry postingEntry);

    /**
     * Adds individual posting lines to an existing journal entry
     * Used for building entries incrementally
     */
    void addLines(UUID journalEntryId, List<PostingLine> postingLines);

    /**
     * Posts a draft journal entry (changes status to POSTED)
     * Once posted, entries become immutable
     */
    JournalEntry postEntry(UUID journalEntryId);

    /**
     * Reverses a posted journal entry by creating offsetting entries
     * Original entry remains for audit trail
     */
//    JournalEntry reverseEntry(UUID journalEntryId, String reason);

    /**
     * Calculates current balance for an account based on posted entries
     */
    Long getAccountBalance(UUID accountId);

    /**
     * Calculates account balance as of a specific point in time
     */
    Long getAccountBalanceAsOf(UUID accountId, Instant asOfTime);

    /**
     * Retrieves complete ledger history for an account
     */
    List<JournalLines> getAccountLedgerHistory(UUID accountId);

    /**
     * Finds journal entry by reference ID (for idempotency)
     */
    JournalEntry findByReferenceId(String referenceId);

    /**
     * Retrieves journal entries by status
     */
    List<JournalEntry> findByStatus(JournalEntryStatus status);

    /**
     * Retrieves journal entries by type
     */
    List<JournalEntry> findByType(JournalEntryType type);

    /**
     * Retrieves journal entries within a date range
     */
    List<JournalEntry> findByDateRange(Instant startTime, Instant endTime);

    /**
     * Validates double-entry accounting rules for a journal entry
     */
    boolean validateDoubleEntry(PostingEntry postingEntry);
}
