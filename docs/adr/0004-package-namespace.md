# 0004. Package namespace `io.github.ewnolin.bifrost`

- **Status:** Accepted
- **Date:** 2026-10-09
- **Issue:** #6

## Context

Java packages and Maven group IDs use a reverse domain the publisher controls. The project does not own a domain. Every
GitHub account controls the `<username>.github.io` namespace, and Maven Central verifies `io.github.<username>` through
the GitHub account.

## Decision

We will use `io.github.ewnolin.bifrost` as the base package and group ID, with one sub-package per module
(`...bifrost.server`, `...bifrost.verify`).

## Consequences

- `bifrost-verify` can be published to Maven Central without buying a domain.
- If the project moves to a GitHub organization or gets its own domain, the namespace changes. This is cheap before the
first published release (IDE rename refactoring) and breaking after it, so decide before publishing 1.0.
