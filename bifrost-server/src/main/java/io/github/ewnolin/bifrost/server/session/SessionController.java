package io.github.ewnolin.bifrost.server.session;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** HTTP endpoints for verification sessions. */
@RestController
@RequestMapping("/sessions")
@Tag(name = "Sessions", description = "Start an age verification session and poll its status")
public class SessionController {

  private static final String PROBLEM_JSON = "application/problem+json";

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
  @Operation(
      summary = "Start a verification session",
      description =
          "Creates a PENDING session for the given minimum age. Poll the URL in the Location"
              + " header until the status is no longer PENDING.")
  @ApiResponse(responseCode = "201", description = "Session created")
  @ApiResponse(
      responseCode = "400",
      description = "minAge is missing, not a number, or not an allowed value",
      content =
          @Content(
              mediaType = PROBLEM_JSON,
              schema = @Schema(implementation = ProblemDetail.class)))
  public ResponseEntity<SessionResponse> create(@Valid @RequestBody CreateSessionRequest request) {
    VerificationSession session = sessionService.createSession(request.minAge());
    return ResponseEntity.created(URI.create("/sessions/" + session.id()))
        .body(SessionResponse.from(session));
  }

  /**
   * Returns the current state of a session.
   *
   * @param id the session ID from the URL
   * @return the session
   */
  @GetMapping("/{id}")
  @Operation(
      summary = "Get session status",
      description =
          "Returns the current state of a session. A PENDING session past its expiry is"
              + " reported as EXPIRED.")
  @ApiResponse(responseCode = "200", description = "Session found")
  @ApiResponse(
      responseCode = "404",
      description = "No session with this ID",
      content =
          @Content(
              mediaType = PROBLEM_JSON,
              schema = @Schema(implementation = ProblemDetail.class)))
  public SessionResponse get(
      @Parameter(
              description = "Session ID from the create response",
              example = "Xk3p9vQ2LmT8rYwB1nZcAa")
          @PathVariable
          String id) {
    return SessionResponse.from(sessionService.getSession(id));
  }
}
