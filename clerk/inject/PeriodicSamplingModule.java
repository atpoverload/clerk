package clerk.inject;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.ClerkExecutor;
import clerk.data.AsynchronousSteadyStateExecutor;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Module to provide a periodic sampling implementation.
 *
 * <p>The sampling rate and worker count are customizable from the system properties with prefix
 * "clerk.sampling".
 */
@Module
public interface PeriodicSamplingModule {
  static final AtomicInteger counter = new AtomicInteger();
  static final String DEFAULT_RATE_MS = "41";
  static final String DEFAULT_POOL_SIZE = "4";

  static String clerkName() {
    return String.join("-", "clerk", String.format("%02d", counter.getAndIncrement()));
  }

  @Provides
  @ClerkComponent
  static Duration provideSamplingRate() {
    return Duration.ofMillis(
        Long.parseLong(System.getProperty("clerk.sampling.rate", DEFAULT_RATE_MS)));
  }

  @Provides
  @ClerkComponent
  static ScheduledExecutorService provideExecutor() {
    return newScheduledThreadPool(
        Integer.parseInt(System.getProperty("clerk.sampling.workers", DEFAULT_POOL_SIZE)),
        r -> {
          Thread t = new Thread(r, clerkName());
          t.setDaemon(true);
          return t;
        });
  }

  @Provides
  static ClerkExecutor provideClerkExecutor(
      @ClerkComponent Duration period, @ClerkComponent ScheduledExecutorService executor) {
    return new AsynchronousSteadyStateExecutor(period, executor);
  }
}
