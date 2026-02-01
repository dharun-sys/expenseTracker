package com.example.backend.Ledger.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class LedgerConfig {

    // Ledger configuration constants
    public static final String SYSTEM_ACCOUNT_ID = "00000000-0000-0000-0000-000000000001";
    public static final String FEE_ACCOUNT_ID = "00000000-0000-0000-0000-000000000002";
    public static final String REVENUE_ACCOUNT_ID = "00000000-0000-0000-0000-000000000003";

    // Transaction reference prefixes
    public static final String TRANSFER_PREFIX = "TRF";
    public static final String DEPOSIT_PREFIX = "DEP";
    public static final String WITHDRAWAL_PREFIX = "WDR";
//    public static final String FEE_PREFIX = "FEE";
//    public static final String REFUND_PREFIX = "REF";
//    public static final String ADJUSTMENT_PREFIX = "ADJ";
}
