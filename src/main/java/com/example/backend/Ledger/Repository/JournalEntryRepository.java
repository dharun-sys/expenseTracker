package com.example.backend.Ledger.Repository;

import com.example.backend.Ledger.Model.JournalEntry;
import com.example.backend.Ledger.Model.JournalEntryStatus;
import com.example.backend.Ledger.Model.JournalEntryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, UUID> {

    Optional<JournalEntry> findByReferenceId(String referenceId);

    List<JournalEntry> findByStatus(JournalEntryStatus status);

    List<JournalEntry> findByType(JournalEntryType type);

    List<JournalEntry> findByOccurredAtBetween(Instant startTime, Instant endTime);

    @Query("SELECT je FROM JournalEntry je WHERE je.status = :status AND je.occurredAt BETWEEN :startTime AND :endTime")
    List<JournalEntry> findByStatusAndOccurredAtBetween(
        @Param("status") JournalEntryStatus status,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime
    );

    boolean existsByReferenceId(String referenceId);
}
