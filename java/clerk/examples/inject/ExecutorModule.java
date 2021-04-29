package clerk.examples.inject;

import static java.util.concurrent.Executors.newScheduledThreadPool;

import dagger.Module;
import dagger.Provides;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Module
interface ExecutorModule {
  static AtomicInteger counter = new AtomicInteger();
  static ThreadFactory daemonFactory =
      r -> {
        Thread t = new Thread(r, "clerk-" + counter.getAndIncrement());
        t.setDaemon(true);
        return t;
      };

  @Provides
  @ClerkComponent
  public static ScheduledExecutorService provideExecutor(
      @ClerkComponent Map<String, Supplier<?>> sources) {
    return newScheduledThreadPool(sources.size(), daemonFactory);
  }
}
