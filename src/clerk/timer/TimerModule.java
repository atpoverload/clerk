package clerk.timer;

import clerk.core.Sampler;
import clerk.core.SampleProcessor;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

/** Module to time how long the profiler has run. */
@Module
public interface TimerModule {
  @Provides
  static Iterable<Sampler> provideSamplers() {
    return new ArrayList<>();
  }

  @Provides
  static SampleProcessor<Duration> provideProcessor() {
    return new SampleProcessor<Duration>() {
      private Instant start = Instant.now();

      @Override
      public Duration process() {
        return Duration.between(start, Instant.now());
      }
    };
  }
}
