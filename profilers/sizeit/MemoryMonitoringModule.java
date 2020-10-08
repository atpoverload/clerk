package clerk.profilers;

import clerk.ClerkComponent;
import clerk.Processor;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/** Module to measure how long the clerk has run since starting. */
@Module
interface MemoryMonitoringModule {
  @Provides
  @ClerkComponent
  @IntoSet
  static Supplier<?> provideSource() {
    return () -> {
      return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    };
  }

  @Provides
  static Processor<?, Map<Instant, Long>> provideProcessor() {
    return new Processor<Long, Map<Instant, Long>>() {
      private TreeMap<Instant, Long> data = new TreeMap<>();

      @Override
      public void add(Long value) {
        synchronized (data) {
          data.put(Instant.now(), value);
        }
      }

      @Override
      public TreeMap<Instant, Long> process() {
        TreeMap<Instant, Long> data = this.data;
        synchronized (this.data) {
          this.data = new TreeMap<>();
        }
        return data;
      }
    };
  }
}
