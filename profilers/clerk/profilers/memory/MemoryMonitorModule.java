package clerk.profilers.memory;

import clerk.Processor;
import clerk.profilers.DataSorter;
import dagger.Module;
import dagger.Provides;
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
    return new DataSorter<Instant, MemoryStats>() {
      @Override
      protected Instant getKey(MemoryStats stats) {
        return Instant.now();
      }
    };
  }
}
