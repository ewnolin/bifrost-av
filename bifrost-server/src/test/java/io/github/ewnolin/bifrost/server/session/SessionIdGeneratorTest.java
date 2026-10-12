package io.github.ewnolin.bifrost.server.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SessionIdGeneratorTest {

  private final SessionIdGenerator generator = new SessionIdGenerator();

  @Test
  void generate_returns22Characters() {
    assertEquals(22, generator.generate().length());
  }

  @Test
  void generate_returnsOnlyUrlSafeCharacters() {
    String id = generator.generate();

    assertTrue(id.matches("[A-Za-z0-9_-]+"), "unexpected characters in " + id);
  }

  @Test
  void generate_calledManyTimes_returnsUniqueIds() {
    Set<String> ids = new HashSet<>();
    for (int i = 0; i < 10_000; i++) {
      ids.add(generator.generate());
    }

    assertEquals(10_000, ids.size());
  }
}
