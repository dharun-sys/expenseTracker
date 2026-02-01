package com.example.backend.Ledger.Repository;

import com.example.backend.Ledger.Model.JournalLines;
import com.example.backend.Ledger.Model.EntrySide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface JournalLinesRepository extends JpaRepository<JournalLines, UUID> {

    List<JournalLines> findByAccountId(UUID accountId);

    List<JournalLines> findByAccountIdAndSide(UUID accountId, EntrySide side);

    @Query("SELECT jl FROM JournalLines jl WHERE jl.accountId = :accountId " +
           "AND jl.journalEntry.occurredAt BETWEEN :startTime AND :endTime")
    List<JournalLines> findByAccountIdAndDateRange(
        @Param("accountId") UUID accountId,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime
    );

    @Query("SELECT COALESCE(SUM(CASE WHEN jl.side = 'DEBIT' THEN jl.amount ELSE -jl.amount END), 0) " +
           "FROM JournalLines jl WHERE jl.accountId = :accountId " +
           "AND jl.journalEntry.status = 'POSTED'")
    Long calculateAccountBalance(@Param("accountId") UUID accountId);

    @Query("SELECT COALESCE(SUM(CASE WHEN jl.side = 'DEBIT' THEN jl.amount ELSE -jl.amount END), 0) " +
           "FROM JournalLines jl WHERE jl.accountId = :accountId " +
           "AND jl.journalEntry.status = 'POSTED' " +
           "AND jl.journalEntry.occurredAt <= :asOfTime")
    Long calculateAccountBalanceAsOf(@Param("accountId") UUID accountId, @Param("asOfTime") Instant asOfTime);

    @Query("SELECT jl FROM JournalLines jl " +
           "JOIN FETCH jl.journalEntry je " +
           "WHERE jl.accountId = :accountId " +
           "AND je.status = 'POSTED' " +
           "ORDER BY je.occurredAt DESC")
    List<JournalLines> findAccountLedgerHistory(@Param("accountId") UUID accountId);
}
