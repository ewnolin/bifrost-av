# 0005. Session design

- **Status:** Proposed
- **Date:** 2026-10-12
- **Issue:** #28

## Context

Why does these session need to exist and what must they never contain?

## Decision

We will create a verification session which uses an in memory storage solution for now, which holds
sessions with unique identifiers and statuses. The identifier is a 128-bit SecureRandom ID, with a fixed
TTL and time from an injected clock.

## Consequences

Sessions are lost on restart which makes the game secure, but this could be a problem when needing to verify
each time you join the game and also this doesn't work on multiple server instances. Both are fine now
but a real deployment needs a database to cover such flaws.
