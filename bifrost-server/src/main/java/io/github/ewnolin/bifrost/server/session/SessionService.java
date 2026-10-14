package io.github.ewnolin.bifrost.server.session;

import io.github.ewnolin.bifrost.server.error.InvalidRequestException;
import java.time.Clock;
import java.time.Instant;
import java.util.TreeSet;
import org.springframework.stereotype.Service;

/** Business rules for verification sessions. */
@Service
public class SessionService {

  private final SessionRepository repository;
  private final SessionIdGenerator idGenerator;
  private final Clock clock;
  private final SessionProperties properties;

  /**
   * Creates the service.
   *
   * @param repository where sessions are stored
   * @param idGenerator source of new session IDs
   * @param clock source of the current time
   * @param properties session settings
   */
  public SessionService(
      SessionRepository repository,
      SessionIdGenerator idGenerator,
      Clock clock,
      SessionProperties properties) {
    this.repository = repository;
    this.idGenerator = idGenerator;
    this.clock = clock;
    this.properties = properties;
  }

  /**
   * Starts a new verification session.
   *
   * @param minAge the minimum age the player must prove
   * @return the new, pending session
   * @throws InvalidRequestException if {@code minAge} is not an allowed value
   */
  public VerificationSession createSession(int minAge) {
    if (!properties.allowedMinAges().contains(minAge)) {
      throw new InvalidRequestException(
          "minAge must be one of " + new TreeSet<>(properties.allowedMinAges()));
    }
    VerificationSession session =
        VerificationSession.newPending(
            idGenerator.generate(), minAge, Instant.now(clock), properties.ttl());
    return repository.save(session);
  }

  /**
   * Looks up a session by ID, marking it expired if its time is up.
   *
   * @param id the session ID
   * @return the session with its current status
   * @throws SessionNotFoundException if no session has this ID
   */
  public VerificationSession getSession(String id) {
    Instant now = Instant.now(clock);
    return repository
        .update(id, session -> session.expireIfDue(now))
        .orElseThrow(SessionNotFoundException::new);
  }

  /**
   * Completes a session with a verification outcome. Each session can be completed only once.
   *
   * @param id the session ID
   * @param outcome {@link SessionStatus#VERIFIED} or {@link SessionStatus#FAILED}
   * @return the completed session
   * @throws SessionNotFoundException if no session has this ID
   * @throws SessionStateException if the session has expired or was already completed
   */
  public VerificationSession completeSession(String id, SessionStatus outcome) {
    Instant now = Instant.now(clock);
    return repository
        .update(id, session -> session.complete(outcome, now))
        .orElseThrow(SessionNotFoundException::new);
  }
}
