package com.example.backend.Transaction.Controller;

import com.example.backend.Transaction.DTO.TransactionCreateRequest;
import com.example.backend.Transaction.DTO.TransactionResponse;
import com.example.backend.Transaction.Service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public TransactionResponse create(@Valid @RequestBody TransactionCreateRequest request) {
        return transactionService.createTransaction(request);
    }

    @GetMapping("/{id}")
    public TransactionResponse get(@PathVariable UUID id) {
        return transactionService.getTransaction(id);
    }

    @GetMapping("/user/{userId}")
    public List<TransactionResponse> byUser(@PathVariable UUID userId) {
        return transactionService.getUserTransactions(userId);
    }
}
