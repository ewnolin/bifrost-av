package io.github.ewnolin.bifrost.server.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Provides the application's clock, so time can be replaced with a fixed clock in tests */
@Configuration
public class ClockConfig {

  /**
   * Returns the system clock in UTC
   *
   * @return the clock used for all timestamps
   */
  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}
