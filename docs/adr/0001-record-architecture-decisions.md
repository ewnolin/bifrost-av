# 0001. Record architecture decisions

- **Status:** Accepted
- **Date:** 2026-10-09
- **Issue:** #7

## Context

Bifrost makes technical choices that affect every module and every game that integrates it: token formats, signing
algorithms, licensing, build tooling. Without a record, the reasons behind these choices get lost,
and new members (or reviewers) have to guess.

## Decision

We will record significant decisions as Architecture Decision Records (ADRs) in `docs/adr/`, using the format in
[0000-template.md](0000-template.md).

- Files are numbered sequentially: `NNNN-short-title.md`.
- An ADR is written in the same pull request as the change it describes.
- Accepted ADRs are never edited to change the decision. A new ADR supersedes the old one, and the old one's status is
updated to point to it.

## Consequences

- Decisions and their reasoning are reviewable in pull requests like code.
- Writing an ADR adds a small amount of work to significant changes.
- "Significant" is a judgment call: anything that is hard to reverse, affects integrators, or that someone is likely to
question later.
