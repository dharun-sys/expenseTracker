package com.example.backend.Ledger.DTO;

import com.example.backend.Ledger.DTO.PostingLine;
import com.example.backend.Ledger.Model.JournalEntryType;
import com.example.backend.Ledger.Model.JournalEntryStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;

import java.time.Instant;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@RequiredArgsConstructor
@AllArgsConstructor
//@NoArgsConstructor
@Getter
@Setter
public class PostingEntry {

    private UUID id;

    @NotNull(message = "Type is required")
    private JournalEntryType type;

    @NotBlank(message = "Reference ID is required")
    private String referenceId;

    @NotNull(message = "Status is required")
    private JournalEntryStatus status = JournalEntryStatus.DRAFT;

    @NotNull(message = "Occurred at time is required")
    private Instant occurredAt;

    @Valid
    @NotNull(message = "Posting lines are required")
    private List<PostingLine> lines = new ArrayList<>();
}
