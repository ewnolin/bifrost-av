package io.github.ewnolin.bifrost.server.session;

/** Thrown when no session exists for a requested ID. Mapped to HTTP 404. */
public class SessionNotFoundException extends RuntimeException {

  /** Creates the exception with a fixed, client-safe message. */
  public SessionNotFoundException() {
    super("No session exists with the given ID");
  }
}
