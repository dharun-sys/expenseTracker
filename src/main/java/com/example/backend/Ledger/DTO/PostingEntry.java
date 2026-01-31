package com.example.backend.Ledger.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;


@RequiredArgsConstructor
@Getter
@Setter
public class PostingEntry {

    private UUID id;
    private String type;
    private String referenceId;
    private String status;
    private Instant occurredAt;

}
