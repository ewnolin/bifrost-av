# bifrost-server

Spring Boot verification service.

## Run locally
```
./gradlew :bifrost-server:bootRun --args="--spring.profiles.active=dev"
```

| URL | What |
|---|---|
| http://localhost:8080/actuator/health | Health check |
| http://localhost:8080/swagger-ui.html | API docs |
| http://localhost:8080/v3/api-docs | OpenAPI spec (JSON) |

## API

### Session lifecycle
A session starts as `PENDING` and ends in exactly one final state:

| Status | Meaning |
|---|---|
| `PENDING` | Waiting for the player to verify |
| `VERIFIED` | The player proved the minimum age |
| `FAILED` | Verification did not succeed |
| `EXPIRED` | Not completed within `bifrost.session.ttl` |

Final states never change. A session can be completed only once; completing an expired or already-completed session returns `409`.

### Create a session
`POST /sessions` starts a verification session for a minimum age.

Request:
```json
{ "minAge": 15 }
```

Response: `201 Created`, with a `Location: /sessions/{sessionId}` header:
```json
{
  "sessionId": "Xk3p9vQ2LmT8rYwB1nZcAa",
  "status": "PENDING",
  "minAge": 15,
  "expiresAt": "2026-10-14T15:05:00Z"
}
```

| Status | When |
|---|---|
| `201` | Session created |
| `400` | `minAge` is missing, not a number, or not an allowed value |

Try it:
```
curl -i -X POST http://localhost:8080/sessions \
  -H "Content-Type: application/json" \
  -d '{"minAge": 15}'
```
PowerShell:
```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/sessions `
  -ContentType "application/json" -Body '{"minAge": 15}'
```

### Get session status
`GET /sessions/{sessionId}` returns the current state of a session. Game clients poll this until the status is no longer `PENDING`.

Response: `200 OK`, with the same body as create.

| Status | When |
|---|---|
| `200` | Session found |
| `404` | No session with this ID (including malformed IDs) |

```
curl http://localhost:8080/sessions/<sessionId>
```

### Errors
All errors are returned as [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) ProblemDetail (`application/problem+json`):
```json
{
  "type": "about:blank",
  "title": "Invalid request",
  "status": 400,
  "detail": "minAge must be one of [13, 15, 16, 18]",
  "instance": "/sessions"
}
```
Validation errors also include an `errors` object naming each invalid field. Error responses never contain stack traces or internal details.

## Configuration

| Property | Default | Meaning |
|---|---|---|
| `bifrost.session.ttl` | `5m` | How long a new session stays open |
| `bifrost.session.allowed-min-ages` | `13, 15, 16, 18` | Minimum ages a game may request |

## Test
```
./gradlew :bifrost-server:test
```
