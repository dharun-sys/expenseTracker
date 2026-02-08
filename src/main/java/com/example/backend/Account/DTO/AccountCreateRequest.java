package com.example.backend.Account.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AccountCreateRequest {

    @NotNull
    private UUID userId;

    @NotBlank
    private String accountType;
}
