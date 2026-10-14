# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Gradle multi-module build with `bifrost-server` and `bifrost-verify` (#2)
- Spotless formatting with google-java-format via a `buildSrc` convention plugin (#3)
- GitHub Actions CI: formatting check, build, tests and coverage reports (#4)
- Issue and pull request templates, protected `main` branch (#5)
- Spring Boot server skeleton with health endpoint and OpenAPI docs (#6)
- Documentation structure: ADRs, integration guide, changelog (#7)
---
- `POST /sessions` endpoint to start a verification session; allowed ages and session TTL are configurable (#30)
