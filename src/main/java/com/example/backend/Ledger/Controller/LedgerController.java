package com.example.backend.Ledger.Controller;

import com.example.backend.Ledger.DTO.PostingEntry;
import com.example.backend.Ledger.DTO.PostingLine;
import com.example.backend.Ledger.Model.*;
import com.example.backend.Ledger.Service.LedgerService;
import com.example.backend.Ledger.Util.LedgerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ledger")
@RequiredArgsConstructor
@Validated
public class LedgerController {

    private final LedgerService ledgerService;

    @PostMapping("/entries")
    public ResponseEntity<JournalEntry> createEntry(@Valid @RequestBody PostingEntry postingEntry) {
        JournalEntry journalEntry = ledgerService.addEntry(postingEntry);
        return ResponseEntity.ok(journalEntry);
    }

    @PostMapping("/entries/{entryId}/lines")
    public ResponseEntity<Void> addLines(
            @PathVariable UUID entryId,
            @Valid @RequestBody List<PostingLine> postingLines) {
        ledgerService.addLines(entryId, postingLines);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/entries/{entryId}/post")
    public ResponseEntity<JournalEntry> postEntry(@PathVariable UUID entryId) {
        JournalEntry journalEntry = ledgerService.postEntry(entryId);
        return ResponseEntity.ok(journalEntry);
    }

    @PostMapping("/entries/{entryId}/reverse")
    public ResponseEntity<JournalEntry> reverseEntry(
            @PathVariable UUID entryId,
            @RequestParam String reason) {
        JournalEntry reversalEntry = ledgerService.reverseEntry(entryId, reason);
        return ResponseEntity.ok(reversalEntry);
    }

    @GetMapping("/entries/{referenceId}")
    public ResponseEntity<JournalEntry> getEntryByReferenceId(@PathVariable String referenceId) {
        JournalEntry journalEntry = ledgerService.findByReferenceId(referenceId);
        return ResponseEntity.ok(journalEntry);
    }

    @GetMapping("/entries") // might return large amount of data - consider pagination
    public ResponseEntity<List<JournalEntry>> getEntries(
            @RequestParam(required = false) JournalEntryStatus status,
            @RequestParam(required = false) JournalEntryType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime) {

        List<JournalEntry> entries;

        if (startTime != null && endTime != null) {
            entries = ledgerService.findByDateRange(startTime, endTime);
        } else if (status != null) {
            entries = ledgerService.findByStatus(status);
        } else if (type != null) {
            entries = ledgerService.findByType(type);
        } else {
            // This would return all entries - consider adding pagination
            entries = ledgerService.findByDateRange(
                Instant.now().minusSeconds(30 * 24 * 60 * 60), // Last 30 days
                Instant.now()
            );
        }

        return ResponseEntity.ok(entries);
    }

    @GetMapping("/accounts/{accountId}/balance")
    public ResponseEntity<Long> getAccountBalance(@PathVariable UUID accountId) {
        Long balance = ledgerService.getAccountBalance(accountId);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/accounts/{accountId}/balance/asof")
    public ResponseEntity<Long> getAccountBalanceAsOf(
            @PathVariable UUID accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant asOfTime) {
        Long balance = ledgerService.getAccountBalanceAsOf(accountId, asOfTime);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/accounts/{accountId}/history")
    public ResponseEntity<List<JournalLines>> getAccountHistory(@PathVariable UUID accountId) {
        List<JournalLines> history = ledgerService.getAccountLedgerHistory(accountId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateEntry(@Valid @RequestBody PostingEntry postingEntry) {
        boolean isValid = ledgerService.validateDoubleEntry(postingEntry);
        return ResponseEntity.ok(isValid);
    }

    // Convenience endpoint for simple transfers
    @PostMapping("/transfer")
    public ResponseEntity<JournalEntry> createTransfer(
            @RequestParam UUID fromAccountId,
            @RequestParam UUID toAccountId,
            @RequestParam Long amount,
            @RequestParam String referenceId) {

        PostingEntry transferEntry = LedgerUtils.createTransferEntry(
            fromAccountId,
            toAccountId,
            amount,
            referenceId
        );

        JournalEntry result = ledgerService.addEntry(transferEntry);

        // Auto-post the transfer
        return ResponseEntity.ok(ledgerService.postEntry(result.getId()));
    }
}
