package com.example.backend.Ledger.Repository;

import com.example.backend.Ledger.Model.JournalLines;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JournalLinesRepository extends JpaRepository<JournalLines, UUID> {

    //TODO: must write a query to derive data for a ledger report

}
