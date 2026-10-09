# 0002. Java 21, Spring Boot 4 and Gradle with convention plugins

- **Status:** Accepted
- **Date:** 2026-10-09
- **Issue:** #2, #3, #6

## Context

The verification server must implement OpenID4VP, validate cryptographic proofs and sign tokens. The EU reference
verifier and several mature identity libraries (e.g. walt.id) run on the JVM.
The "team" already knows Java and Spring Boot.

## Decision

- **Language:** Java 21 (LTS), set through a Gradle toolchain.
- **Server framework:** Spring Boot 4.1 (the issue originally said Spring Boot 3; 4.x was the current release when the
skeleton was built).
- **Build tool:** Gradle 9 with the Kotlin DSL.
  - Shared build logic lives in a `buildSrc` convention plugin (`bifrost.java-conventions`), applied by each module
  through `plugins {}`, instead of `subprojects {}` / `allprojects {}`.
  - Repositories are declared once in `settings.gradle.kts` (`FAIL_ON_PROJECT_REPOS`).
  - Dependency and plugin versions live in a version catalog (`gradle/libs.versions.toml`).
- **Formatting:** Spotless with google-java-format, enforced in CI.

## Consequences

- JVM identity libraries can be used directly in `bifrost-server`.
- `bifrost-verify` stays plain Java with no Spring dependency, so it can be embedded anywhere a JVM runs.
- Engine plugins (Godot, Unity, Unreal) are written in their own languages and talk to the server over HTTP; they do not
depend on the JVM.
- The convention-plugin setup is compatible with Gradle's configuration cache and keeps each module's build file short.
