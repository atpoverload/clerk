package clerk.dagger;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.ClerkComponent;
import clerk.InjectableClerk;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Module to provide a user customizable interface for the execution policy.
 *
 * <p>Each user of this module must {@link Provide} a {@link ClerkComponent} String as their user to
 * differentiate from other potential users. The resultant formatting for jvm options will be
 * "clerk.user.property". Therefore, the default execution period can be set to 500 ms with
 * -Dclerk.user.period=500.
 *
 * <p>This currently supports a default .period in ms and a .worker count.
 */
// TODO(timurbey): in **theory**, i'd like to support a more verbose set of options such as time
// units from the jvm options
@Module
public interface ClerkUserExecutionModule {
  static final AtomicInteger counter = new AtomicInteger();
  static final String DEFAULT_PERIOD_MS = "41";
  static final String DEFAULT_POOL_SIZE = "4";

  static String clerkName(String user) {
    return String.join("-", "clerk", user, String.format("%02d", counter.getAndIncrement()));
  }

  @Provides
  @ClerkComponent
  static Duration provideDefaultPolicy(@ClerkComponent String user) {
    return Duration.ofMillis(
        Long.parseLong(
            System.getProperty(String.join(".", "clerk", user, "period"), DEFAULT_PERIOD_MS)));
  }

  @Provides
  @IntoMap
  @StringKey(InjectableClerk.DEFAULT_POLICY_KEY)
  @ClerkComponent
  static ExecutionPolicy providePeriod(@ClerkComponent Duration defaultPeriod) {
    return new SteadyStatePeriodicExecutionPolicy(defaultPeriod);
  }

  @Provides
  @ClerkComponent
  static ScheduledExecutorService provideExecutor(@ClerkComponent String user) {
    return newScheduledThreadPool(
        Integer.parseInt(
            System.getProperty(String.join(".", "clerk", user, "workers"), DEFAULT_POOL_SIZE)),
        r -> {
          Thread t = new Thread(r, clerkName(user));
          t.setDaemon(true);
          return t;
        });
  }
}
