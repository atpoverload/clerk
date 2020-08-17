package clerk.concurrent;

import clerk.core.Sampler;
import clerk.sampling.SamplingRate;
import dagger.Module;
import dagger.Provides;
// TODO: is this right?
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Module to provide an executor for the profiler. Users can provide their
 * own executor if they like (unit testing).
 */
@Module
public interface ParallelExecutorModule {
  private static final AtomicInteger counter = new AtomicInteger();

  private static clerkName(Class cls) {
    return String.join("-",
      "clerk",
      String.format("%02d", counter.getAndIncrement())
      r.getClass().getSimpleName());
  }

  // make sure each sampler has a thread
  @Provides
  static ExecutorService provideExecutor(@SamplingRate Duration rate, Set<Sampler> samplers) {
    return Executors.newFixedThreadPool(
      samplers.size(),
      r -> new Thread(
        new SteadyStateScheduledRunnable(r, rate.toEpochMillis()),
        clerkName(r.getClass())));
  }
}
