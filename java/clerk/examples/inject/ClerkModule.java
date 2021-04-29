package clerk.examples.inject;

import clerk.Clerk;
import clerk.DataCollector;
import clerk.DataProcessor;
import clerk.util.StringMappedClerk;
import dagger.Module;
import dagger.Provides;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Module
interface ClerkModule {
  @Provides
  public static Clerk<Map<Class<?>, List<Object>>> provideClerk(
      @ClerkComponent Map<String, Supplier<?>> sources,
      @ClerkComponent DataProcessor<?, Map<Class<?>, List<Object>>> processor,
      @ClerkComponent Map<String, DataCollector> collectors) {
    return new StringMappedClerk(sources, processor, collectors);
  }
}
