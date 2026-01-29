package com.example.backend.Ledger.Model;


import jakarta.persistence.*;

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
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, unique = true)
    private String referenceId;

    @Column(nullable = false)
    private String status; // DRAFT, POSTED, REVERSED

    @Column(nullable = false, updatable = false)
    private Instant occurredAt;

    @OneToMany(
            mappedBy = "journalEntry",
            cascade = CascadeType.ALL,
            orphanRemoval = true // rollback child entries if parent is deleted
    )
    private List<JournalLines> lines = new ArrayList<>();
}

