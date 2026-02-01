package com.example.backend.Ledger.Service.ServiceImpl;

import com.example.backend.Ledger.DTO.PostingEntry;
import com.example.backend.Ledger.DTO.PostingLine;
import com.example.backend.Ledger.Exception.DuplicateReferenceIdException;
import com.example.backend.Ledger.Exception.LedgerException;
import com.example.backend.Ledger.Exception.UnbalancedJournalEntryException;
import com.example.backend.Ledger.Model.*;
import com.example.backend.Ledger.Repository.JournalEntryRepository;
import com.example.backend.Ledger.Repository.JournalLinesRepository;
import com.example.backend.Ledger.Service.LedgerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LedgerServiceImpl implements LedgerService {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalLinesRepository journalLinesRepository;

    @Override
    public JournalEntry addEntry(PostingEntry postingEntry) {
        log.info("Creating new journal entry with reference ID: {}", postingEntry.getReferenceId());

        // Check dup refID
        if (journalEntryRepository.existsByReferenceId(postingEntry.getReferenceId())) {
            throw new DuplicateReferenceIdException(
                "Journal entry with reference ID already exists: " + postingEntry.getReferenceId());
        }

        // Validate double-entry accounting rules
        if (!validateDoubleEntry(postingEntry)) {
            throw new UnbalancedJournalEntryException(
                "Journal entry is not balanced. Debit and credit amounts must be equal.");
        }

        // Create journal entry
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setType(postingEntry.getType());
        journalEntry.setReferenceId(postingEntry.getReferenceId());
        journalEntry.setStatus(postingEntry.getStatus());
        journalEntry.setOccurredAt(postingEntry.getOccurredAt());

        // Save journal entry first to get ID
        JournalEntry savedEntry = journalEntryRepository.save(journalEntry);

        // Create and add journal lines
        for (PostingLine postingLine : postingEntry.getLines()) {
            JournalLines journalLine = new JournalLines(
                postingLine.getAccountId(),
                savedEntry,
                postingLine.getAmount(),
                postingLine.getSide()
            );
            savedEntry.addLine(journalLine);
        }

        JournalEntry result = journalEntryRepository.save(savedEntry);
        log.info("Successfully created journal entry with ID: {}", result.getId());

        return result;
    }

    @Override
    public void addLines(UUID journalEntryId, List<PostingLine> postingLines) {
        JournalEntry journalEntry = journalEntryRepository.findById(journalEntryId)
            .orElseThrow(() -> new LedgerException("Journal entry not found: " + journalEntryId));

        if (journalEntry.getStatus() == JournalEntryStatus.POSTED) {
            throw new LedgerException("Cannot modify posted journal entry: " + journalEntryId);
        }

        for (PostingLine postingLine : postingLines) {
            JournalLines journalLine = new JournalLines(
                postingLine.getAccountId(),
                journalEntry,
                postingLine.getAmount(),
                postingLine.getSide()
            );
            journalEntry.addLine(journalLine);
        }

        journalEntryRepository.save(journalEntry);
    }

    @Override
    public JournalEntry postEntry(UUID journalEntryId) {
        JournalEntry journalEntry = journalEntryRepository.findById(journalEntryId)
            .orElseThrow(() -> new LedgerException("Journal entry not found: " + journalEntryId));

        if (journalEntry.getStatus() == JournalEntryStatus.POSTED) {
            throw new LedgerException("Journal entry is already posted: " + journalEntryId);
        }

        if (!journalEntry.isBalanced()) {
            throw new UnbalancedJournalEntryException(
                "Cannot post unbalanced journal entry: " + journalEntryId);
        }

        journalEntry.setStatus(JournalEntryStatus.POSTED);
        JournalEntry result = journalEntryRepository.save(journalEntry);

        log.info("Posted journal entry with ID: {}", journalEntryId);
        return result;
    }

    @Override
    public JournalEntry reverseEntry(UUID journalEntryId, String reason) {
        JournalEntry originalEntry = journalEntryRepository.findById(journalEntryId)
            .orElseThrow(() -> new LedgerException("Journal entry not found: " + journalEntryId));

        if (originalEntry.getStatus() != JournalEntryStatus.POSTED) {
            throw new LedgerException("Can only reverse posted journal entries: " + journalEntryId);
        }

        // Create reversal entry
        JournalEntry reversalEntry = new JournalEntry();
        reversalEntry.setType(originalEntry.getType());
        reversalEntry.setReferenceId("REV-" + originalEntry.getReferenceId());
        reversalEntry.setStatus(JournalEntryStatus.POSTED);
        reversalEntry.setOccurredAt(Instant.now());

        // Create offsetting lines (flip debit/credit)
        for (JournalLines originalLine : originalEntry.getLines()) {
            EntrySide reversalSide = originalLine.getSide() == EntrySide.DEBIT ?
                EntrySide.CREDIT : EntrySide.DEBIT;

            JournalLines reversalLine = new JournalLines(
                originalLine.getAccountId(),
                reversalEntry,
                originalLine.getAmount(),
                reversalSide
            );
            reversalEntry.addLine(reversalLine);
        }

        // Mark original as reversed
        originalEntry.setStatus(JournalEntryStatus.REVERSED);
        journalEntryRepository.save(originalEntry);

        JournalEntry result = journalEntryRepository.save(reversalEntry);
        log.info("Reversed journal entry {} with new entry {}", journalEntryId, result.getId());

        return result;
    }

    @Override
    public Long getAccountBalance(UUID accountId) {
        Long balance = journalLinesRepository.calculateAccountBalance(accountId);
        return balance != null ? balance : 0L;
    }

    @Override
    public Long getAccountBalanceAsOf(UUID accountId, Instant asOfTime) {
        Long balance = journalLinesRepository.calculateAccountBalanceAsOf(accountId, asOfTime);
        return balance != null ? balance : 0L;
    }

    @Override
    public List<JournalLines> getAccountLedgerHistory(UUID accountId) {
        return journalLinesRepository.findAccountLedgerHistory(accountId);
    }

    @Override
    public JournalEntry findByReferenceId(String referenceId) {
        return journalEntryRepository.findByReferenceId(referenceId)
            .orElseThrow(() -> new LedgerException("Journal entry not found with reference ID: " + referenceId));
    }

    @Override
    public List<JournalEntry> findByStatus(JournalEntryStatus status) {
        return journalEntryRepository.findByStatus(status);
    }

    @Override
    public List<JournalEntry> findByType(JournalEntryType type) {
        return journalEntryRepository.findByType(type);
    }

    @Override
    public List<JournalEntry> findByDateRange(Instant startTime, Instant endTime) {
        return journalEntryRepository.findByOccurredAtBetween(startTime, endTime);
    }

    @Override
    public boolean validateDoubleEntry(PostingEntry postingEntry) {
        if (postingEntry.getLines() == null || postingEntry.getLines().size() < 2) {
            return false;
        }

        long debitSum = postingEntry.getLines().stream()
            .filter(line -> line.getSide() == EntrySide.DEBIT)
            .mapToLong(PostingLine::getAmount)
            .sum();

        long creditSum = postingEntry.getLines().stream()
            .filter(line -> line.getSide() == EntrySide.CREDIT)
            .mapToLong(PostingLine::getAmount)
            .sum();

        return debitSum == creditSum && debitSum > 0;
    }
}
