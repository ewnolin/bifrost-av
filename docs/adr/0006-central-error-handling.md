# 0006. Central error handling

- **Status:** Accepted
- **Date:** 2026-10-14
- **Issue:** #29

## Context

Every endpoint can fail(unknown sessions, invalid inputs, expired sessions and unexpected bugs). Without a
shared approach, each controller would return errors in its own format, game plugins would need endpoint specific error
handling, and stack traces or internal details could leak to clients.

## Decision

We will handle all errors in one `@RestControllerAdvice` that returns RFC 9457 `ProblemDetail` responses. Domain errors
are typed exceptions mapped to status codes: SessionNotFoundException → 404, InvalidRequestException → 400,
SessionStateException → 409. Unexpected exceptions return a generic 500; details are only logged server-side.

## Consequences
* Clients parse one error format for every endpoint
* Controllers stay free of error-handling code; they just throw
* Internal details cannot leak through error responses.
* New error types require a new exception class and handler method.
