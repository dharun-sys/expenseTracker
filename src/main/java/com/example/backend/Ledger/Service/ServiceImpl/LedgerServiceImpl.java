package com.example.backend.Ledger.Service.ServiceImpl;

import com.example.backend.Ledger.DTO.PostingEntry;
import com.example.backend.Ledger.DTO.PostingLine;
import com.example.backend.Ledger.Repository.JournalEntryRepository;
import com.example.backend.Ledger.Repository.JournalLinesRepository;
import com.example.backend.Ledger.Service.LedgerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional //By default, doesn't roll back for caught exceptions.
@RequiredArgsConstructor
public class LedgerServiceImpl implements LedgerService {

    private final JournalEntryRepository journalEntryRepository;
    private final JournalLinesRepository journalLinesRepository;

    @Override
    public void addEntry(PostingEntry postingEntry){
        //TODO: logic for adding Entry to journalEntry table


//        addLines(PostingLine postingLine);
    }

    @Override
    public void addLines(PostingLine postingLine){
        //TODO: logic for adding Lines to journalEntry table
    }


}
