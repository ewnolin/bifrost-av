package io.github.ewnolin.bifrost.server.session;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** HTTP endpoints for verification sessions. */
@RestController
@RequestMapping("/sessions")
public class SessionController {

  private final SessionService sessionService;

  /**
   * Creates the controller.
   *
   * @param sessionService the session business logic
   */
  public SessionController(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  /**
   * Starts a verification session.
   *
   * @param request the minimum age to verify
   * @return 201 Created with the new session and its location
   */
  @PostMapping
  public ResponseEntity<SessionResponse> create(@Valid @RequestBody CreateSessionRequest request) {
    VerificationSession session = sessionService.createSession(request.minAge());
    return ResponseEntity.created(URI.create("/sessions/" + session.id()))
        .body(SessionResponse.from(session));
  }

  /**
   * Returns the current state of a session.
   *
   * @param id of the session from the URL
   * @return the session
   */
  @GetMapping("/{id}")
  public SessionResponse get(@PathVariable String id) {
    return SessionResponse.from(sessionService.getSession(id));
  }
}
