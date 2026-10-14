package io.github.ewnolin.bifrost.server.session;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.ewnolin.bifrost.server.error.ApiExceptionHandler;
import io.github.ewnolin.bifrost.server.error.InvalidRequestException;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class SessionControllerTest {

  private static final Instant NOW = Instant.parse("2026-01-01T12:00:00Z");

  private final SessionService sessionService = mock(SessionService.class);
  private final MockMvc mockMvc =
      MockMvcBuilders.standaloneSetup(new SessionController(sessionService))
          .setControllerAdvice(new ApiExceptionHandler())
          .build();

  private static VerificationSession session(String id, int minAge) {
    return VerificationSession.newPending(id, minAge, NOW, Duration.ofMinutes(5));
  }

  @Test
  void create_withValidBody_returns201WithLocationAndBody() throws Exception {
    when(sessionService.createSession(15)).thenReturn(session("abc123", 15));

    mockMvc
        .perform(
            post("/sessions").contentType(MediaType.APPLICATION_JSON).content("{\"minAge\": 15}"))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/sessions/abc123"))
        .andExpect(jsonPath("$.sessionId").value("abc123"))
        .andExpect(jsonPath("$.status").value("PENDING"))
        .andExpect(jsonPath("$.minAge").value(15))
        .andExpect(jsonPath("$.expiresAt").exists())
        .andExpect(jsonPath("$.*", hasSize(4)));
  }

  @Test
  void create_withMissingMinAge_returns400NamingTheField() throws Exception {
    mockMvc
        .perform(post("/sessions").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.minAge").exists());

    verifyNoInteractions(sessionService);
  }

  @Test
  void create_withNonNumericMinAge_returns400() throws Exception {
    mockMvc
        .perform(
            post("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"minAge\": \"abc\"}"))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(sessionService);
  }

  @Test
  void create_whenServiceRejectsAge_returns400WithMessage() throws Exception {
    when(sessionService.createSession(21))
        .thenThrow(new InvalidRequestException("minAge must be one of [13, 15, 16, 18]"));

    mockMvc
        .perform(
            post("/sessions").contentType(MediaType.APPLICATION_JSON).content("{\"minAge\": 21}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value("minAge must be one of [13, 15, 16, 18]"));
  }
}
