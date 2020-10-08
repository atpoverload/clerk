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
interface ThreadMonitoringModule {
  @Provides
  @ClerkComponent
  @IntoSet
  static Supplier<?> provideSource() {
    return Thread::activeCount;
  }

  @Provides
  static Processor<?, Map<Instant, Integer>> provideProcessor() {
    return new Processor<Integer, Map<Instant, Integer>>() {
      private TreeMap<Instant, Integer> data = new TreeMap<>();

      @Override
      public void add(Integer count) {
        synchronized (data) {
          data.put(Instant.now(), count);
        }
      }

      @Override
      public TreeMap<Instant, Integer> process() {
        TreeMap<Instant, Integer> data = this.data;
        synchronized (this.data) {
          this.data = new TreeMap<>();
        }
        return data;
      }
    };
  }
}
