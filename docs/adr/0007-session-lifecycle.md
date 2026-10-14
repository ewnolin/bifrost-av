# 0007. Session lifecycle

- **Status:** Accepted
- **Date:** 2026-10-14
- **Issue:** #32

## Context

<!-- What problem or question are we facing? What forces are at play (technical, legal, time, team)? -->
There is a risk of two request arriving at the same moment can both read `PENDING` relating to a check-then-act
race which means one session gets completed twice.

## Decision

<!-- What did we decide? State it in one or two sentences, in active voice: "We will …". -->
The fix is to make the read, check and write in one atomic step. We will do this by using lazy expiry and
`computeIfPresent` function which functions as a keymap which detects if the key exists and is a non-null it fires
the operations simultaneously.

## Consequences

<!-- What becomes easier or harder because of this decision? Include trade-offs and follow-up work. -->
No background scheduler is needed and expired sessions stay in memory until a future cleanup is issued.
