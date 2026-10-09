# Integration guide

How to add Bifrost age verification to your game.

> Status: draft. Sections are filled in as the features are built.

## How it works

1. Your game asks the Bifrost server to start a verification session.
2. The player scans a QR code (or opens a link) with their EU age verification or digital identity wallet.
3. The wallet sends a zero-knowledge proof of age to Bifrost. No name, birthdate or ID reaches you.
4. Your game receives a short-lived signed token such as `age_over_15: true`.
5. Your game, or your own server, checks the token's signature with Bifrost's public key.

## Install

_Coming soon: Godot plugin, Unity package._

## Start a verification

_Coming soon._

## Check a token

_Coming soon: `bifrost-verify` library and the public key endpoint (`/.well-known/jwks.json`)._

## Private and community servers

_Coming soon: verifying tokens offline, without contacting Bifrost._
