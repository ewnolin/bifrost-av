package io.github.ewnolin.bifrost.server.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

public class VerificationSessionTest {

  private static final Instant NOW = Instant.parse("2026-01-01T12:00:00Z");
  private static final Duration TTL = Duration.ofMinutes(5);

  @Test
  void newPending_withValidInput_isPendingAndExpiresAfterTtl() {
    VerificationSession session = VerificationSession.newPending("abc", 15, NOW, TTL);

    assertEquals(SessionStatus.PENDING, session.status());
    assertEquals(15, session.minAge());
    assertEquals(NOW, session.createdAt());
    assertEquals(NOW.plus(TTL), session.expiresAt());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1, -18})
  void newPending_withNonPositiveMinAge_throws(int minAge) {
    assertThrows(
        IllegalArgumentException.class,
        () -> VerificationSession.newPending("abc", minAge, NOW, TTL));
  }

  @Test
  void newPending_withZeroTtl_throws() {
    assertThrows(
        IllegalArgumentException.class,
        () -> VerificationSession.newPending("abc", 15, NOW, Duration.ZERO));
  }

  @Test
  void constructor_withExpiryBeforeCreation_throws() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new VerificationSession("abc", 15, SessionStatus.PENDING, NOW, NOW.minusSeconds(1)));
  }

  @Test
  void constructor_withNullId_throws() {
    assertThrows(
        NullPointerException.class,
        () -> new VerificationSession(null, 15, SessionStatus.PENDING, NOW, NOW.plus(TTL)));
  }

  @Test
  void constructor_withNullStatus_throws() {
    assertThrows(
        NullPointerException.class,
        () -> new VerificationSession("abc", 15, null, NOW, NOW.plus(TTL)));
  }

  @Test
  void newPending_withNullTtl_throws() {
    assertThrows(
        NullPointerException.class, () -> VerificationSession.newPending("abc", 15, NOW, null));
  }

  @Test
  void expireIfDue_justBeforeExpiry_staysPending() {
    VerificationSession session = VerificationSession.newPending("abc", 15, NOW, TTL);

    assertEquals(SessionStatus.PENDING, session.expireIfDue(NOW.plus(TTL).minusNanos(1)).status());
  }

  @Test
  void expireIfDue_exactlyAtExpiry_isExpired() {
    VerificationSession session = VerificationSession.newPending("abc", 15, NOW, TTL);

    assertEquals(SessionStatus.EXPIRED, session.expireIfDue(NOW.plus(TTL)).status());
  }

  @Test
  void expireIfDue_onCompletedSession_keepsStatus() {
    VerificationSession verified =
        VerificationSession.newPending("abc", 15, NOW, TTL).withStatus(SessionStatus.VERIFIED);

    assertEquals(SessionStatus.VERIFIED, verified.expireIfDue(NOW.plus(TTL)).status());
  }

  @ParameterizedTest
  @EnumSource(
      value = SessionStatus.class,
      names = {"VERIFIED", "FAILED"})
  void complete_pendingSession_takesOutcome(SessionStatus outcome) {
    VerificationSession session = VerificationSession.newPending("abc", 15, NOW, TTL);

    assertEquals(outcome, session.complete(outcome, NOW).status());
  }

  @Test
  void complete_withPendingAsOutcome_throws() {
    VerificationSession session = VerificationSession.newPending("abc", 15, NOW, TTL);

    assertThrows(
        IllegalArgumentException.class, () -> session.complete(SessionStatus.PENDING, NOW));
  }
}
