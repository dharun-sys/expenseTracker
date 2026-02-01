# Ledger Package Documentation

## Overview

The Ledger package implements a double-entry accounting system for tracking financial transactions in the expense tracker backend. It ensures financial integrity through immutable ledger entries and follows established accounting principles.

## Architecture

### Core Components

1. **Models**
   - `JournalEntry`: Represents a complete financial transaction
   - `JournalLines`: Individual debit/credit lines within a journal entry
   - `EntrySide`: Enum for DEBIT/CREDIT sides
   - `JournalEntryType`: Types of transactions (TRANSFER, PAYMENT, etc.)
   - `JournalEntryStatus`: Status of entries (DRAFT, POSTED, REVERSED)

2. **DTOs**
   - `PostingEntry`: Request DTO for creating journal entries
   - `PostingLine`: Individual line items in a posting entry
   - `AccountBalanceResponse`: Response DTO for balance queries
   - `LedgerHistoryResponse`: Response DTO for transaction history

3. **Services**
   - `LedgerService`: Interface defining ledger operations
   - `LedgerServiceImpl`: Implementation with business logic

4. **Repositories**
   - `JournalEntryRepository`: Data access for journal entries
   - `JournalLinesRepository`: Data access for journal lines with balance calculations

5. **Controllers**
   - `LedgerController`: REST API endpoints for ledger operations

## Key Principles

### Double-Entry Accounting
- Every transaction must have at least 2 lines
- Total debits must equal total credits for each transaction
- This ensures the accounting equation: Assets = Liabilities + Equity

### Immutability
- Once posted, journal entries cannot be modified
- Corrections are made through reversal entries
- Maintains complete audit trail

### Idempotency
- Duplicate reference IDs are prevented
- Safe to retry failed transactions

## API Endpoints

### Journal Entry Operations

#### Create Journal Entry
```http
POST /api/ledger/entries
Content-Type: application/json

{
  "type": "TRANSFER",
  "referenceId": "TRF-123456",
  "status": "DRAFT",
  "occurredAt": "2026-02-01T10:30:00Z",
  "lines": [
    {
      "accountId": "uuid-account-a",
      "amount": 10000,
      "side": "DEBIT"
    },
    {
      "accountId": "uuid-account-b", 
      "amount": 10000,
      "side": "CREDIT"
    }
  ]
}
```

#### Post Entry (Make Immutable)
```http
PUT /api/ledger/entries/{entryId}/post
```

#### Reverse Entry
```http
POST /api/ledger/entries/{entryId}/reverse?reason=Correction
```

### Account Operations

#### Get Account Balance
```http
GET /api/ledger/accounts/{accountId}/balance
```

#### Get Account Balance As Of Date
```http
GET /api/ledger/accounts/{accountId}/balance/asof?asOfTime=2026-01-01T00:00:00Z
```

#### Get Account History
```http
GET /api/ledger/accounts/{accountId}/history
```

### Convenience Operations

#### Simple Transfer
```http
POST /api/ledger/transfer
?fromAccountId={uuid}
&toAccountId={uuid}
&amount=10000
&referenceId=TRF-SIMPLE-123
```

## Usage Examples

### Creating a P2P Transfer

```java
@Autowired
private LedgerService ledgerService;

// Create transfer entry
PostingEntry transfer = LedgerUtils.createTransferEntry(
    fromAccountId, 
    toAccountId, 
    10000L, // $100.00 in cents
    "TRF-" + UUID.randomUUID().toString().substring(0, 8)
);

// Add to ledger as draft
JournalEntry entry = ledgerService.addEntry(transfer);

// Post to make immutable
ledgerService.postEntry(entry.getId());
```

### Calculating Account Balance

```java
// Current balance
Long currentBalance = ledgerService.getAccountBalance(accountId);

// Balance as of specific date
Long historicalBalance = ledgerService.getAccountBalanceAsOf(
    accountId, 
    Instant.parse("2026-01-01T00:00:00Z")
);
```

### Account Statement

```java
List<JournalLines> history = ledgerService.getAccountLedgerHistory(accountId);
for (JournalLines line : history) {
    System.out.printf("Date: %s, %s: %s, Amount: %s%n",
        line.getJournalEntry().getOccurredAt(),
        line.getSide(),
        line.getJournalEntry().getReferenceId(),
        LedgerUtils.formatAmount(line.getAmount())
    );
}
```

## Error Handling

### Common Exceptions
- `UnbalancedJournalEntryException`: Debits don't equal credits
- `DuplicateReferenceIdException`: Reference ID already exists
- `LedgerException`: General ledger operation errors

### Validation Rules
1. Journal entries must have at least 2 lines (ensures doubtle-entry)
2. Total debits must equal total credits
3. All amounts must be positive
4. Account IDs must be valid UUIDs
5. Reference IDs must be unique

## Database Schema

### journal_entries Table
```sql
CREATE TABLE journal_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type VARCHAR(50) NOT NULL,
    reference_id VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_journal_ref ON journal_entries(reference_id);
```

### journal_lines Table
```sql
CREATE TABLE journal_lines (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    journal_entry_id UUID NOT NULL REFERENCES journal_entries(id),
    amount BIGINT NOT NULL,
    side VARCHAR(10) NOT NULL CHECK (side IN ('DEBIT', 'CREDIT'))
);

CREATE INDEX idx_account_id ON journal_lines(account_id);
```

## Configuration

### Application Properties
```yaml
# Database configuration for PostgreSQL
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/expense_tracker
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:password}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

