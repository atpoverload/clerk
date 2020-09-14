package clerk.concurrent;

import dagger.Module;
import dagger.Provides;

@Module
public interface DirectSamplingModule {
  @Provides
  static Scheduler bindScheduler() {
    return new DirectScheduler();
  }
}
