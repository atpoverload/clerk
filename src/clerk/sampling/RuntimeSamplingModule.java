package clerk.sampling;

import clerk.core.Sample;
import clerk.core.Sampler;
import clerk.core.SampleProcessor;
import dagger.Module;
import dagger.Provides;
import java.time.Instant;
import java.util.Set;
import java.util.TreeSet;

/** Module to provide a sampling rate from dargs or a default value. */
@Module
public interface RuntimeSamplingModule {
  @Provides
  static Set<Sampler> provideSamplers() {
    return Set.of(RuntimeSample::new);
  }

  @Provides
  static SampleProcessor<Iterable<Instant>> provideProcessor() {
    return new SampleProcessor<Iterable<Instant>>() {
      private final TreeSet<Instant> samples = new TreeSet<>();

      @Override
      public void add(Sample s) {
        if (s instanceof RuntimeSample) {
          samples.add(((RuntimeSample) s).getTimestamp());
        }
      }

      @Override
      public Iterable<Instant> process() {
        return samples;
      }
    };
  }
}
