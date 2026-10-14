Closes #

## What changed
<!-- 1–3 bullets. What does this PR add or change? -->
-

## Deferred
<!-- Anything intentionally left for another issue, so reviewers (and Claude) don't flag it. Delete if none. -->
-

## How to test
- [ ] CI is green (formatting, build, tests)
- [ ] New/changed behavior is covered by the tests in this PR
<!-- Only add steps below if a reviewer must try something by hand, e.g. an endpoint call from the README -->

## Review checklist
**Scope**
- [ ] Matches the issue's acceptance criteria, nothing more
- [ ] Naming: branch, commits and title reference the issue

**Code**
- [ ] Follows the Code Conventions (layering, injection, records, no `null` returns)
- [ ] Error cases are tested, not only the happy path
- [ ] No debug code, commented-out code, or TODOs without an issue number

**Privacy & security**
- [ ] No personal data, tokens or secrets in code, logs, responses or tests
- [ ] Inputs and tokens are validated, never trusted blindly

**Docs**
- [ ] Javadoc on public classes and methods
- [ ] README / CHANGELOG / ADR updated if behavior or decisions changed
