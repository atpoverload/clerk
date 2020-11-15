package clerk.testing.inject;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import clerk.inject.ClerkComponent;
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
  @StringKey("default_period")
  @ClerkComponent
  static Duration provideDefaultPeriod() {
    return Duration.ofMillis(0);
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
