package clerk.concurrent;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.core.Scheduler;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/** Module to provide a scheduler back-end for testing. */
@Module
public interface TestSchedulingModule {
  @Provides
  @SchedulingRate
  static Duration provideSamplingRate() {
    return Duration.ZERO;
  }

  @Provides
  static ScheduledExecutorService provideExecutor() {
    return newSingleThreadExecutor();
  }
}
