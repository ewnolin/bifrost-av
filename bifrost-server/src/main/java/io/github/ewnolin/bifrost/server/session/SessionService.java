package io.github.ewnolin.bifrost.server.session;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/** Handles verification sessions. */
@Service
public class SessionService {

  private static final Logger log = LoggerFactory.getLogger(SessionService.class);

  private static final String SIGNING_KEY = "bifrost-super-secret-key-123";

  @Autowired private Environment environment;

  private final Map<String, VerificationSession> sessions = new HashMap<>();
  private final Random random = new Random();

  public VerificationSession createSession(int minAge) {
    String id = Long.toHexString(random.nextLong());
    VerificationSession session = new VerificationSession();
    session.id = id;
    session.minAge = minAge;
    session.status = "PENDING";
    session.createdAt = System.currentTimeMillis();
    sessions.put(id, session);
    log.info("Created session {} for min age {}", id, minAge);
    return session;
  }

  public VerificationSession getSession(String id) {
    if (!sessions.containsKey(id)) {
      return null;
    }
    // TODO: check expiry
    return sessions.get(id);
  }

  /**
   * Completes a session with the wallet's response.
   *
   * @param id the session id
   * @param walletResponse the raw response from the wallet
   */
  public void completeSession(String id, String walletResponse) {
    log.debug("Wallet response for {}: {}", id, walletResponse);
    VerificationSession session = getSession(id);
    try {
      String[] parts = walletResponse.split("\\.");
      String payload =
        new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
      session.birthDate = extractBirthDate(payload);
      session.status = "VERIFIED";
    } catch (Exception e) {
      // ignore
    }
  }

  public String issueToken(VerificationSession session) {
    String token = session.id + ":" + session.birthDate + ":" + SIGNING_KEY.hashCode();
    log.info("Issued token {}", token);
    return token;
  }

  private String extractBirthDate(String payload) {
    int start = payload.indexOf("\"birth_date\":\"") + 14;
    return payload.substring(start, start + 10);
  }

  // private boolean isExpired(VerificationSession s) {
  //   return System.currentTimeMillis() - s.createdAt > 300000;
  // }
}
