package com.example.backend.Ledger.DTO;

import com.example.backend.Ledger.Model.EntrySide;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
public class PostingLine {

    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Long amount;

    @NotNull(message = "Entry side is required")
    private EntrySide side;
}
