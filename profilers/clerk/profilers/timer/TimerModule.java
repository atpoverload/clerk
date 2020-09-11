package clerk.profilers.timer;

import clerk.Processor;
import clerk.concurrent.Scheduler;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.function.Supplier;

/** Module to time how long the profiler has run. */
@Module
public interface TimerModule {
  @Provides
  static Iterable<Supplier<Duration>> provideSources() {
    return List.of(
      new Supplier<Duration>() {
        private Instant start = Instant.now();

        @Override
        public Duration get() {
          Instant now = Instant.now();
          Duration elapsed = Duration.between(start, Instant.now());
          start = now;
          return elapsed;
        }
      }
    )
  }

  // create a two-shot scheduler
  @Provides
  static Scheduler provideScheduler() {
    return new Scheduler() {
      @Override
      public void schedule(Runnable r) { }

      @Override
      public void cancel() { }
    };
  }

  @Binds
  abstract DataProcessor<Duration, Duration> provideProcessor(SourceWrapper processor);
}
