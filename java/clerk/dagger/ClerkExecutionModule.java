package clerk.dagger;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.ClerkComponent;
import clerk.InjectableClerk;
import clerk.data.FixedPeriodPolicy;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Dagger module to provide a {@link PeriodicExecutor} as the default execution policy that can be
 * customized with JVM arguments.
 *
 * <p>NOTE: All users of this module will share the same provisions.
 */
@Module
public interface ClerkExecutionModule {
  static final AtomicInteger counter = new AtomicInteger();
  static final String DEFAULT_PERIOD_MS = "41";
  static final String DEFAULT_POOL_SIZE = "4";

  static String clerkName() {
    return String.join("-", "clerk", String.format("%02d", counter.getAndIncrement()));
  }

  @Provides
  @ClerkComponent
  static ScheduledExecutorService provideScheduledExecutorService() {
    return newScheduledThreadPool(
        Integer.parseInt(
            System.getProperty(String.join(".", "clerk", "workers"), DEFAULT_POOL_SIZE)),
        r -> {
          Thread t = new Thread(r, clerkName());
          t.setDaemon(true);
          return t;
        });
  }

  @Provides
  @IntoMap
  @StringKey(InjectableClerk.DEFAULT_POLICY_KEY)
  @ClerkComponent
  static Executor provideDefaultExecutor(@ClerkComponent ScheduledExecutorService executor) {
    Duration period =
        Duration.ofMillis(
            Long.parseLong(
                System.getProperty(String.join(".", "clerk", "period"), DEFAULT_PERIOD_MS)));
    return new FixedPeriodPolicy(executor, period);
  }
}
