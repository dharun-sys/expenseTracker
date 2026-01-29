package com.example.backend.Ledger.Repository;

import com.example.backend.Ledger.Model.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, UUID> {
}
