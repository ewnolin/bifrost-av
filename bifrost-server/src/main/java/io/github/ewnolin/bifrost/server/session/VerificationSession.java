package io.github.ewnolin.bifrost.server.session;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * A single age verification attempt for one player.
 *
 * <p>A session only records whether verification succeeded. It never holds personal data such as a
 * name or birthdate.
 *
 * @param id unguessable session identifier
 * @param minAge minimum age the player must prove, in years
 * @param status current state of the session
 * @param createdAt when the session was created
 * @param expiresAt when the session stops accepting verification; always after {@code createdAt}
 */
public record VerificationSession(
    String id, int minAge, SessionStatus status, Instant createdAt, Instant expiresAt) {

  /** Validates all components so that an invalid session never gets created */
  public VerificationSession {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(createdAt, "createdAt");
    Objects.requireNonNull(expiresAt, "expiresAt");
    if (minAge <= 0) {
      throw new IllegalArgumentException("minAge must be positive");
    }
    if (!expiresAt.isAfter(createdAt)) {
      throw new IllegalArgumentException("expiresAt must be after createdAt");
    }
  }

  /**
   * Creates a new session that is waiting for a verification
   *
   * @param id unguessable session identifier
   * @param minAge minimum age the player must prove, in years
   * @param now the current time, used as the creation time
   * @param ttl how long the session stays open
   * @return a new session with status {@link SessionStatus#PENDING}
   * @throws IllegalArgumentException if {@code minAge} is not positive or {@code ttl} is not
   *     positive
   */
  public static VerificationSession newPending(String id, int minAge, Instant now, Duration ttl) {
    Objects.requireNonNull(now, "now");
    Objects.requireNonNull(ttl, "ttl");
    return new VerificationSession(id, minAge, SessionStatus.PENDING, now, now.plus(ttl));
  }
}
