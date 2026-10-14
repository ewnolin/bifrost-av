package io.github.ewnolin.bifrost.server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** General information shown at the top of the generated API documentation. */
@Configuration
public class OpenApiConfig {

  private static final String LICENSE = "MIT";
  private static final String FALLBACK_VERSION = "dev";

  /**
   * Describes the Bifrost API.
   *
   * @return the OpenAPI metadata
   */
  @Bean
  public OpenAPI bifrostOpenApi(ObjectProvider<BuildProperties> buildProperties) {
    String version =
        buildProperties.stream()
            .map(BuildProperties::getVersion)
            .findFirst()
            .orElse(FALLBACK_VERSION);
    return new OpenAPI()
        .info(
            new Info()
                .title("Bifrost API")
                .version(version)
                .description(
                    "Privacy-preserving age verification for games. A game starts a session, the"
                        + " player verifies their age, and the game only ever learns the result —"
                        + " never personal data.")
                .license(new License().name(LICENSE)));
  }
}
