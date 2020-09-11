package clerk.concurrent;

import dagger.Module;
import dagger.Provides;

/** Module to provide a sampling rate from dargs or a default value. */
@Module
public interface OneShotSchedulingModule {
  @Provides
  static Scheduler provideScheduler() {
    return new OneShotScheduler();
  }
}
