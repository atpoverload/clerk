package clerk.concurrent;

import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.Duration;

/** Module to provide a single thread pool to the scheduler. */
@Module
public interface SerialSchedulingModule {
  @Provides
  static ScheduledExecutorService provideExecutor() {
    return Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "clerk"));
  }
}
