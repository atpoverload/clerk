package clerk.concurrent;

import dagger.Module;
import dagger.Provides;

/** Provision for direct sampling. */
@Module
public interface DirectSamplingModule {
  @Provides
  static TaskRunner bindTaskRunner() {
    return new DirectTaskRunner();
  }
}
