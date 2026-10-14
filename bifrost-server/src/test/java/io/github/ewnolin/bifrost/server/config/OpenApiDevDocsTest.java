package io.github.ewnolin.bifrost.server.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class OpenApiDevDocsTest {

  private final MockMvc mockMvc;

  OpenApiDevDocsTest(@Autowired MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void apiDocs_withDevProfile_containMockVerifier() throws Exception {
    mockMvc
        .perform(get("/v3/api-docs"))
        .andExpect(jsonPath("$.paths['/dev/sessions/{id}/approve'].post").exists())
        .andExpect(jsonPath("$.paths['/dev/sessions/{id}/reject'].post").exists());
  }
}
