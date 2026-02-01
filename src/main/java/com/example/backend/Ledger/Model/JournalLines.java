package com.example.backend.Ledger.Model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(
        name = "journal_lines",
        indexes = {
                @Index(name = "idx_account_id", columnList = "accountId")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "journalEntry") // Avoid circular reference in toString
public class JournalLines {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID accountId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntrySide side;

    // Constructor without ID for creating new entries
    public JournalLines(UUID accountId, JournalEntry journalEntry, Long amount, EntrySide side) {
        this.accountId = accountId;
        this.journalEntry = journalEntry;
        this.amount = amount;
        this.side = side;
    }
}

