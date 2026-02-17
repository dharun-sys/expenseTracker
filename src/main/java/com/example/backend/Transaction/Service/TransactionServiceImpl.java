package com.example.backend.Transaction.Service;

import com.example.backend.Transaction.DTO.TransactionCreateRequest;
import com.example.backend.Transaction.DTO.TransactionResponse;
import com.example.backend.Transaction.Model.TransactionDB;
import com.example.backend.Transaction.Repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionResponse createTransaction(TransactionCreateRequest request) {

        TransactionDB tx = new TransactionDB();
        tx.setUserId(request.getUserId());
        tx.setDebitAccountId(request.getDebitAccountId());
        tx.setCreditAccountId(request.getCreditAccountId());
        tx.setCategoryId(request.getCategoryId());
        tx.setReferenceId(generateReferenceId());
        tx.setStatus("PENDING");
        tx.setAmount(request.getAmount());
        tx.setCreatedAt(Instant.now());

        TransactionDB saved = transactionRepository.save(tx);
        return map(saved);
    }

    @Override
    public TransactionResponse getTransaction(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .map(this::map)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<TransactionResponse> getUserTransactions(UUID userId) {
        return transactionRepository.findByUserId(userId)
                .stream()
                .map(this::map)
                .toList();
    }

    private TransactionResponse map(TransactionDB tx) {
        return new TransactionResponse(
                tx.getTransactionId(),
                tx.getUserId(),
                tx.getDebitAccountId(),
                tx.getCreditAccountId(),
                tx.getReferenceId(),
                tx.getStatus(),
                tx.getAmount(),
                tx.getCreatedAt()
        );
    }

    private String generateReferenceId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}
