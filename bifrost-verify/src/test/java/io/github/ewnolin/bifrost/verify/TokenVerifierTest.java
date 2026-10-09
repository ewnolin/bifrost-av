package io.github.ewnolin.bifrost.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TokenVerifierTest {

  @Test
  void name_returnsLibraryName() {
    assertEquals("bifrost-verify", TokenVerifier.name());
  }
}
