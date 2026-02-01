package com.example.backend.Ledger.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class LedgerExceptionHandler {

    @ExceptionHandler(LedgerException.class)
    public ResponseEntity<Map<String, Object>> handleLedgerException(LedgerException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Ledger Error");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now().toString());
        error.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UnbalancedJournalEntryException.class)
    public ResponseEntity<Map<String, Object>> handleUnbalancedJournalEntry(UnbalancedJournalEntryException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Unbalanced Journal Entry");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now().toString());
        error.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DuplicateReferenceIdException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateReferenceId(DuplicateReferenceIdException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Duplicate Reference ID");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now().toString());
        error.put("status", HttpStatus.CONFLICT.value());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
