package io.github.ewnolin.bifrost.server.dev;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class MockVerifierFlowTest {

  private final MockMvc mockMvc;

  MockVerifierFlowTest(@Autowired MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  private String createSession() throws Exception {
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
    return location.substring(location.lastIndexOf('/') + 1);
  }

  @Test
  void createApproveAndPoll_endsVerified() throws Exception {
    String id = createSession();

    mockMvc.perform(post("/dev/sessions/" + id + "/approve")).andExpect(status().isOk());

    mockMvc
        .perform(get("/sessions/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("VERIFIED"));
  }

  @Test
  void approveTwice_secondCallReturns409() throws Exception {
    String id = createSession();
    mockMvc.perform(post("/dev/sessions/" + id + "/approve")).andExpect(status().isOk());

    mockMvc.perform(post("/dev/sessions/" + id + "/approve")).andExpect(status().isConflict());
  }
}
