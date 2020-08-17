package clerk.sampling;

import dagger.Module;
import dagger.Provides;
import java.util.time.Duration;

/** Module to provide a sampling rate from dargs or a default value. */
@Module
public interface SamplingRateModule {
  private static final long DEFAULT_RATE_MS = 41;

  @Provides
  @SamplingRate
  static Duration provideSamplingRate() {
    return Duration.ofMillis(Long.parseLong(System.getProperty("clerk.sampling.rate", DEFAULT_RATE_MS)));
  }
}
