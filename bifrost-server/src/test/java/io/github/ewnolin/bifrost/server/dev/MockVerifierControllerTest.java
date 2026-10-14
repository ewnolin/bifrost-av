package io.github.ewnolin.bifrost.server.dev;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.ewnolin.bifrost.server.error.ApiExceptionHandler;
import io.github.ewnolin.bifrost.server.session.SessionNotFoundException;
import io.github.ewnolin.bifrost.server.session.SessionService;
import io.github.ewnolin.bifrost.server.session.SessionStateException;
import io.github.ewnolin.bifrost.server.session.SessionStatus;
import io.github.ewnolin.bifrost.server.session.VerificationSession;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MockVerifierControllerTest {

  private static final Instant NOW = Instant.parse("2026-01-01T12:00:00Z");

  private final SessionService sessionService = mock(SessionService.class);
  private final MockMvc mockMvc =
      MockMvcBuilders.standaloneSetup(new MockVerifierController(sessionService))
          .setControllerAdvice(new ApiExceptionHandler())
          .build();

  private static VerificationSession session(SessionStatus status) {
    return VerificationSession.newPending("abc123", 15, NOW, Duration.ofMinutes(5))
        .withStatus(status);
  }

  @Test
  void approve_pendingSession_returnsVerified() throws Exception {
    when(sessionService.completeSession("abc123", SessionStatus.VERIFIED))
        .thenReturn(session(SessionStatus.VERIFIED));

    mockMvc
        .perform(post("/dev/sessions/abc123/approve"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("VERIFIED"));
  }

  @Test
  void reject_pendingSession_returnsFailed() throws Exception {
    when(sessionService.completeSession("abc123", SessionStatus.FAILED))
        .thenReturn(session(SessionStatus.FAILED));

    mockMvc
        .perform(post("/dev/sessions/abc123/reject"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("FAILED"));
  }

  @Test
  void approve_unknownSession_returns404() throws Exception {
    when(sessionService.completeSession("missing", SessionStatus.VERIFIED))
        .thenThrow(new SessionNotFoundException());

    mockMvc.perform(post("/dev/sessions/missing/approve")).andExpect(status().isNotFound());
  }

  @Test
  void approve_completedOrExpiredSession_returns409() throws Exception {
    when(sessionService.completeSession("abc123", SessionStatus.VERIFIED))
        .thenThrow(new SessionStateException("Session was already completed"));

    mockMvc
        .perform(post("/dev/sessions/abc123/approve"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.detail").value("Session was already completed"));
  }
}
