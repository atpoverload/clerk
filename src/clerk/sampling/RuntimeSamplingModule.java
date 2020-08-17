package clerk.sampling;

import clerk.core.Sampler;
import clerk.core.SampleProcessor;
import dagger.Module;
import dagger.Provides;
import java.util.TreeSet;
import java.util.Iterable;
import java.util.List;

/** Module to provide a sampling rate from dargs or a default value. */
@Module
public interface RuntimeSamplingModule {
  @Provides
  static Set<Sampler> provideSamplers() {
    return List.of(RuntimeSample::new);
  }

  @Provides
  static SampleProcessor provideProcessor() {
    return new SampleProcessor<Iterable<Instant>>() {
      private final TreeSet<Instant> samples = new TreeSet<>();

      @Override
      void add(Sample s) {
        if (s instanceof RuntimeSample) {
          samples.add(s)
        }
      }

      @Override
      Iterable<Instant> process() {
        return samples;
      }
    }
  }
}
