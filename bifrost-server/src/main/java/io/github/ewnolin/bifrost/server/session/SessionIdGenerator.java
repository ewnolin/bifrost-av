package io.github.ewnolin.bifrost.server.session;

import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.stereotype.Component;

/** Generates unguessable session IDs: 128 random bits encoded as a 22-character URL-safe string. */
@Component
public class SessionIdGenerator {

  private static final int ID_BYTES = 16;

  private final SecureRandom random = new SecureRandom();
  private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

  /**
   * Generates a new random session ID.
   *
   * @return a 22-character URL-safe ID
   */
  public String generate() {
    byte[] bytes = new byte[ID_BYTES];
    random.nextBytes(bytes);
    return encoder.encodeToString(bytes);
  }
}
