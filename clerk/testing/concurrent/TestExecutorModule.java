package clerk.testing.concurrent;

import static clerk.Clerk.DEFAULT_POLICY_KEY;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.ClerkComponent;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

/** Module to provide test functionality. */
@Module
public interface TestExecutorModule {
  @Provides
  @IntoMap
  @StringKey(DEFAULT_POLICY_KEY)
  @ClerkComponent
  static ExecutionPolicy provideDefaultPolicy() {
    return new SteadyStatePeriodicExecutionPolicy(Duration.ofMillis(0));
  }

  @Provides
  @ClerkComponent
  static ScheduledExecutorService provideExecutor() {
    return newSingleThreadScheduledExecutor(
        r -> {
          Thread t = new Thread(r);
          t.setDaemon(true);
          return t;
        });
  }
}
