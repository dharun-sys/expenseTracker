package com.example.backend.Ledger.Model;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "journal_lines",
        indexes = {
                @Index(name = "idx_account_id", columnList = "accountId")
        }
)
// JournalLines cant exist without a JournalEntry
public class JournalLines {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID accountId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry; // respective mapping (FK)

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntrySide side; // DEBIT or CREDIT
}

