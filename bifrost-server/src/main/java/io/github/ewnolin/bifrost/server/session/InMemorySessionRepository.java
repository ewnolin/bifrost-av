package io.github.ewnolin.bifrost.server.session;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

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
}
