package clerk.profilers.memory;

import clerk.DataProcessor;
import clerk.DataSource;
import dagger.Module;
import dagger.Provides;
import java.util.List;
import java.util.function.Supplier;

/** Module to time how long the profiler has run. */
@Module
public interface MemoryMonitorModule {
  @Provides
  @DataSource
  static Iterable<Supplier<MemoryStats>> provideSources() {
    return List.of(() -> new MemoryStats());
  }

  @Provides
  static DataProcessor<MemoryStats, Long> provideProcessor() {
    return new DataProcessor<MemoryStats, Long>() {
      private MemoryStats start;
      private MemoryStats end;

      @Override
      public void accept(MemoryStats timestamp) {
        if (start == null) {
          this.start = timestamp;
        } else if (end == null) {
          this.end = timestamp;
        } else {
          this.start = this.end;
          this.end = timestamp;
        }
      }

      @Override
      public Long get() {
        return start.getFreeMemory() - end.getFreeMemory();
      }
    };
  }
}
