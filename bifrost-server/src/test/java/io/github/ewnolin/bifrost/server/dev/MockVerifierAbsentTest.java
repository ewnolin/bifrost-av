package io.github.ewnolin.bifrost.server.dev;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MockVerifierAbsentTest {

  private final ApplicationContext context;
  private final MockMvc mockMvc;

  MockVerifierAbsentTest(@Autowired ApplicationContext context, @Autowired MockMvc mockMvc) {
    this.context = context;
    this.mockMvc = mockMvc;
  }

  @Test
  void withoutDevProfile_mockVerifierBeanDoesNotExist() {
    assertTrue(context.getBeansOfType(MockVerifierController.class).isEmpty());
  }

  @Test
  void withoutDevProfile_approveEndpointIsNotAvailable() throws Exception {
    mockMvc.perform(post("/dev/sessions/anything/approve")).andExpect(status().is4xxClientError());
  }
}
