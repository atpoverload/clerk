package clerk.examples.inject;

import clerk.DataCollector;
import clerk.collectors.FixedPeriodCollector;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

@Module
interface MemoryDataModule {
  @Provides
  @IntoMap
  @StringKey("memory_data")
  @ClerkComponent
  public static Supplier<?> provideData() {
    return () -> Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
  }

  @Provides
  @IntoMap
  @StringKey("memory_data")
  @ClerkComponent
  public static DataCollector provideCollector(@ClerkComponent ScheduledExecutorService executor) {
    return new FixedPeriodCollector(executor, Duration.ofMillis(100));
  }
}
