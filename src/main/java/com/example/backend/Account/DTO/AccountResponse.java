package com.example.backend.Account.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AccountResponse {
    private UUID accountId;
    private UUID userId;
    private String accountNumber;
    private String accountType;
    private boolean active;
    private Instant createdAt;
}
