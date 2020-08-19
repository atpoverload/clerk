package clerk.concurrent;

import clerk.core.Sampler;
import clerk.sampling.SamplingRate;
import dagger.Module;
import dagger.Provides;
// TODO: is this right?
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.Duration;

/**
 * Module to provide an executor for the profiler. Users can provide their
 * own executor if they like (unit testing).
 */
@Module
public interface ParallelSchedulingModule {
  static final AtomicInteger counter = new AtomicInteger();

  static String clerkName(Class cls) {
    return String.join("-",
      "clerk",
      String.format("%02d", counter.getAndIncrement()),
      cls.getSimpleName());
  }

  // make sure each sampler has a thread
  @Provides
  static ScheduledExecutorService provideExecutor(@SamplingRate Duration rate, Set<Sampler> samplers) {
    if (samplers.size() > 0) {
      return Executors.newScheduledThreadPool(
        samplers.size(),
        r -> new Thread(
          r,
          clerkName(r.getClass())));
    } else {
      return Executors.newSingleThreadScheduledExecutor(
        r -> new Thread(
          r,
          clerkName(r.getClass())));
    }
  }
}