package clerk.concurrent;

import dagger.Module;
import dagger.Provides;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.Duration;

/** Module to provide a thread pool to the scheduler. */
@Module
public interface ParallelSchedulingModule {
  static final int THREAD_POOL_SIZE = 4;
  static final AtomicInteger counter = new AtomicInteger();

  static String clerkName(Class cls) {
    return String.join("-",
      "clerk",
      String.format("%02d", counter.getAndIncrement()),
      cls.getSimpleName());
  }

  // make sure each sampler has a thread
  @Provides
  static ScheduledExecutorService provideExecutor() {
    return Executors.newScheduledThreadPool(
      THREAD_POOL_SIZE,
      r -> new Thread(
        r,
        clerkName(r.getClass())));
  }
}
