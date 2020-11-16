package clerk.concurrent;

import static clerk.Clerk.DEFAULT_POLICY_KEY;
import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.ClerkComponent;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Module to provide a user customizable interface for the execution policy.
 *
 * <p>All users of this module will share default properties defined by "clerk.property".
 *
 * <p>This currently supports a default .period in ms and a .worker count.
 */
// TODO(timurbey): in **theory**, i'd like to support a more verbose set of options such as time
// units from the jvm options
@Module
public abstract class ClerkExecutorModule {
  private static final AtomicInteger counter = new AtomicInteger();
  private static final String DEFAULT_PERIOD_MS = "41";
  private static final String DEFAULT_POOL_SIZE = "4";

  static String clerkName() {
    return String.join("-", "clerk", String.format("%02d", counter.getAndIncrement()));
  }

  @Provides
  @ClerkComponent
  static Duration provideDefaultPeriod() {
    return Duration.ofMillis(
        Long.parseLong(System.getProperty(String.join(".", "clerk", "period"), DEFAULT_PERIOD_MS)));
  }

  @Provides
  @ClerkComponent
  static ScheduledExecutorService provideExecutor() {
    return newScheduledThreadPool(
        Integer.parseInt(
            System.getProperty(String.join(".", "clerk", "workers"), DEFAULT_POOL_SIZE)),
        r -> {
          Thread t = new Thread(r, clerkName());
          t.setDaemon(true);
          return t;
        });
  }

  @Binds
  @IntoMap
  @StringKey(DEFAULT_POLICY_KEY)
  @ClerkComponent
  abstract Executor provideDefaultExecutor(PeriodicSteadyStateExecutor executor);
}
