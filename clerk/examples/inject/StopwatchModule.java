package clerk.examples.inject;

import clerk.Processor;
import clerk.examples.data.RelativePairStorage;
import clerk.inject.ClerkComponent;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

/** Module to provide clerk with the necessary data and pumbling through an inject graph. */
@Module
public interface StopwatchModule {
  @Provides
  @IntoSet
  @ClerkComponent
  static Supplier<?> provideSource() {
    return Instant::now;
  }

  @Provides
  static Processor<?, ?> provideProcessor() {
    return new RelativePairStorage<Instant, Duration>() {
      @Override
      public Duration process() {
        return Duration.between(getFirst(), getSecond());
      }
    };
  }
}
