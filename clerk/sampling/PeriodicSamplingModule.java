package clerk.concurrent;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/** Module to provide periodic sampling with an adjustable period. */
@Module
public interface PeriodicSamplingModule {
  static final AtomicInteger counter = new AtomicInteger();
  static final int DEFAULT_RATE_MS = 41; // formalize this
  static final int THREAD_POOL_SIZE = 4; // formalize this

  static String clerkName(Class cls) {
    return String.join("-",
      "clerk",
      String.format("%02d", counter.getAndIncrement()),
      cls.getSimpleName());
  }

  @Provides
  @SchedulingPeriod
  static Duration provideSamplingRate() {
    return Duration.ofMillis(Long.parseLong(System.getProperty(
      "clerk.sampling.rate",
      Integer.toString(DEFAULT_RATE_MS))));
  }

  // TODO(timurbey): is there any reason to change the pool from the source size?
  @Provides
  static ScheduledExecutorService provideExecutor() {
    return newScheduledThreadPool(
      THREAD_POOL_SIZE,
      r -> new Thread(
        r,
        clerkName(r.getClass())));
  }

  @Binds
  abstract TaskRunner bindTaskRunner(PeriodicTaskRunner schedulerImpl);
}
