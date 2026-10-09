# Bifrost – instructions for Claude as a reviewer

## Project
Bifrost is a way to make age verification easier for both big and small video game creators, aimed to launch when
and if the EU Kids Act goes through with legislations making it required for online video games. A stack made of two
main modules `bifrost-server` and `bifrost-verify` which are the main logic packages and then specified packages for
Unity, Godot, Unreal, etc. The game must never receive personal data but only a signed yes/no age claim which leaves
the server. `bifrost-verify` must stay plain Java with no Spring dependency. The engine plugins only talk to the
server over HTTP.

## When reviewing a PR, flag

**Privacy (highest priority)**
- Personal data, tokens, wallet responses or proofs written to logs, stored, or returned to the game
- Anything other than the age claim leaving the server

**Token and crypto handling**
- Token signature not verified, or the algorithm taken from the token header (`alg: none`, algorithm confusion)
- Missing checks on `exp`, `iat`, `iss` or `aud`
- Secrets or private keys in code, config, tests or fixtures
- `java.util.Random` used for IDs, nonces or keys (must be `SecureRandom`)
- Session IDs or nonces that are guessable, never expire, or can be reused (replay)
- Hand-written crypto instead of a vetted library

**Code conventions**
- Field `@Autowired`, business logic in controllers
- Swallowed exceptions, broad `catch (Exception e)`, returning `null`
- New Spring or heavy dependencies in `bifrost-verify`
- Actuator endpoints exposed beyond `health`; wildcard CORS

**Tests and docs**
- New or changed logic without tests, especially for error paths
- Public classes or methods without Javadoc
- TODOs without an issue number; commented-out code
- Behaviour changes without matching README / integration guide / CHANGELOG updates

## Do not flag
- Formatting or import order (Spotless and CI handle it)
- Generated files (`gradlew`, Gradle wrapper)
- Missing Javadoc on test classes and test methods
- Features outside the scope of the PR's issue
- Personal style preferences not covered by these rules

## Review style
- Prefix every comment with **blocking:** or **suggestion:**
- Explain *why*, and name the rule it breaks
- Prefer a few high-signal comments over many small ones
