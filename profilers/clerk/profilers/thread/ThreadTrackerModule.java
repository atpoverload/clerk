package clerk.profilers.thread;

import clerk.core.DataProcessor;
import clerk.core.Scheduler;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/** Module to time how long the profiler has run. */
@Module
public interface ThreadTrackerModule {
  @Provides
  static Iterable<Supplier<Integer>> provideSources() {
    return List.of(() -> Thread.activeCount());
  }

  @Provides
  static DataProcessor<Integer, Map<Instant, Integer>> provideProcessor() {
    return new DataProcessor<Integer, Map<Instant, Integer>>() {
      private TreeMap<Instant, Integer> counts = new TreeMap<>();

      @Override
      public void add(Integer count) {
        synchronized (counts) {
          counts.put(Instant.now(), count);
        }
      }

      @Override
      public Map<Instant, Integer> process() {
        Map<Instant, Integer> counts = this.counts;
        synchronized (counts) {
          this.counts = new TreeMap<>();
        }
        return counts;
      }
    };
  }
}
