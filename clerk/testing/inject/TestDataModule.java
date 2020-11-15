package clerk.testing.inject;

import clerk.Processor;
import clerk.data.DummyStorage;
import clerk.inject.ClerkComponent;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/** Module to provide test functionality. */
@Module
public interface TestDataModule {
  static final CyclicBarrier testBarrier = new CyclicBarrier(2);
  static final AtomicInteger counter = new AtomicInteger();

  public static void awaitTestBarrier() throws Exception {
    if (testBarrier.getParties() == 1) {
      testBarrier.await();
    }
  }

  @Provides
  @IntoMap
  @StringKey("dummy_source")
  @ClerkComponent
  static Supplier<?> provideSource() {
    return () -> {
      try {
        testBarrier.await();
      } catch (Exception e) {
        System.out.println("we broke the barrier?");
        throw new RuntimeException(e);
      }

      return counter.incrementAndGet();
    };
  }

  @Provides
  static Processor<?, Integer> provideProcessor() {
    return new DummyStorage<Integer>();
  }
}
