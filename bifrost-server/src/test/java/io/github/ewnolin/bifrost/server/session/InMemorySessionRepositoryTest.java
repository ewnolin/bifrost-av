package io.github.ewnolin.bifrost.server.session;

import static org.junit.jupiter.api.Assertions.*;

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

  @Test
  void update_existingSession_storesResult() {
    repository.save(session("abc", SessionStatus.PENDING));

    repository.update("abc", s -> s.withStatus(SessionStatus.VERIFIED));

    assertEquals(SessionStatus.VERIFIED, repository.findById("abc").orElseThrow().status());
  }

  @Test
  void update_unknownId_returnsEmpty() {
    assertTrue(repository.update("missing", s -> s).isEmpty());
  }

  @Test
  void update_whenUpdaterThrows_leavesSessionUnchanged() {
    VerificationSession original = session("abc", SessionStatus.PENDING);
    repository.save(original);

    assertThrows(
        IllegalStateException.class,
        () ->
            repository.update(
                "abc",
                s -> {
                  throw new IllegalStateException("boom");
                }));

    assertEquals(Optional.of(original), repository.findById("abc"));
  }
}
