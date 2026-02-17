package com.example.backend.Account.Service;

import com.example.backend.Account.DTO.AccountCreateRequest;
import com.example.backend.Account.DTO.AccountResponse;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountResponse createAccount(AccountCreateRequest request);

    AccountResponse getAccount(UUID accountId);

    List<AccountResponse> getAccountsByUser(UUID userId);
}
