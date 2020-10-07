package clerk.data;

import clerk.ClerkExecutor;
import dagger.Module;
import dagger.Provides;

/** Provision for direct sampling. */
@Module
public interface DirectSamplingModule {
  @Provides
  static ClerkExecutor provideClerkExecutor() {
    return new SynchronousExecutor();
  }
}
