package clerk.concurrent;

import chappie.profiling.Sampler;
import dagger.Module;
import dagger.Provides;
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

  // make sure each sampler has a thread
  @Provides
  static ExecutorService provideExecutor(Set<Sampler> samplers) {
    return Executors.newFixedThreadPool(
      samplers.size(), r -> new Thread(r,
        String.join("-",
          "clerk",
          String.format("%02d", counter.getAndIncrement())
          r.getClass().getSimpleName()
    )));
  }

  @Provides
  @SamplingRate
  static Duration provideSamplingRate() {
    return Duration.ofMillis(Long.parseLong(System.getProperty("clerk.rate", "41")));
  }
}
