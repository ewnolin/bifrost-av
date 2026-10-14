package io.github.ewnolin.bifrost.server.session;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SessionApiIntegrationTest {

  private final MockMvc mockMvc;

  SessionApiIntegrationTest(@Autowired MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void createSession_endToEnd_returnsPendingSession() throws Exception {
    mockMvc
        .perform(
            post("/sessions").contentType(MediaType.APPLICATION_JSON).content("{\"minAge\": 18}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("PENDING"))
        .andExpect(jsonPath("$.minAge").value(18));
  }

  @Test
  void createSession_withNotAllowedAge_returns400FromConfiguredRules() throws Exception {
    mockMvc
        .perform(
            post("/sessions").contentType(MediaType.APPLICATION_JSON).content("{\"minAge\": 21}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getSession_afterCreate_returnsSameSession() throws Exception {
    String location =
        mockMvc
            .perform(
                post("/sessions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"minAge\": 15}"))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    mockMvc
        .perform(get(location))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("PENDING"))
        .andExpect(jsonPath("$.minAge").value(15));
  }

  @ParameterizedTest
  @ValueSource(strings = {"unknown", "x", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
  void getSession_withUnknownOrMalformedId_returns404(String id) throws Exception {
    mockMvc
        .perform(get("/sessions/" + id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.title").value("Session not found"));
  }
}
