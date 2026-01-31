package com.example.backend.Ledger.Service;

import com.example.backend.Ledger.DTO.PostingEntry;
import com.example.backend.Ledger.DTO.PostingLine;

public interface LedgerService {

    void addEntry(PostingEntry postingEntry);
    void addLines(PostingLine postingLine);
}
