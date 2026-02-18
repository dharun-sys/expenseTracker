package com.example.backend.Account.Controller;

import com.example.backend.Account.DTO.AccountCreateRequest;
import com.example.backend.Account.DTO.AccountResponse;
import com.example.backend.Account.Service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountResponse create(@Valid @RequestBody AccountCreateRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{accountId}")
    public AccountResponse get(@PathVariable UUID accountId) {
        return accountService.getAccount(accountId);
    }

    @GetMapping("/user/{userId}")
    public List<AccountResponse> byUser(@PathVariable UUID userId) {
        return accountService.getAccountsByUser(userId);
    }
}
