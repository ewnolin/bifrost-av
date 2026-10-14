package io.github.ewnolin.bifrost.server.session;

import jakarta.validation.constraints.NotNull;

/**
 * Body of {@code POST /sessions}.
 *
 * @param minAge the minimum age the player must prove
 */
public record CreateSessionRequest(@NotNull Integer minAge) {}
