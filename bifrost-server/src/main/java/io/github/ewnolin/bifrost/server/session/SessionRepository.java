package io.github.ewnolin.bifrost.server.session;

import java.util.Optional;

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
}
