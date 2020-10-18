package clerk.contrib.inject;

import clerk.Processor;
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
    return new Processor<Instant, Duration>() {
      private Instant start = Instant.EPOCH;
      private Instant end = Instant.EPOCH;

      @Override
      public void add(Instant timestamp) {
        if (start.equals(Instant.EPOCH)) {
          this.start = timestamp;
        } else if (end.equals(Instant.EPOCH)) {
          this.end = timestamp;
        } else {
          this.start = this.end;
          this.end = timestamp;
        }
      }

      @Override
      public Duration process() {
        return Duration.between(start, end);
      }
    };
  }
}
