package io.github.ewnolin.bifrost.server.session;

import java.util.Optional;
import java.util.function.UnaryOperator;

/** Stores and retrieves verification sessions */
public interface SessionRepository {

  /**
   * Saves a session, replacing any existing session with the same ID.
   *
   * @param session the session to save
   * @return the saves session
   */
  VerificationSession save(VerificationSession session);

  /**
   * Finds a session by its ID
   *
   * @param id the session ID
   * @return the session, or {@link Optional#empty()} if no session has this ID
   */
  Optional<VerificationSession> findById(String id);

  /**
   * Atomically replaces a session with the result of {@code updater}. While the updater runs, no
   * other update of the same session can happen. If the updater throws, the session is unchanged
   * and the exception is rethrown.
   *
   * @param id the session ID
   * @param updater computes the new session from the current one
   * @return the updated session, or {@link Optional#empty()} if no session has this ID
   */
  Optional<VerificationSession> update(String id, UnaryOperator<VerificationSession> updater);
}
