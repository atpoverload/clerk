package clerk.concurrent;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.ClerkComponent;
import clerk.ClerkExecutor;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/** Module to provide periodic sampling with an adjustable period and worker count. */
@Module
public interface PeriodicSamplingModule {
  static final AtomicInteger counter = new AtomicInteger();
  static final String DEFAULT_RATE_MS = "41";
  static final String DEFAULT_POOL_SIZE = "4";

  static String clerkName(Class cls) {
    return String.join(
        "-", "clerk", String.format("%02d", counter.getAndIncrement()), cls.getSimpleName());
  }

  @Provides
  @ClerkComponent
  static Duration provideSamplingRate() {
    return Duration.ofMillis(
        Long.parseLong(System.getProperty("clerk.sampling.rate", DEFAULT_RATE_MS)));
  }

  @Provides
  static ScheduledExecutorService provideExecutor() {
    int poolSize =
        Integer.parseInt(System.getProperty("clerk.sampling.threads", DEFAULT_POOL_SIZE));
    return newScheduledThreadPool(poolSize, r -> new Thread(r, clerkName(r.getClass())));
  }

  @Binds
  abstract ClerkExecutor provideClerkExecutor(AsynchronousSteadyStateExecutor executor);
}
