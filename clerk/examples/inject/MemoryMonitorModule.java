package clerk.examples.inject;

import clerk.Processor;
import clerk.examples.data.ListStorage;
import clerk.inject.ClerkComponent;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

/** Module to provide a snapshot of the currently reserved memory and a */
@Module
public interface MemoryMonitorModule {
  @Provides
  @IntoMap
  @StringKey("memory_snapshot_source")
  @ClerkComponent
  static Supplier<?> provideSource() {
    return () ->
        new MemorySnapshot(
            Instant.now(), Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
  }

  @Provides
  static Processor<?, List<MemorySnapshot>> provideProcessor() {
    return new ListStorage<MemorySnapshot>();
  }
}
