# Bifrost
![CI](https://github.com/ewnolin/bifrost-av/actions/workflows/ci.yml/badge.svg)

Open-source age verification layer that lets small game developers comply with upcoming EU rules. Players prove their age with their EU age verification wallet; the game receives a signed yes/no token and never sees personal data.

## Modules
- `bifrost-server` – verification service (Spring Boot)
- `bifrost-verify` – offline token checker
- `bifrost-godot` – Godot plugin

## Build
Requires JDK 21.

```
./gradlew build
```

## Contributing
Code is formatted with Spotless (google-java-format). Before pushing:

```
./gradlew spotlessApply
```

CI runs `spotlessCheck` and fails on unformatted code.
