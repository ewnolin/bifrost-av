package io.github.ewnolin.bifrost.server.error;

import io.github.ewnolin.bifrost.server.session.SessionNotFoundException;
import io.github.ewnolin.bifrost.server.session.SessionStateException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Turns exceptions from any controller into RFC 9457 {@link ProblemDetail} responses, so every API
 * error has the same shape and never exposes internal details.
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

  /**
   * Handles requests for sessions that do not exist.
   *
   * @param ex the exception
   * @return a 404 problem response
   */
  @ExceptionHandler(SessionNotFoundException.class)
  public ProblemDetail handleSessionNotFound(SessionNotFoundException ex) {
    return problem(HttpStatus.NOT_FOUND, "Session not found", ex.getMessage());
  }

  /**
   * Handles state changes that are not allowed, such as completing an expired session.
   *
   * @param ex the exception
   * @return a 409 problem response
   */
  @ExceptionHandler(SessionStateException.class)
  public ProblemDetail handleSessionState(SessionStateException ex) {
    return problem(HttpStatus.CONFLICT, "Session state conflict", ex.getMessage());
  }

  /**
   * Handles requests with values that are not allowed.
   *
   * @param ex the exception
   * @return a 400 problem response
   */
  @ExceptionHandler(InvalidRequestException.class)
  public ProblemDetail handleInvalidRequest(InvalidRequestException ex) {
    return problem(HttpStatus.BAD_REQUEST, "Invalid request", ex.getMessage());
  }

  /**
   * Handles anything unexpected. Details are logged, never returned to the client.
   *
   * @param ex the exception
   * @return a 500 problem response with a generic message
   */
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleUnexpected(Exception ex) {
    log.error("Unhandled exception", ex);
    return problem(
        HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "An unexpected error occurred.");
  }

  /** Adds the names of invalid fields to Bean Validation errors. */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemDetail problem = ex.getBody();
    problem.setTitle("Invalid request");
    Map<String, String> errors = new LinkedHashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    problem.setProperty("errors", errors);
    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  private static ProblemDetail problem(HttpStatus status, String title, String detail) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
    problem.setTitle(title);
    return problem;
  }
}
