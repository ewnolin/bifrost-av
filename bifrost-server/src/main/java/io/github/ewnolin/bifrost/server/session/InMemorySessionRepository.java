package io.github.ewnolin.bifrost.server.session;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;
import org.springframework.stereotype.Repository;

/**
 * In-memory {@link SessionRepository}; sessions are lost on restart and not shared across
 * instances.
 */
@Repository
public class InMemorySessionRepository implements SessionRepository {

  private final Map<String, VerificationSession> sessions = new ConcurrentHashMap<>();

  @Override
  public VerificationSession save(VerificationSession session) {
    sessions.put(session.id(), session);
    return session;
  }

  @Override
  public Optional<VerificationSession> findById(String id) {
    return Optional.ofNullable(sessions.get(id));
  }

  @Override
  public Optional<VerificationSession> update(
      String id, UnaryOperator<VerificationSession> updater) {
    return Optional.ofNullable(
        sessions.computeIfPresent(id, (key, current) -> updater.apply(current)));
  }
}
