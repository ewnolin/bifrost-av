package io.github.ewnolin.bifrost.testsupport;

import io.github.ewnolin.bifrost.server.error.InvalidRequestException;
import io.github.ewnolin.bifrost.server.session.SessionNotFoundException;
import io.github.ewnolin.bifrost.server.session.SessionStateException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Test-only controller that throws each exception the API handler must map. */
@RestController
@RequestMapping("/test-errors")
public class ThrowingController {

  /** Request body used to trigger Bean Validation errors. */
  public record Body(@NotNull Integer value) {}

  @GetMapping("/not-found")
  public void notFound() {
    throw new SessionNotFoundException();
  }

  @GetMapping("/conflict")
  public void conflict() {
    throw new SessionStateException("Session was already completed");
  }

  @GetMapping("/invalid")
  public void invalid() {
    throw new InvalidRequestException("minAge must be one of [13, 15, 16, 18]");
  }

  @GetMapping("/unexpected")
  public void unexpected() {
    throw new IllegalStateException("secret internal detail");
  }

  @PostMapping("/validate")
  public void validate(@Valid @RequestBody Body body) {}
}
