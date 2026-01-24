
✅ IDEAL P2P TRANSFER — HAPPY PATH (END-TO-END)

Assume:

Sender A (accountId = A1)

Receiver B (accountId = B1)

Amount = ₹100

transactionId = tx-123

STEP 0 — Request enters system

Component: TransactionController

Input:

{
  "fromAccount": "A1",
  "toAccount": "B1",
  "amount": 100
}


Controller does nothing except forward.

STEP 1 — Transaction intent is created

Component: TransactionService

Actions:

Generate transactionId

Persist Transaction:

id = tx-123
type = P2P
status = INIT
from = A1
to = B1
amount = 100


Why?

This is your source intent

Everything else hangs off this

STEP 2 — Validate business rules

Component: TransactionService

Checks:

A1 ≠ B1

Amount > 0

Sender balance ≥ 100

Sender & receiver ACTIVE

❗ Balance check is from account table, NOT ledger.

If fail → mark transaction FAILED → exit.

STEP 3 — Begin DB transaction (CRITICAL)

Component: Spring @Transactional

From here:

Either everything commits

Or everything rolls back

No partial state allowed.

STEP 4 — Write to Ledger (IMMUTABLE FACT)

Component: LedgerService

Action:

Write two rows:

account	type	amount	txId
A1	DEBIT	100	tx-123
B1	CREDIT	100	tx-123

Rules enforced:

Double entry

Same transactionId

Idempotency check

Ledger does nothing else.

STEP 5 — Update Account Balances (PROJECTION)

Component: AccountService

Actions:

A1.balance -= 100

B1.balance += 100

No ledger reads.
No recalculation.
Just apply intent.

STEP 6 — Mark Transaction SUCCESS

Component: TransactionService

Update:

status = SUCCESS

STEP 7 — Commit DB Transaction

Everything persists atomically.

Client gets 200 OK.
