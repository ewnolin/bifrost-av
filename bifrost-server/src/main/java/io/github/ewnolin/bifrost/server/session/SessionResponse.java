package io.github.ewnolin.bifrost.server.session;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Unguessable session ID", example = "Xk3p9vQ2LmT8rYwB1nZcAa")
        String sessionId,
    @Schema(description = "Current state of the session", example = "PENDING") SessionStatus status,
    @Schema(description = "Minimum age being verified, in years", example = "15") int minAge,
    @Schema(
            description = "When the session stops accepting verification (UTC)",
            example = "2026-10-14T15:05:00Z")
        Instant expiresAt) {

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
