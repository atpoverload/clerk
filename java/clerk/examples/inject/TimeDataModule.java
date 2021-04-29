package clerk.examples.inject;

import clerk.DataCollector;
import clerk.collectors.DirectCollector;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import java.time.Instant;
import java.util.function.Supplier;

@Module
interface TimeDataModule {
  @Provides
  @IntoMap
  @StringKey("time_data")
  @ClerkComponent
  public static Supplier<?> provideData() {
    return Instant::now;
  }

  @Provides
  @IntoMap
  @StringKey("time_data")
  @ClerkComponent
  public static DataCollector provideCollector() {
    return new DirectCollector();
  }
}
