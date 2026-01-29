package com.example.backend.Ledger.DTO;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class PostingLine {

    private String accountId;

    //

    private Long amount;

    private String side; // "DEBIT" or "CREDIT"

}
