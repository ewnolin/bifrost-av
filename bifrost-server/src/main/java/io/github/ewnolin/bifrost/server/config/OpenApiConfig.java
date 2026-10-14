package io.github.ewnolin.bifrost.server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** General information shown at the top of the generated API documentation. */
@Configuration
public class OpenApiConfig {

  /**
   * Describes the Bifrost API.
   *
   * @return the OpenAPI metadata
   */
  @Bean
  public OpenAPI bifrostOpenApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Bifrost API")
                .version("0.1.0")
                .description(
                    "Privacy-preserving age verification for games. A game starts a session, the"
                        + " player verifies their age, and the game only ever learns the result —"
                        + " never personal data.")
                .license(new License().name("MIT")));
  }
}
