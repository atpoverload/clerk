package clerk.inject;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import clerk.AsynchronousClerk;
import clerk.Clerk;
import clerk.Processor;
import clerk.Scheduler;
import clerk.scheduling.SteadyStateScheduler;
import dagger.Module;
import dagger.Provides;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/** Module to provide clerk with the necessary data and pumbling through an injection graph. */
// i guess this is clever but i feel like it's brittle by virtue of wildcards
@Module
public interface AsynchronousClerkModule {
  static final AtomicInteger counter = new AtomicInteger();
  static final String DEFAULT_RATE_MS = "41";
  static final String DEFAULT_POOL_SIZE = "4";

  static String clerkName() {
    return String.join("-", "clerk", String.format("%02d", counter.getAndIncrement()));
  }

  @Provides
  static Scheduler provideScheduler() {
    return new SteadyStateScheduler(
        Instant::now,
        Duration.ofMillis(
            Long.parseLong(System.getProperty("clerk.sampling.rate", DEFAULT_RATE_MS))));
  }

  // TODO(timurbey): this should take a policy instead
  @Provides
  @ClerkComponent
  static ScheduledExecutorService provideExecutor() {
    return newScheduledThreadPool(
        Integer.parseInt(System.getProperty("clerk.sampling.workers", DEFAULT_POOL_SIZE)),
        r -> {
          Thread t = new Thread(r, clerkName());
          t.setDaemon(true);
          return t;
        });
  }

  @Provides
  static Clerk provideClerk(
      @ClerkComponent Set<Supplier<?>> sources,
      Processor<?, ?> processor,
      @ClerkComponent ScheduledExecutorService executor,
      Scheduler scheduler) {
    return new AsynchronousClerk<>(sources, processor, executor, scheduler);
  }
}
