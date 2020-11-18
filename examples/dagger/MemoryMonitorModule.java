package clerk.examples;

import clerk.ClerkComponent;
import clerk.Processor;
import clerk.data.ListStorage;
import clerk.examples.ExampleProtos.MemorySnapshot;
import com.google.protobuf.Timestamp;
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
  static Timestamp now() {
    Instant now = Instant.now();
    return Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build();
  }

  @Provides
  @IntoMap
  @StringKey("memory_snapshot_source")
  @ClerkComponent
  static Supplier<?> provideSource() {
    return () ->
        MemorySnapshot.newBuilder()
            .setTimestamp(now())
            .setTotalMemory(Runtime.getRuntime().totalMemory())
            .setFreeMemory(Runtime.getRuntime().freeMemory())
            .build();
  }

  @Provides
  static Processor<?, List<MemorySnapshot>> provideProcessor() {
    return new ListStorage<MemorySnapshot>();
  }
}
