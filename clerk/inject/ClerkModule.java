package clerk.inject;

import clerk.Clerk;
import clerk.ClerkExecutor;
import clerk.Processor;
import dagger.Module;
import dagger.Provides;
import java.util.Set;
import java.util.function.Supplier;

/** Module to provide clerk with the necessary data and pumbling through an injection graph. */
// i guess this is clever but i feel like it's brittle by virtue of wildcards
@Module
public interface ClerkModule {
  @Provides
  static Clerk provideClerk(
      @ClerkComponent Set<Supplier<?>> sources, Processor<?, ?> processor, ClerkExecutor executor) {
    return new Clerk(sources, processor, executor);
  }
}
