package io.github.ewnolin.bifrost.server.session;

import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Session settings, bound from {@code bifrost.session.*} in {@code application.yml}.
 *
 * @param ttl how long a new session stays open
 * @param allowedMinAges the minimum ages a game may ask to verify
 */
@ConfigurationProperties("bifrost.session")
public record SessionProperties(
    @DefaultValue("5m") Duration ttl,
    @DefaultValue({"13", "15", "16", "18"}) Set<Integer> allowedMinAges) {}
