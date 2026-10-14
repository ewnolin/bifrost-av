package io.github.ewnolin.bifrost.server.session;

/**
 * Thrown when a session cannot make the requested state change, for example because it has expired
 * or was already completed. Mapped to HTTP 409.
 */
public class SessionStateException extends RuntimeException {

  /**
   * Creates the exception.
   *
   * @param message client-safe explanation, without personal data
   */
  public SessionStateException(String message) {
    super(message);
  }
}
