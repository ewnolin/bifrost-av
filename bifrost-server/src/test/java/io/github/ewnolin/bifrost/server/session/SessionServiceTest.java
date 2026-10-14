package io.github.ewnolin.bifrost.server.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.ewnolin.bifrost.server.error.InvalidRequestException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SessionServiceTest {

  private static final Instant NOW = Instant.parse("2026-01-01T12:00:00Z");
  private static final Duration TTL = Duration.ofMinutes(5);

  private final InMemorySessionRepository repository = new InMemorySessionRepository();
  private final SessionService service =
      new SessionService(
          repository,
          new SessionIdGenerator(),
          Clock.fixed(NOW, ZoneOffset.UTC),
          new SessionProperties(TTL, Set.of(13, 15, 16, 18)));

  @Test
  void createSession_withAllowedAge_returnsPendingSessionWithTtl() {
    VerificationSession session = service.createSession(15);

    assertEquals(SessionStatus.PENDING, session.status());
    assertEquals(15, session.minAge());
    assertEquals(NOW, session.createdAt());
    assertEquals(NOW.plus(TTL), session.expiresAt());
  }

  @Test
  void createSession_savesSessionInRepository() {
    VerificationSession session = service.createSession(18);

    assertEquals(Optional.of(session), repository.findById(session.id()));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1, 14, 21})
  void createSession_withNotAllowedAge_throwsInvalidRequest(int minAge) {
    InvalidRequestException ex =
        assertThrows(InvalidRequestException.class, () -> service.createSession(minAge));

    assertEquals("minAge must be one of [13, 15, 16, 18]", ex.getMessage());
  }
}
