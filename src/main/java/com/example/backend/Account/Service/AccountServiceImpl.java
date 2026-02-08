package com.example.backend.Account.Service;

import com.example.backend.Account.DTO.AccountCreateRequest;
import com.example.backend.Account.DTO.AccountResponse;
import com.example.backend.Account.Model.AccountDB;
import com.example.backend.Account.Repository.AccountRepository;
import com.example.backend.User.Model.UserDB;
import com.example.backend.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public AccountResponse createAccount(AccountCreateRequest request) {
        UserDB user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        AccountDB account = AccountDB.builder()
                .user(user)
                .accountNumber(generateAccountNumber())
                .accountType(request.getAccountType())
                .active(true)
                .createdAt(Instant.now())
                .build();

        AccountDB saved = accountRepository.save(account);

        return map(saved);
    }

    @Override
    public AccountResponse getAccount(UUID accountId) {
        AccountDB account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return map(account);
    }

    @Override
    public List<AccountResponse> getAccountsByUser(UUID userId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .map(this::map)
                .toList();
    }

    private AccountResponse map(AccountDB account) {
        return new AccountResponse(
                account.getId(),
                account.getUser().getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.isActive(),
                account.getCreatedAt()
        );
    }

    private String generateAccountNumber() {
        return "AC-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}
