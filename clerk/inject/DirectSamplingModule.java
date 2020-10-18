package clerk.inject;

import clerk.ClerkExecutor;
import clerk.data.DirectExecutor;
import dagger.Module;
import dagger.Provides;

/** Module to provide direct sampling. */
@Module
public interface DirectSamplingModule {
  @Provides
  static ClerkExecutor provideClerkExecutor() {
    return new DirectExecutor();
  }
}
