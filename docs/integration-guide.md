# Integration guide

How to add Bifrost age verification to your game.

> Status: draft. Sections are filled in as the features are built.

## How it works

1. **Start a session.** Your game calls `POST /sessions` with the minimum age it needs (e.g. `{"minAge": 15}`). The response contains a `sessionId` and a `Location` header.
2. **The player verifies.** The player proves their age with their EU age verification wallet. *(Until real wallet support lands, use the dev-only mock verifier to approve sessions by hand.)*
3. **Poll the status.** Your game calls `GET /sessions/{sessionId}` every one or two seconds until the status is no longer `PENDING`:
  - `VERIFIED`: the player meets the minimum age
  - `FAILED`: verification did not succeed
  - `EXPIRED`: the player did not finish in time; start a new session
4. **Never personal data.** Bifrost only ever tells your game the result. It never sends a name, birthdate or ID document.

The full API reference, with examples, is in Swagger UI at `/swagger-ui.html` on the server.

## Install

_Coming soon: Godot plugin, Unity package._

## Start a verification

_Coming soon._

## Check a token

_Coming soon: `bifrost-verify` library and the public key endpoint (`/.well-known/jwks.json`)._

## Private and community servers

_Coming soon: verifying tokens offline, without contacting Bifrost._
