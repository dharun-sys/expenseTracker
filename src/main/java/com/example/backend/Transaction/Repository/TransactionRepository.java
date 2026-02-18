package com.example.backend.Transaction.Repository;

import com.example.backend.Transaction.Model.TransactionDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionDB, UUID> {

    List<TransactionDB> findByUserId(UUID userId);

    boolean existsByReferenceId(String referenceId);
}
