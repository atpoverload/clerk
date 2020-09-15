package clerk.profilers;

import clerk.Processor;
import clerk.DataSource;
import dagger.Module;
import dagger.Provides;
import java.util.List;
import java.util.function.Supplier;

/** Module to track the difference in memory usage between two snapshots. */
@Module
public interface MemoryMonitorModule {
  @Provides
  @DataSource
  static Iterable<Supplier<MemoryStats>> provideSources() {
    return List.of(() -> new MemoryStats());
  }

  @Provides
  static Processor<MemoryStats, Long> provideProcessor() {
    return new Processor<MemoryStats, Long>() {
      private MemoryStats start = MemoryStats.ZERO;
      private MemoryStats end = MemoryStats.ZERO;

      @Override
      public void accept(MemoryStats timestamp) {
        if (start.equals(MemoryStats.ZERO)) {
          this.start = timestamp;
        } else if (end.equals(MemoryStats.ZERO)) {
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
