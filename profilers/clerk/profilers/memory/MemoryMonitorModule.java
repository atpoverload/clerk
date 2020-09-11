package clerk.profilers.memory;

import clerk.Processor;
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
public interface MemoryMonitorModule {
  @Provides
  static Iterable<Supplier<MemoryStats>> provideSources() {
    return List.of(() -> new MemoryStats());
  }

  @Provides
  static Processor<MemoryStats, Map<Instant, MemoryStats>> provideProcessor() {
    return new Processor<MemoryStats, Map<Instant, MemoryStats>>() {
      private TreeMap<Instant, MemoryStats> stats = new TreeMap<>();

      @Override
      public void add(MemoryStats stats) {
        synchronized (stats) {
          this.stats.put(Instant.now(), stats);
        }
      }

      @Override
      public Map<Instant, MemoryStats> process() {
        Map<Instant, MemoryStats> stats = this.stats;
        synchronized (stats) {
          this.stats = new TreeMap<>();
        }
        return stats;
      }
    };
  }
}
