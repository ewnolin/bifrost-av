package io.github.ewnolin.bifrost.server.dev;

import io.github.ewnolin.bifrost.server.session.SessionResponse;
import io.github.ewnolin.bifrost.server.session.SessionService;
import io.github.ewnolin.bifrost.server.session.SessionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fake verifier for local development: approves or rejects a session by hand, standing in for real
 * EU wallet verification. Only exists when the {@code dev} profile is active.
 */
@Profile("dev")
@RestController
@RequestMapping("/dev/sessions")
public class MockVerifierController {

  private static final Logger log = LoggerFactory.getLogger(MockVerifierController.class);

  private final SessionService sessionService;

  /**
   * Creates the controller and warns that verification is being faked.
   *
   * @param sessionService the session business logic
   */
  public MockVerifierController(SessionService sessionService) {
    this.sessionService = sessionService;
    log.warn(
        "Mock verifier is ACTIVE: any session can be approved by hand. Never use in production.");
  }

  /**
   * Marks a session as verified, as if the player had proved their age.
   *
   * @param id the session ID
   * @return the verified session
   */
  @PostMapping("/{id}/approve")
  public SessionResponse approve(@PathVariable String id) {
    return SessionResponse.from(sessionService.completeSession(id, SessionStatus.VERIFIED));
  }

  /**
   * Marks a session as failed, as if verification had not succeeded.
   *
   * @param id the session ID
   * @return the failed session
   */
  @PostMapping("/{id}/reject")
  public SessionResponse reject(@PathVariable String id) {
    return SessionResponse.from(sessionService.completeSession(id, SessionStatus.FAILED));
  }
}
