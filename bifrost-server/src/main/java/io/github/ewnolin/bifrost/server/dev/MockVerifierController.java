package io.github.ewnolin.bifrost.server.dev;

import io.github.ewnolin.bifrost.server.session.SessionResponse;
import io.github.ewnolin.bifrost.server.session.SessionService;
import io.github.ewnolin.bifrost.server.session.SessionStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ProblemDetail;
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
@Tag(
    name = "Development (mock verifier)",
    description =
        "DEV PROFILE ONLY. Fakes verification by hand until real EU wallet verification exists."
            + " These endpoints do not exist in production.")
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
  @Operation(
      summary = "Approve a session (dev only)",
      description = "Marks a PENDING session as VERIFIED.")
  @ApiResponse(responseCode = "200", description = "Session is now VERIFIED")
  @ApiResponse(
      responseCode = "404",
      description = "No session with this ID",
      content =
          @Content(
              mediaType = "application/problem+json",
              schema = @Schema(implementation = ProblemDetail.class)))
  @ApiResponse(
      responseCode = "409",
      description = "Session has expired or was already completed",
      content =
          @Content(
              mediaType = "application/problem+json",
              schema = @Schema(implementation = ProblemDetail.class)))
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
  @Operation(
      summary = "Reject a session (dev only)",
      description = "Marks a PENDING session as FAILED.")
  @ApiResponse(responseCode = "200", description = "Session is now FAILED")
  @ApiResponse(
      responseCode = "404",
      description = "No session with this ID",
      content =
          @Content(
              mediaType = "application/problem+json",
              schema = @Schema(implementation = ProblemDetail.class)))
  @ApiResponse(
      responseCode = "409",
      description = "Session has expired or was already completed",
      content =
          @Content(
              mediaType = "application/problem+json",
              schema = @Schema(implementation = ProblemDetail.class)))
  public SessionResponse reject(@PathVariable String id) {
    return SessionResponse.from(sessionService.completeSession(id, SessionStatus.FAILED));
  }
}
