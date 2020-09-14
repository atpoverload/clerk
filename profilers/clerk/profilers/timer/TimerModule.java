package clerk.profilers.timer;

import clerk.DataProcessor;
import clerk.DataSource;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

/** Module to time how long the profiler has run. */
@Module
public interface TimerModule {
  @Provides
  @DataSource
  static Iterable<Supplier<Instant>> provideSources() {
    return List.of(() -> Instant.now());
  }

  @Provides
  static DataProcessor<Instant, Duration> provideProcessor() {
    return new DataProcessor<Instant, Duration>() {
      private Instant start;
      private Instant end;

      @Override
      public void accept(Instant timestamp) {
        if (start == null) {
          this.start = timestamp;
        } else if (end == null) {
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
