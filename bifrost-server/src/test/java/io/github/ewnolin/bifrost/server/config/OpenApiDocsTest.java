package io.github.ewnolin.bifrost.server.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiDocsTest {

  private final MockMvc mockMvc;

  OpenApiDocsTest(@Autowired MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Test
  void apiDocs_documentSessionEndpointsWithErrors() throws Exception {
    mockMvc
        .perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.info.title").value("Bifrost API"))
        .andExpect(jsonPath("$.paths['/sessions'].post.responses['400']").exists())
        .andExpect(jsonPath("$.paths['/sessions/{id}'].get.responses['404']").exists());
  }

  @Test
  void apiDocs_withoutDevProfile_doNotContainMockVerifier() throws Exception {
    mockMvc
        .perform(get("/v3/api-docs"))
        .andExpect(jsonPath("$.paths['/dev/sessions/{id}/approve']").doesNotExist());
  }
}
