package io.github.ewnolin.bifrost.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Entry point for the Bifrost verification service */
@SpringBootApplication
public class BifrostServerApplication {

  /**
   * Starts the server
   *
   * @param args command-line arguments passed to Spring Boot
   */
  public static void main(String[] args) {
    SpringApplication.run(BifrostServerApplication.class, args);
  }
}
