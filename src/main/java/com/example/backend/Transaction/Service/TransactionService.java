package com.example.backend.Transaction.Service;

import com.example.backend.Transaction.DTO.TransactionCreateRequest;
import com.example.backend.Transaction.DTO.TransactionResponse;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionResponse createTransaction(TransactionCreateRequest request);

    TransactionResponse getTransaction(UUID transactionId);

    List<TransactionResponse> getUserTransactions(UUID userId);
}
