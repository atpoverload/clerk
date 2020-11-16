package clerk.testing.data;

import clerk.ClerkComponent;
import clerk.Processor;
import clerk.data.DummyStorage;
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
  static final AtomicInteger counter = new AtomicInteger();
  // TODO(timurbey): this is still not working right
  static final CyclicBarrier testBarrier = new CyclicBarrier(2);

  public static void awaitTestBarrier() throws Exception {
    if (testBarrier.isBroken()) {
      return;
    }
    testBarrier.await();
    System.out.println(testBarrier.isBroken());
  }

  public static void resetTestBarrier() {
    testBarrier.reset();
  }

  @Provides
  @IntoMap
  @StringKey("dummy_source")
  @ClerkComponent
  static Supplier<?> provideSource() {
    return () -> {
      if (testBarrier.isBroken()) {
        throw new RuntimeException();
      }
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
