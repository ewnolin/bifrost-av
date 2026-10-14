package io.github.ewnolin.bifrost.server.session;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.RepeatedTest;

class SessionServiceConcurrencyTest {

  private static final int THREADS = 10;

  @RepeatedTest(20)
  void completeSession_concurrentCalls_exactlyOneSucceeds() throws Exception {
    SessionService service =
        new SessionService(
            new InMemorySessionRepository(),
            new SessionIdGenerator(),
            Clock.fixed(Instant.parse("2026-01-01T12:00:00Z"), ZoneOffset.UTC),
            new SessionProperties(Duration.ofMinutes(5), Set.of(15)));
    String id = service.createSession(15).id();

    CountDownLatch start = new CountDownLatch(1);
    AtomicInteger successes = new AtomicInteger();
    AtomicInteger rejections = new AtomicInteger();
    ExecutorService pool = Executors.newFixedThreadPool(THREADS);

    for (int i = 0; i < THREADS; i++) {
      pool.submit(
          () -> {
            start.await();
            try {
              service.completeSession(id, SessionStatus.VERIFIED);
              successes.incrementAndGet();
            } catch (SessionStateException e) {
              rejections.incrementAndGet();
            }
            return null;
          });
    }
    start.countDown();
    pool.shutdown();
    pool.awaitTermination(5, TimeUnit.SECONDS);

    assertEquals(1, successes.get());
    assertEquals(THREADS - 1, rejections.get());
  }
}
