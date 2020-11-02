package clerk.inject;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/** Module to provide clerk with the necessary data and pumbling through an injection graph. */
// i guess this is clever but i feel like it's brittle by virtue of wildcards
@Module
public interface ClerkExecutorModule {
  static final AtomicInteger counter = new AtomicInteger();
  static final String DEFAULT_POOL_SIZE = "4";
  static final String DEFAULT_PERIOD_MS = "41";

  static String clerkName() {
    return String.join("-", "clerk", String.format("%02d", counter.getAndIncrement()));
  }

  @Provides
  @ClerkComponent
  static Duration provideDefaultPeriod() {
    return Duration.ofMillis(Long.parseLong(System.getProperty("clerk.rate", DEFAULT_PERIOD_MS)));
  }

  @Provides
  @ClerkComponent
  static ScheduledExecutorService provideExecutor() {
    return newScheduledThreadPool(
        Integer.parseInt(System.getProperty("clerk.workers", DEFAULT_POOL_SIZE)),
        r -> {
          Thread t = new Thread(r, clerkName());
          t.setDaemon(true);
          return t;
        });
  }
}
