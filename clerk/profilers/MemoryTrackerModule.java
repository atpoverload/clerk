package clerk.profilers;

import clerk.Processor;
import clerk.DataSource;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** Module to track the difference in memory usage between two snapshots. */
@Module
interface MemoryTrackerModule {
  @Provides
  @DataSource
  static Iterable<Supplier<MemoryStats>> provideSources() {
    return List.of(() -> new MemoryStats());
  }

  @Provides
  static Processor<MemoryStats, Iterable<MemoryStats>> provideProcessor() {
    return new Processor<MemoryStats, Iterable<MemoryStats>>() {
      private ArrayList<MemoryStats> data = new ArrayList<>();

      @Override
      public void accept(MemoryStats stats) {
        if (!data.equals(MemoryStats.ZERO)) {
          data.add(stats);
        }
      }

      @Override
      public Iterable<MemoryStats> get() {
        ArrayList<MemoryStats> data = this.data;
        this.data = new ArrayList<>();
        return data;
      }
    };
  }
}
