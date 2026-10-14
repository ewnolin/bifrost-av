package io.github.ewnolin.bifrost.server.session;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Body of {@code POST /sessions}.
 *
 * @param minAge the minimum age the player must prove
 */
public record CreateSessionRequest(
    @Schema(
            description =
                "Minimum age the player must prove, in years. Allowed values are configured on the server.",
            example = "15",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        Integer minAge) {}
