package io.github.ewnolin.bifrost.server.session;

import java.time.Instant;

/**
 * What the API returns about a session. Contains no personal data.
 *
 * @param sessionId the session ID
 * @param status the current status
 * @param minAge the minimum age being verified
 * @param expiresAt when the session stops accepting verification
 */
public record SessionResponse(
    String sessionId, SessionStatus status, int minAge, Instant expiresAt) {

  /**
   * Converts a domain session into an API response.
   *
   * @param session the session to convert
   * @return the response
   */
  public static SessionResponse from(VerificationSession session) {
    return new SessionResponse(
        session.id(), session.status(), session.minAge(), session.expiresAt());
  }
}
