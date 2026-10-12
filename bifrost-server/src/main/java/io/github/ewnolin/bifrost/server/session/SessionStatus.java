package io.github.ewnolin.bifrost.server.session;

/** The states a {@link VerificationSession} can be in. */
public enum SessionStatus {
  /** Created and waiting for the player to complete verification. */
  PENDING,
  /** The player proved they meet the minimum age. */
  VERIFIED,
  /** Verification was attempted and did not succeed. */
  FAILED,
  /** The session timed out before verification was completed. */
  EXPIRED
}
