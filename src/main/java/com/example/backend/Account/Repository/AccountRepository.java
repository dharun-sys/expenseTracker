package com.example.backend.Account.Repository;

import com.example.backend.Account.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
