package clerk.sampling;

import clerk.core.Sample;
import clerk.core.Sampler;
import clerk.core.SampleProcessor;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/** Module to time how long the profiler has run. */
@Module
public interface TimingModule {
  @Provides
  static Set<Sampler> provideSamplers() {
    return new HashSet<>();
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
