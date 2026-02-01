package com.example.backend.Ledger.Exception;

public class UnbalancedJournalEntryException extends LedgerException {
    public UnbalancedJournalEntryException(String message) {
        super(message);
    }
}
