# 0003. MIT license

- **Status:** Accepted
- **Date:** 2026-10-09
- **Issue:** #2

## Context

Bifrost's users are small game studios that will embed `bifrost-verify` and the engine plugins in their games, which are
usually closed source and commercial. A copyleft license (GPL) would require those games to be released under the same
license, which would block adoption. GPL-3.0 was briefly committed and replaced during review of #2.

## Decision

We will license Bifrost under the MIT License.

## Consequences

- Studios can ship closed-source games that include Bifrost without obligations beyond keeping the copyright notice.
- Others may also build closed-source products on top of Bifrost.
- MIT has no explicit patent grant. If that becomes a concern (for example, for corporate contributors), revisit
Apache-2.0 in a new ADR.
