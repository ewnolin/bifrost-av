# 0007. Session lifecycle: lazy expiry and atomic updates

- **Status:** Accepted
- **Date:** 2026-10-14
- **Issue:** #32

## Context

A session moves from `PENDING` to exactly one final state (`VERIFIED`, `FAILED` or `EXPIRED`) and must never change
again. Two problems threaten this:

- **Race condition:** if two requests complete the same session at the same moment, both can read `PENDING`, both pass
the check, and both write. The session is then completed twice, which breaks single-use completion and enables replay.
- **Expiry:** sessions must expire after the configured TTL, but running a timer per session or a background job adds complexity.

## Decision

- We will update sessions atomically with `ConcurrentHashMap.computeIfPresent`. It locks the single session while the
read, check and write run, so concurrent updates to the same session happen one after another, never at the same time.
If the update throws, the session is left unchanged.
- We will expire sessions lazily: whenever a session is read or completed, a `PENDING` session at or past `expiresAt`
becomes `EXPIRED`. No scheduler is used.
- The state rules live in `VerificationSession` (`expireIfDue`, `complete`); `SessionService` only supplies the current
time and calls the atomic update.

## Consequences

- Completing a session twice is impossible, even under concurrent requests (covered by a repeated multithreaded test).
- No background scheduler or timers are needed.
- Expired sessions stay in memory until someone reads them or a future cleanup job removes them.
- Atomicity only holds within one server instance. Running several instances requires shared storage with its own atomic
updates (e.g. a database transaction), as already noted in ADR 0005.
