package io.github.ewnolin.bifrost.server.error;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.ewnolin.bifrost.testsupport.ThrowingController;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ApiExceptionHandlerTest {

  private final MockMvc mockMvc =
    MockMvcBuilders.standaloneSetup(new ThrowingController())
      .setControllerAdvice(new ApiExceptionHandler())
      .build();

  @Test
  void sessionNotFound_returns404ProblemDetail() throws Exception {
    mockMvc
      .perform(get("/test-errors/not-found"))
      .andExpect(status().isNotFound())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
      .andExpect(jsonPath("$.status").value(404))
      .andExpect(jsonPath("$.title").value("Session not found"))
      .andExpect(jsonPath("$.detail").value("No session exists with the given ID"));
  }

  @Test
  void sessionState_returns409ProblemDetail() throws Exception {
    mockMvc
      .perform(get("/test-errors/conflict"))
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.title").value("Session state conflict"));
  }

  @Test
  void invalidRequest_returns400WithMessage() throws Exception {
    mockMvc
      .perform(get("/test-errors/invalid"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.detail").value("minAge must be one of [13, 15, 16, 18]"));
  }

  @Test
  void validationFailure_returns400NamingTheField() throws Exception {
    mockMvc
      .perform(
        post("/test-errors/validate").contentType(MediaType.APPLICATION_JSON).content("{}"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid request"))
      .andExpect(jsonPath("$.errors.value").exists());
  }

  @Test
  void malformedJson_returns400ProblemDetail() throws Exception {
    mockMvc
      .perform(
        post("/test-errors/validate")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{not json"))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
  }

  @Test
  void unexpectedException_returns500WithoutInternalDetails() throws Exception {
    mockMvc
      .perform(get("/test-errors/unexpected"))
      .andExpect(status().isInternalServerError())
      .andExpect(jsonPath("$.detail").value("An unexpected error occurred."))
      .andExpect(content().string(not(containsString("secret internal detail"))))
      .andExpect(content().string(not(containsString("IllegalStateException"))));
  }
}
