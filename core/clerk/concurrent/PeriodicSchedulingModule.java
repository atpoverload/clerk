package clerk.concurrent;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/** Module to provide a sampling rate from dargs or a default value. */
@Module
public interface PeriodicSchedulingModule {
  static final AtomicInteger counter = new AtomicInteger();
  static final int DEFAULT_RATE_MS = 41;
  static final int THREAD_POOL_SIZE = 4;

  static String clerkName(Class cls) {
    return String.join("-",
      "clerk",
      String.format("%02d", counter.getAndIncrement()),
      cls.getSimpleName());
  }

  @Provides
  @PeriodicSchedulingRate
  static Duration provideSamplingRate() {
    return Duration.ofMillis(Long.parseLong(System.getProperty(
      "clerk.sampling.rate",
      Integer.toString(DEFAULT_RATE_MS)
    )));
  }

  // make sure each sampler has a thread
  @Provides
  static ScheduledExecutorService provideExecutor() {
    return newScheduledThreadPool(
      THREAD_POOL_SIZE,
      r -> new Thread(
        r,
        clerkName(r.getClass())));
  }

  @Binds
  abstract Scheduler bindScheduler(PeriodicExecutionScheduler schedulerImpl);
}
