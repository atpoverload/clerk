package clerk.profilers;

import clerk.Processor;
import clerk.DataSource;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

/** Module to measure how long the profiler has run. */
@Module
interface TimerModule {
  @Provides
  @DataSource
  static Iterable<Supplier<Instant>> provideSources() {
    return List.of(() -> Instant.now());
  }

  @Provides
  static Processor<Instant, Duration> provideProcessor() {
    return new Processor<Instant, Duration>() {
      private Instant start = Instant.EPOCH;
      private Instant end = Instant.EPOCH;

      @Override
      public void accept(Instant timestamp) {
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
      public Duration get() {
        return Duration.between(start, end);
      }
    };
  }
}
