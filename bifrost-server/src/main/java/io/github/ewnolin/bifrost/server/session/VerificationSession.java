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

  /**
   * Returns a copy of this session with a different status.
   *
   * @param newStatus the status of the copy
   * @return a new session; this one is unchanged
   */
  public VerificationSession withStatus(SessionStatus newStatus) {
    return new VerificationSession(id, minAge, newStatus, createdAt, expiresAt);
  }

  /**
   * Marks the session expired if it is still pending and its time is up.
   *
   * @param now the current time
   * @return an expired copy if due, otherwise this session
   */
  public VerificationSession expireIfDue(Instant now) {
    if (status == SessionStatus.PENDING && !now.isBefore(expiresAt)) {
      return withStatus(SessionStatus.EXPIRED);
    }
    return this;
  }

  /**
   * Completes a pending session with a verification outcome.
   *
   * @param outcome {@link SessionStatus#VERIFIED} or {@link SessionStatus#FAILED}
   * @param now the current time
   * @return a completed copy of this session
   * @throws IllegalArgumentException if {@code outcome} is not VERIFIED or FAILED
   * @throws SessionStateException if the session has expired or was already completed
   */
  public VerificationSession complete(SessionStatus outcome, Instant now) {
    if (outcome != SessionStatus.VERIFIED && outcome != SessionStatus.FAILED) {
      throw new IllegalArgumentException("outcome must be VERIFIED or FAILED");
    }
    VerificationSession current = expireIfDue(now);
    if (current.status == SessionStatus.EXPIRED) {
      throw new SessionStateException("Session has expired");
    }
    if (current.status != SessionStatus.PENDING) {
      throw new SessionStateException("Session was already completed");
    }
    return withStatus(outcome);
  }
}
