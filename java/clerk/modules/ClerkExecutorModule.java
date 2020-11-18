package clerk.modules;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.Clerk;
import clerk.ClerkComponent;
import clerk.concurrent.PeriodicExecutor;
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
public interface ClerkExecutorModule {
  static final AtomicInteger counter = new AtomicInteger();
  static final String DEFAULT_PERIOD_MS = "41";
  static final String DEFAULT_POOL_SIZE = "4";

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
  @StringKey(Clerk.DEFAULT_POLICY_KEY)
  @ClerkComponent
  static Executor provideDefaultExecutor(
      @ClerkComponent ScheduledExecutorService executor, @ClerkComponent Duration period) {
    String policy = System.getProperty(String.join(".", "clerk", "executor"), "steady_state");
    if (policy.equals("steady_state")) {
      return new PeriodicExecutor(executor, period);
    } else {
      return executor;
    }
  }
}
