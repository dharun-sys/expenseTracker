package com.example.backend.Ledger.Model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "journal_entries",
        indexes = {
                @Index(name = "idx_journal_ref", columnList = "referenceId")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JournalEntryType type;

    @Column(nullable = false, unique = true)
    private String referenceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JournalEntryStatus status;

    @Column(nullable = false, updatable = false)
    private Instant occurredAt;

    @OneToMany(
            mappedBy = "journalEntry",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<JournalLines> lines = new ArrayList<>();

    // Helper methods for double-entry validation
    public void addLine(JournalLines line) {
        lines.add(line);
        line.setJournalEntry(this);
    }

    public void removeLine(JournalLines line) {
        lines.remove(line);
        line.setJournalEntry(null);
    }

    // Validates that the sum of all lines equals zero (double-entry invariant)
    public boolean isBalanced() {
        long debitSum = lines.stream()
                .filter(line -> line.getSide() == EntrySide.DEBIT)
                .mapToLong(JournalLines::getAmount)
                .sum();

        long creditSum = lines.stream()
                .filter(line -> line.getSide() == EntrySide.CREDIT)
                .mapToLong(JournalLines::getAmount)
                .sum();

        return debitSum == creditSum;
    }
}

