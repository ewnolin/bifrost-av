package io.github.ewnolin.bifrost.server.error;

/** Thrown when a request is well-formed but its values are not allowed. Mapped to HTTP 400. */
public class InvalidRequestException extends RuntimeException {

  /**
   * Creates the exception.
   *
   * @param message client-safe explanation of what is wrong, without personal data
   */
  public InvalidRequestException(String message) {
    super(message);
  }
}
