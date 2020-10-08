package clerk.profilers;

import clerk.ClerkComponent;
import clerk.Processor;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

/** Module to measure how long the clerk has run since starting. */
@Module
interface StopwatchModule {
  @Provides
  @ClerkComponent
  @IntoSet
  static Supplier<?> provideSource() {
    return Instant::now;
  }

  @Provides
  static Processor<?, Duration> provideProcessor() {
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
