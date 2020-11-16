package clerk.execution;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.ClerkComponent;
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
 * <p>Each user of this module must {@link Provide} a {@link ClerkComponent} String as their appName
 * to differentiate from other potential users. The resultant formatting for jvm options will be
 * "clerk.appName.property". Therefore, the default execution period can be set to 500 ms with
 * -Dclerk.appName.period=500.
 *
 * <p>This currently supports a default .period in ms and a .worker count.
 */
// TODO(timurbey): in **theory**, i'd like to support a more verbose set of options such as time
// units from the jvm options
@Module
public interface ClerkApplicationExecutorModule {
  static final AtomicInteger counter = new AtomicInteger();
  static final String DEFAULT_PERIOD_MS = "41";
  static final String DEFAULT_POOL_SIZE = "4";

  static String clerkName(String appName) {
    return String.join("-", "clerk", appName, String.format("%02d", counter.getAndIncrement()));
  }

  @Provides
  @ClerkComponent
  static Duration provideDefaultPolicy(@ClerkComponent String appName) {
    return Duration.ofMillis(
        Long.parseLong(
            System.getProperty(String.join(".", "clerk", appName, "period"), DEFAULT_PERIOD_MS)));
  }

  @Provides
  @IntoMap
  @StringKey(Clerk.DEFAULT_POLICY_KEY)
  @ClerkComponent
  static ExecutionPolicy providePeriod(@ClerkComponent Duration defaultPeriod) {
    return new SteadyStatePeriodicExecutionPolicy(defaultPeriod);
  }

  @Provides
  @ClerkComponent
  static ScheduledExecutorService provideExecutor(@ClerkComponent String appName) {
    return newScheduledThreadPool(
        Integer.parseInt(
            System.getProperty(String.join(".", "clerk", appName, "workers"), DEFAULT_POOL_SIZE)),
        r -> {
          Thread t = new Thread(r, clerkName(appName));
          t.setDaemon(true);
          return t;
        });
  }
}
