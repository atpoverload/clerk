package clerk.examples.inject;

import clerk.Processor;
import clerk.examples.MemoryMonitor.MemorySnapshot;
import clerk.examples.data.ListStorage;
import clerk.inject.ClerkComponent;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import java.time.Instant;
import java.util.function.Supplier;

/** Module to provide clerk with the necessary data and pumbling through an inject graph. */
@Module
public interface MemoryMonitorModule {
  @Provides
  @IntoSet
  @ClerkComponent
  static Supplier<?> provideSource() {
    return () ->
        new MemorySnapshot(
            Instant.now(), Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
  }

  @Provides
  static Processor<?, ?> provideProcessor() {
    return new ListStorage<MemorySnapshot>();
  }
}
