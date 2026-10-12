package io.github.ewnolin.bifrost.server.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class InMemorySessionRepositoryTest {

  private static final Instant NOW = Instant.parse("2026-01-01T12:00:00Z");

  private final InMemorySessionRepository repository = new InMemorySessionRepository();

  private VerificationSession session(String id, SessionStatus status) {
    return new VerificationSession(id, 15, status, NOW, NOW.plus(Duration.ofMinutes(5)));
  }

  @Test
  void save_returnsTheSavedSession() {
    VerificationSession session = session("abc", SessionStatus.PENDING);

    assertSame(session, repository.save(session));
  }

  @Test
  void findById_afterSave_returnsSession() {
    VerificationSession session = session("abc", SessionStatus.PENDING);
    repository.save(session);

    assertEquals(Optional.of(session), repository.findById("abc"));
  }

  @Test
  void findById_withUnknownId_returnsEmpty() {
    assertTrue(repository.findById("does-not-exist").isEmpty());
  }

  @Test
  void save_withExistingId_replacesSession() {
    repository.save(session("abc", SessionStatus.PENDING));
    VerificationSession verified = session("abc", SessionStatus.VERIFIED);

    repository.save(verified);

    assertEquals(Optional.of(verified), repository.findById("abc"));
  }
}
