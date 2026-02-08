package com.example.backend.Account.Repository;

import com.example.backend.Account.Model.AccountDB;
import com.example.backend.Account.Model.AccountDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountDB, UUID> {

    List<AccountDB> findByUserId(UUID userId);

    Optional<AccountDB> findByAccountNumber(String accountNumber);
}
