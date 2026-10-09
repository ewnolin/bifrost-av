package io.github.bifrostage.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CiCheckTest {
  @Test
  void ciShouldFail() {
    assertEquals(1, 2);
  }
}
