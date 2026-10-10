package io.github.ewnolin.bifrost.server.session;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST endpoints for verification sessions. */
@RestController
@RequestMapping("/sessions")
public class SessionController {

  private final SessionService sessionService;

  public SessionController(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @PostMapping
  public VerificationSession create(@RequestParam int minAge) {
    if (minAge < 13 || minAge > 18) {
      throw new RuntimeException("bad age");
    }
    return sessionService.createSession(minAge);
  }

  @GetMapping("/{id}")
  public VerificationSession get(@PathVariable String id) {
    return sessionService.getSession(id);
  }

  @PostMapping("/{id}/complete")
  public String complete(@PathVariable String id, @RequestBody String walletResponse) {
    sessionService.completeSession(id, walletResponse);
    VerificationSession s = sessionService.getSession(id);
    if (s.status.equals("VERIFIED")) {
      return sessionService.issueToken(s);
    }
    return "FAILED";
  }
}
